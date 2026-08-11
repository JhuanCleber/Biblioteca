package com.matemagicos.biblioteca.DTO;

import java.util.List;

public class RankingResponseDTO {

  private List<RankingItemDTO> top;
  // Preenchido só quando o usuário logado NÃO está dentro do "top" acima —
  // assim o front sempre consegue mostrar "sua posição", mesmo fora do topo.
  private RankingItemDTO suaPosicao;

  public RankingResponseDTO() {
  }

  public RankingResponseDTO(List<RankingItemDTO> top, RankingItemDTO suaPosicao) {
    this.top = top;
    this.suaPosicao = suaPosicao;
  }

  public List<RankingItemDTO> getTop() {
    return top;
  }

  public void setTop(List<RankingItemDTO> top) {
    this.top = top;
  }

  public RankingItemDTO getSuaPosicao() {
    return suaPosicao;
  }

  public void setSuaPosicao(RankingItemDTO suaPosicao) {
    this.suaPosicao = suaPosicao;
  }
}