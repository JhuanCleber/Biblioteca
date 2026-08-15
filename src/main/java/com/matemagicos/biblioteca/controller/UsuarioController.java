package com.matemagicos.biblioteca.controller;

import com.matemagicos.biblioteca.DTO.EditarPerfilRequestDTO;
import com.matemagicos.biblioteca.DTO.UsuarioDTO;
import com.matemagicos.biblioteca.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public List<UsuarioDTO> listar() {
        return service.listar();
    }

    @PutMapping("/perfil")
    public ResponseEntity<?> editarPerfil(@Valid @RequestBody EditarPerfilRequestDTO dto,
            Authentication authentication) {
        try {
            Integer idUsuario = (Integer) authentication.getDetails();
            UsuarioDTO usuarioAtualizado = service.editarPerfil(idUsuario, dto);
            return ResponseEntity.ok(Map.of(
                    "ok", true,
                    "mensagem", "Perfil atualizado!",
                    "usuario", usuarioAtualizado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "ok", false,
                    "erro", e.getMessage()));
        }
    }
}