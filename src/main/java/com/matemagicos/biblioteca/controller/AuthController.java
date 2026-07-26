package com.matemagicos.biblioteca.controller;

import com.matemagicos.biblioteca.DTO.CadastroRequestDTO;
import com.matemagicos.biblioteca.DTO.LoginRequestDTO;
import com.matemagicos.biblioteca.DTO.LoginResponseDTO;
import com.matemagicos.biblioteca.DTO.UsuarioDTO;
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
            UsuarioDTO usuario = service.cadastrar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "ok", true,
                    "mensagem", "Conta criada com sucesso!",
                    "usuario", usuario));
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
                    "token", resposta.getToken()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "ok", false,
                    "erro", e.getMessage()));
        }
    }
}
