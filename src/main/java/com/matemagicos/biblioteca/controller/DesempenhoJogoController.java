package com.matemagicos.biblioteca.controller;

import com.matemagicos.biblioteca.DTO.DesempenhoRequestDTO;
import com.matemagicos.biblioteca.DTO.DesempenhoResponseDTO;
import com.matemagicos.biblioteca.DTO.HistoricoItemDTO;
import com.matemagicos.biblioteca.service.DesempenhoJogoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/desempenho")
public class DesempenhoJogoController {

  private final DesempenhoJogoService service;

  public DesempenhoJogoController(DesempenhoJogoService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<?> registrar(@Valid @RequestBody DesempenhoRequestDTO dto, Authentication authentication) {
    try {

      Integer idUsuario = (Integer) authentication.getDetails();

      DesempenhoResponseDTO resposta = service.registrar(idUsuario, dto);
      return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
          "ok", true,
          "mensagem", "Resultado registrado com sucesso!",
          "resultado", resposta));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(Map.of(
          "ok", false,
          "erro", e.getMessage()));
    }
  }

  @GetMapping("/historico")
  public ResponseEntity<?> historico(Authentication authentication) {
    Integer idUsuario = (Integer) authentication.getDetails();
    List<HistoricoItemDTO> historico = service.obterHistorico(idUsuario);

    return ResponseEntity.ok(Map.of(
        "ok", true,
        "historico", historico));
  }
}