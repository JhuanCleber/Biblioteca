package com.matemagicos.biblioteca.service;

import com.matemagicos.biblioteca.DTO.RankingItemDTO;
import com.matemagicos.biblioteca.DTO.RankingResponseDTO;
import com.matemagicos.biblioteca.models.Usuario;
import com.matemagicos.biblioteca.repository.UsuarioRepository;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RankingService {

  
  private static final int TAMANHO_TOP = 20;

  private final UsuarioRepository usuarioRepository;

  public RankingService(UsuarioRepository usuarioRepository) {
    this.usuarioRepository = usuarioRepository;
  }

  public RankingResponseDTO obterRanking(Integer idUsuarioLogado) {
    List<Usuario> todosOrdenados = usuarioRepository.findAllByOrderByTotalPontosDesc();

    List<RankingItemDTO> top = new ArrayList<>();
    RankingItemDTO suaPosicao = null;

    for (int i = 0; i < todosOrdenados.size(); i++) {
      Usuario usuario = todosOrdenados.get(i);
      int posicao = i + 1;
      boolean ehVoce = usuario.getIdUsuario().equals(idUsuarioLogado);

      RankingItemDTO item = new RankingItemDTO(
          posicao,
          primeiroNome(usuario.getNome()),
          usuario.getTotalPontos(),
          usuario.getMoedasMagicas(),
          ehVoce);

      if (posicao <= TAMANHO_TOP) {
        top.add(item);
      } else if (ehVoce) {
        suaPosicao = item;
      }
    }

    return new RankingResponseDTO(top, suaPosicao);
  }

  
  private String primeiroNome(String nomeCompleto) {
    if (nomeCompleto == null || nomeCompleto.isBlank()) {
      return "Jogador(a)";
    }
    return nomeCompleto.trim().split("\\s+")[0];
  }
}