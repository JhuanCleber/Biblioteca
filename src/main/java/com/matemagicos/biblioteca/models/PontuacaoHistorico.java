package com.matemagicos.biblioteca.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pontuacao_historico")
public class PontuacaoHistorico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPonto;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    private Integer valorGanho;
    private String origem;
    private LocalDateTime dataGanho;

    public PontuacaoHistorico() {}

    public Integer getIdPonto() {
        return idPonto;
    }

    public void setIdPonto(Integer idPonto) {
        this.idPonto = idPonto;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Integer getValorGanho() {
        return valorGanho;
    }

    public void setValorGanho(Integer valorGanho) {
        this.valorGanho = valorGanho;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public LocalDateTime getDataGanho() {
        return dataGanho;
    }

    public void setDataGanho(LocalDateTime dataGanho) {
        this.dataGanho = dataGanho;
    }
}