package com.matemagicos.biblioteca.DTO;

import java.time.LocalDateTime;

public class HistoricoItemDTO {
  private Integer idDesempenho;
  private Integer idJogo;
  private String nomeFase;
  private String tipoOperacao;
  private Integer acertosPartida;
  private Integer tempoGasto;
  private Integer pontosGanhos;
  private LocalDateTime dataHora;

  public HistoricoItemDTO() {
  }

  public HistoricoItemDTO(Integer idDesempenho, Integer idJogo, String nomeFase, String tipoOperacao,
      Integer acertosPartida, Integer tempoGasto, Integer pontosGanhos, LocalDateTime dataHora) {
    this.idDesempenho = idDesempenho;
    this.idJogo = idJogo;
    this.nomeFase = nomeFase;
    this.tipoOperacao = tipoOperacao;
    this.acertosPartida = acertosPartida;
    this.tempoGasto = tempoGasto;
    this.pontosGanhos = pontosGanhos;
    this.dataHora = dataHora;
  }

  public Integer getIdDesempenho() {
    return idDesempenho;
  }

  public void setIdDesempenho(Integer idDesempenho) {
    this.idDesempenho = idDesempenho;
  }

  public Integer getIdJogo() {
    return idJogo;
  }

  public void setIdJogo(Integer idJogo) {
    this.idJogo = idJogo;
  }

  public String getNomeFase() {
    return nomeFase;
  }

  public void setNomeFase(String nomeFase) {
    this.nomeFase = nomeFase;
  }

  public String getTipoOperacao() {
    return tipoOperacao;
  }

  public void setTipoOperacao(String tipoOperacao) {
    this.tipoOperacao = tipoOperacao;
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

  public LocalDateTime getDataHora() {
    return dataHora;
  }

  public void setDataHora(LocalDateTime dataHora) {
    this.dataHora = dataHora;
  }
}