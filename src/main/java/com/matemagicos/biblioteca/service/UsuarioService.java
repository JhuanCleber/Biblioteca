package com.matemagicos.biblioteca.service;

import com.matemagicos.biblioteca.DTO.CadastroRequestDTO;
import com.matemagicos.biblioteca.DTO.LoginRequestDTO;
import com.matemagicos.biblioteca.DTO.LoginResponseDTO;
import com.matemagicos.biblioteca.DTO.RedefinirSenhaRequestDTO;
import com.matemagicos.biblioteca.DTO.UsuarioDTO;
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
    private final EmailService emailService;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService,
            RefreshTokenService refreshTokenService, PasswordResetService passwordResetService,
            EmailService emailService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.passwordResetService = passwordResetService;
        this.emailService = emailService;
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

        Usuario u = new Usuario();
        u.setNome(dto.getNome().trim());
        u.setEmail(emailNormalizado);
        u.setSenha(passwordEncoder.encode(dto.getSenha()));
        u.setIdade(dto.getIdade());
        u.setNivelEscolar(dto.getNivelEscolar());
        u.setTotalPontos(0);
        u.setMoedasMagicas(0);

        Usuario salvo = repository.save(u);
        String token = jwtService.gerarToken(salvo.getIdUsuario(), salvo.getEmail());
        String refreshToken = refreshTokenService.gerar(salvo.getIdUsuario());

        return new LoginResponseDTO(toDTO(salvo), "Conta criada com sucesso!", token, refreshToken);
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

    private UsuarioDTO toDTO(Usuario u) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(u.getIdUsuario());
        dto.setNome(u.getNome());
        dto.setEmail(u.getEmail());
        dto.setIdade(u.getIdade());
        dto.setNivelEscolar(u.getNivelEscolar());
        dto.setTotalPontos(u.getTotalPontos());
        dto.setMoedasMagicas(u.getMoedasMagicas());
        return dto;
    }
}