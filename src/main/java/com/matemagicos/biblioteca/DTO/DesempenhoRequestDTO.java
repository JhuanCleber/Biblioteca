package com.matemagicos.biblioteca.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class DesempenhoRequestDTO {

  @NotNull(message = "O id do jogo é obrigatório")
  private Integer idJogo;

  @NotNull(message = "A quantidade de acertos é obrigatória")
  @Min(value = 0, message = "Acertos não pode ser negativo")
  private Integer acertosPartida;

  @NotNull(message = "O tempo gasto é obrigatório")
  @Min(value = 0, message = "Tempo gasto não pode ser negativo")
  private Integer tempoGasto; 

  public Integer getIdJogo() {
    return idJogo;
  }

  public void setIdJogo(Integer idJogo) {
    this.idJogo = idJogo;
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
}