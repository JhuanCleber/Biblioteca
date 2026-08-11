package com.matemagicos.biblioteca.DTO;

public class RankingItemDTO {

  private int posicao;
  private String nome;
  private Integer totalPontos;
  private Integer moedasMagicas;
  private boolean voce;

  public RankingItemDTO() {
  }

  public RankingItemDTO(int posicao, String nome, Integer totalPontos, Integer moedasMagicas, boolean voce) {
    this.posicao = posicao;
    this.nome = nome;
    this.totalPontos = totalPontos;
    this.moedasMagicas = moedasMagicas;
    this.voce = voce;
  }

  public int getPosicao() {
    return posicao;
  }

  public void setPosicao(int posicao) {
    this.posicao = posicao;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public Integer getTotalPontos() {
    return totalPontos;
  }

  public void setTotalPontos(Integer totalPontos) {
    this.totalPontos = totalPontos;
  }

  public Integer getMoedasMagicas() {
    return moedasMagicas;
  }

  public void setMoedasMagicas(Integer moedasMagicas) {
    this.moedasMagicas = moedasMagicas;
  }

  public boolean isVoce() {
    return voce;
  }

  public void setVoce(boolean voce) {
    this.voce = voce;
  }
}