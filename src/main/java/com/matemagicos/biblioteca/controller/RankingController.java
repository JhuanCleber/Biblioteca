package com.matemagicos.biblioteca.controller;

import com.matemagicos.biblioteca.DTO.RankingResponseDTO;
import com.matemagicos.biblioteca.service.RankingService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ranking")
public class RankingController {

  private final RankingService service;

  public RankingController(RankingService service) {
    this.service = service;
  }

  @GetMapping
  public Map<String, Object> obterRanking(Authentication authentication) {
    Integer idUsuario = (Integer) authentication.getDetails();
    RankingResponseDTO resposta = service.obterRanking(idUsuario);

    // HashMap (não Map.of) porque "suaPosicao" pode ser null — quando o
    // usuário logado já está dentro do "top", o front não precisa
    // mostrar um card separado repetindo a mesma informação.
    Map<String, Object> body = new HashMap<>();
    body.put("ok", true);
    body.put("top", resposta.getTop());
    body.put("suaPosicao", resposta.getSuaPosicao());
    return body;
  }
}