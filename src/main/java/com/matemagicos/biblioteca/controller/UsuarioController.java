package com.matemagicos.biblioteca.controller;

import com.matemagicos.biblioteca.DTO.UsuarioDTO;
import com.matemagicos.biblioteca.service.UsuarioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public List<UsuarioDTO> listar() {
        return service.listar().stream()
                .map(u -> {
                    UsuarioDTO dto = new UsuarioDTO();
                    dto.setId(u.getIdUsuario());
                    dto.setNome(u.getNome());
                    dto.setEmail(u.getEmail());
                    dto.setIdade(u.getIdade());
                    dto.setNivelEscolar(u.getNivelEscolar());
                    dto.setTotalPontos(u.getTotalPontos());
                    dto.setMoedasMagicas(u.getMoedasMagicas());
                    return dto;
                })
                .toList();
    }
}
