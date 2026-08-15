package com.matemagicos.biblioteca.controller;

import com.matemagicos.biblioteca.DTO.CadastroRequestDTO;
import com.matemagicos.biblioteca.DTO.EsqueciSenhaRequestDTO;
import com.matemagicos.biblioteca.DTO.LoginRequestDTO;
import com.matemagicos.biblioteca.DTO.LoginResponseDTO;
import com.matemagicos.biblioteca.DTO.RedefinirSenhaRequestDTO;
import com.matemagicos.biblioteca.DTO.RefreshRequestDTO;
import com.matemagicos.biblioteca.DTO.VerificarEmailRequestDTO;
import com.matemagicos.biblioteca.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioService service;

    public AuthController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrar(@Valid @RequestBody CadastroRequestDTO dto) {
        try {
            LoginResponseDTO resposta = service.cadastrar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "ok", true,
                    "mensagem", resposta.getMensagem(),
                    "usuario", resposta.getUsuario(),
                    "token", resposta.getToken(),
                    "refreshToken", resposta.getRefreshToken()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "ok", false,
                    "erro", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO dto) {
        try {
            LoginResponseDTO resposta = service.login(dto);
            return ResponseEntity.ok(Map.of(
                    "ok", true,
                    "mensagem", resposta.getMensagem(),
                    "usuario", resposta.getUsuario(),
                    "token", resposta.getToken(),
                    "refreshToken", resposta.getRefreshToken()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "ok", false,
                    "erro", e.getMessage()));
        }
    }

    // Chamado automaticamente pelo app quando o access token expira.
    // Devolve um access token novo + um refresh token novo (rotacionado).
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@Valid @RequestBody RefreshRequestDTO dto) {
        try {
            Map<String, String> tokens = service.renovarToken(dto.getRefreshToken());
            return ResponseEntity.ok(Map.of(
                    "ok", true,
                    "token", tokens.get("token"),
                    "refreshToken", tokens.get("refreshToken")));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "ok", false,
                    "erro", e.getMessage()));
        }
    }

    // Revoga o refresh token no banco — logout de verdade, não só limpar o app
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@Valid @RequestBody RefreshRequestDTO dto) {
        service.logout(dto.getRefreshToken());
        return ResponseEntity.ok(Map.of(
                "ok", true,
                "mensagem", "Logout realizado com sucesso."));
    }

    // Sempre responde "ok" independente do email existir ou não — evita que alguém
    // descubra quais emails estão cadastrados só testando essa rota
    @PostMapping("/esqueci-senha")
    public ResponseEntity<?> esqueciSenha(@Valid @RequestBody EsqueciSenhaRequestDTO dto) {
        service.esqueciSenha(dto.getEmail());
        return ResponseEntity.ok(Map.of(
                "ok", true,
                "mensagem", "Se esse email estiver cadastrado, você vai receber um código em instantes."));
    }

    @PostMapping("/redefinir-senha")
    public ResponseEntity<?> redefinirSenha(@Valid @RequestBody RedefinirSenhaRequestDTO dto) {
        try {
            service.redefinirSenha(dto);
            return ResponseEntity.ok(Map.of(
                    "ok", true,
                    "mensagem", "Senha redefinida com sucesso! Faça login com a senha nova."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "ok", false,
                    "erro", e.getMessage()));
        }
    }

    // Mesmo formato de corpo do "esqueci-senha" (só email), por isso reaproveita o
    // DTO
    @PostMapping("/reenviar-verificacao")
    public ResponseEntity<?> reenviarVerificacao(@Valid @RequestBody EsqueciSenhaRequestDTO dto) {
        service.reenviarVerificacao(dto.getEmail());
        return ResponseEntity.ok(Map.of(
                "ok", true,
                "mensagem",
                "Se esse email estiver cadastrado e ainda não verificado, você vai receber um novo código."));
    }

    @PostMapping("/verificar-email")
    public ResponseEntity<?> verificarEmail(@Valid @RequestBody VerificarEmailRequestDTO dto) {
        try {
            service.verificarEmail(dto.getEmail(), dto.getCodigo());
            return ResponseEntity.ok(Map.of(
                    "ok", true,
                    "mensagem", "Email verificado com sucesso!"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "ok", false,
                    "erro", e.getMessage()));
        }
    }
}