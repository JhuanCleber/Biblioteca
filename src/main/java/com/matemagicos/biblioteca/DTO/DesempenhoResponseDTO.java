package com.matemagicos.biblioteca.DTO;

public class DesempenhoResponseDTO {

  private Integer idDesempenho;
  private Integer acertosPartida;
  private Integer tempoGasto;
  private Integer pontosGanhos;
  private Integer totalPontosAtualizado;
  private Integer moedasMagicasAtualizado;

  public DesempenhoResponseDTO() {
  }

  public DesempenhoResponseDTO(Integer idDesempenho, Integer acertosPartida, Integer tempoGasto,
      Integer pontosGanhos, Integer totalPontosAtualizado, Integer moedasMagicasAtualizado) {
    this.idDesempenho = idDesempenho;
    this.acertosPartida = acertosPartida;
    this.tempoGasto = tempoGasto;
    this.pontosGanhos = pontosGanhos;
    this.totalPontosAtualizado = totalPontosAtualizado;
    this.moedasMagicasAtualizado = moedasMagicasAtualizado;
  }

  public Integer getIdDesempenho() {
    return idDesempenho;
  }

  public void setIdDesempenho(Integer idDesempenho) {
    this.idDesempenho = idDesempenho;
  }

  public Integer getAcertosPartida() {
    return acertosPartida;
  }

  public void setAcertosPartida(Integer acertosPartida) {
    this.acertosPartida = acertosPartida;
  }

  public Integer getTempoGasto() {
    return tempoGasto;
  }

  public void setTempoGasto(Integer tempoGasto) {
    this.tempoGasto = tempoGasto;
  }

  public Integer getPontosGanhos() {
    return pontosGanhos;
  }

  public void setPontosGanhos(Integer pontosGanhos) {
    this.pontosGanhos = pontosGanhos;
  }

  public Integer getTotalPontosAtualizado() {
    return totalPontosAtualizado;
  }

  public void setTotalPontosAtualizado(Integer totalPontosAtualizado) {
    this.totalPontosAtualizado = totalPontosAtualizado;
  }

  public Integer getMoedasMagicasAtualizado() {
    return moedasMagicasAtualizado;
  }

  public void setMoedasMagicasAtualizado(Integer moedasMagicasAtualizado) {
    this.moedasMagicasAtualizado = moedasMagicasAtualizado;
  }
}