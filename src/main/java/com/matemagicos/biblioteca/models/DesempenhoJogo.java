package com.matemagicos.biblioteca.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "desempenho_jogo")
public class DesempenhoJogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDesempenho;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_jogo")
    private Jogo jogo;

    private Integer acertosPartida;
    private Integer tempoGasto;
    private LocalDateTime dataHora;

    public DesempenhoJogo() {
    }

    public Integer getIdDesempenho() {
        return idDesempenho;
    }

    public void setIdDesempenho(Integer idDesempenho) {
        this.idDesempenho = idDesempenho;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Jogo getJogo() {
        return jogo;
    }

    public void setJogo(Jogo jogo) {
        this.jogo = jogo;
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

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}
