package com.matemagicos.biblioteca.service;

import com.matemagicos.biblioteca.DTO.CadastroRequestDTO;
import com.matemagicos.biblioteca.DTO.LoginRequestDTO;
import com.matemagicos.biblioteca.DTO.LoginResponseDTO;
import com.matemagicos.biblioteca.DTO.UsuarioDTO;
import com.matemagicos.biblioteca.models.Usuario;
import com.matemagicos.biblioteca.repository.UsuarioRepository;
import com.matemagicos.biblioteca.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public List<UsuarioDTO> listar() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    public UsuarioDTO cadastrar(CadastroRequestDTO dto) {
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
        return toDTO(salvo);
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

        return new LoginResponseDTO(toDTO(u), "Login realizado com sucesso!", token);
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
