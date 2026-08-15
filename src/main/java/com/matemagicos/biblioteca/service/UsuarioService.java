package com.matemagicos.biblioteca.service;

import com.matemagicos.biblioteca.DTO.CadastroRequestDTO;
import com.matemagicos.biblioteca.DTO.LoginRequestDTO;
import com.matemagicos.biblioteca.DTO.LoginResponseDTO;
import com.matemagicos.biblioteca.DTO.RedefinirSenhaRequestDTO;
import com.matemagicos.biblioteca.DTO.UsuarioDTO;
import com.matemagicos.biblioteca.models.EmailVerificationToken;
import com.matemagicos.biblioteca.models.PasswordResetToken;
import com.matemagicos.biblioteca.models.RefreshToken;
import com.matemagicos.biblioteca.models.Usuario;
import com.matemagicos.biblioteca.repository.UsuarioRepository;
import com.matemagicos.biblioteca.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordResetService passwordResetService;
    private final EmailVerificationService emailVerificationService;
    private final EmailService emailService;
    private final FiltroDeNomeService filtroDeNomeService;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService,
            RefreshTokenService refreshTokenService, PasswordResetService passwordResetService,
            EmailVerificationService emailVerificationService, EmailService emailService,
            FiltroDeNomeService filtroDeNomeService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.passwordResetService = passwordResetService;
        this.emailVerificationService = emailVerificationService;
        this.emailService = emailService;
        this.filtroDeNomeService = filtroDeNomeService;
    }

    public List<UsuarioDTO> listar() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    public LoginResponseDTO cadastrar(CadastroRequestDTO dto) {
        String emailNormalizado = dto.getEmail().trim().toLowerCase();

        if (repository.existsByEmail(emailNormalizado)) {
            throw new IllegalArgumentException("Este email já está cadastrado. Tente fazer login.");
        }

        // " Ana Maria " -> "Ana Maria" (o @Pattern do DTO já barrou
        // número/símbolo/emoji)
        String nomeNormalizado = dto.getNome().trim().replaceAll("\\s+", " ");

        if (filtroDeNomeService.contemPalavraProibida(nomeNormalizado)) {
            throw new IllegalArgumentException("Esse nome não pode ser usado. Escolha outro, por favor.");
        }

        Usuario u = new Usuario();
        u.setNome(nomeNormalizado);
        u.setEmail(emailNormalizado);
        u.setSenha(passwordEncoder.encode(dto.getSenha()));
        u.setIdade(dto.getIdade());
        u.setNivelEscolar(dto.getNivelEscolar());
        u.setTotalPontos(0);
        u.setMoedasMagicas(0);
        u.setEmailVerificado(false);

        Usuario salvo = repository.save(u);
        String token = jwtService.gerarToken(salvo.getIdUsuario(), salvo.getEmail());
        String refreshToken = refreshTokenService.gerar(salvo.getIdUsuario());

        enviarEmailVerificacaoSemQuebrarCadastro(salvo);

        return new LoginResponseDTO(toDTO(salvo), "Conta criada com sucesso!", token, refreshToken);
    }

    // Cadastro nunca pode falhar por causa do email de verificação (servidor de
    // email fora do ar, por exemplo) — o usuário sempre pode pedir reenvio depois
    private void enviarEmailVerificacaoSemQuebrarCadastro(Usuario u) {
        try {
            String codigo = emailVerificationService.gerarCodigo(u.getIdUsuario());
            emailService.enviarCodigoVerificacao(u.getEmail(), u.getNome(), codigo);
        } catch (Exception e) {
            System.err.println("Não foi possível enviar o email de verificação: " + e.getMessage());
        }
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {
        String emailNormalizado = dto.getEmail().trim().toLowerCase();
        Usuario u = repository.findByEmail(emailNormalizado)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Email ou senha incorretos. Verifique ou cadastre-se primeiro."));

        if (!passwordEncoder.matches(dto.getSenha(), u.getSenha())) {
            throw new IllegalArgumentException(
                    "Email ou senha incorretos. Verifique ou cadastre-se primeiro.");
        }

        String token = jwtService.gerarToken(u.getIdUsuario(), u.getEmail());
        String refreshToken = refreshTokenService.gerar(u.getIdUsuario());

        return new LoginResponseDTO(toDTO(u), "Login realizado com sucesso!", token, refreshToken);
    }

    // Recebe um refresh token válido, devolve um access token novo + um refresh
    // token novo (rotacionado)
    public Map<String, String> renovarToken(String refreshTokenRecebido) {
        RefreshToken rt = refreshTokenService.validar(refreshTokenRecebido);

        Usuario u = repository.findById(rt.getIdUsuario())
                .orElseThrow(() -> new IllegalArgumentException("Sessão expirada. Faça login novamente."));

        String novoAccessToken = jwtService.gerarToken(u.getIdUsuario(), u.getEmail());
        String novoRefreshToken = refreshTokenService.rotacionar(rt);

        Map<String, String> resultado = new HashMap<>();
        resultado.put("token", novoAccessToken);
        resultado.put("refreshToken", novoRefreshToken);
        return resultado;
    }

    public void logout(String refreshTokenRecebido) {
        refreshTokenService.revogar(refreshTokenRecebido);
    }

    // Não lança erro se o email não existir — se lançasse, daria pra descobrir
    // quais
    // emails estão cadastrados só tentando "esqueci senha" pra cada um (enumeration
    // attack)
    public void esqueciSenha(String email) {
        String emailNormalizado = email.trim().toLowerCase();
        repository.findByEmail(emailNormalizado).ifPresent(u -> {
            String codigo = passwordResetService.gerarCodigo(u.getIdUsuario());
            emailService.enviarCodigoRecuperacao(u.getEmail(), u.getNome(), codigo);
        });
    }

    public void redefinirSenha(RedefinirSenhaRequestDTO dto) {
        String emailNormalizado = dto.getEmail().trim().toLowerCase();
        Usuario u = repository.findByEmail(emailNormalizado)
                .orElseThrow(() -> new IllegalArgumentException("Código inválido ou expirado."));

        PasswordResetToken tokenValido = passwordResetService.validarCodigo(u.getIdUsuario(), dto.getCodigo().trim());

        u.setSenha(passwordEncoder.encode(dto.getNovaSenha()));
        repository.save(u);

        passwordResetService.marcarComoUsado(tokenValido);

        // Senha mudou — derruba qualquer sessão antiga ativa (segurança)
        refreshTokenService.revogarTodosDoUsuario(u.getIdUsuario());
    }

    // Reenvia o código de verificação. Não faz nada (silenciosamente) se o email
    // não
    // existir ou já estiver verificado — mesmo princípio de não vazar informação.
    public void reenviarVerificacao(String email) {
        String emailNormalizado = email.trim().toLowerCase();
        repository.findByEmail(emailNormalizado).ifPresent(u -> {
            if (u.isEmailVerificado()) {
                return;
            }
            String codigo = emailVerificationService.gerarCodigo(u.getIdUsuario());
            emailService.enviarCodigoVerificacao(u.getEmail(), u.getNome(), codigo);
        });
    }

    public void verificarEmail(String email, String codigo) {
        String emailNormalizado = email.trim().toLowerCase();
        Usuario u = repository.findByEmail(emailNormalizado)
                .orElseThrow(() -> new IllegalArgumentException("Código inválido ou expirado."));

        EmailVerificationToken tokenValido = emailVerificationService.validarCodigo(u.getIdUsuario(), codigo.trim());

        u.setEmailVerificado(true);
        repository.save(u);

        emailVerificationService.marcarComoUsado(tokenValido);
    }

    private UsuarioDTO toDTO(Usuario u) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(u.getIdUsuario());
        dto.setNome(u.getNome());
        dto.setEmail(u.getEmail());
        dto.setIdade(u.getIdade());
        dto.setNivelEscolar(u.getNivelEscolar());
        dto.setTotalPontos(u.getTotalPontos());
        dto.setMoedasMagicas(u.getMoedasMagicas());
        dto.setEmailVerificado(u.isEmailVerificado());
        return dto;
    }
}