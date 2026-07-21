package com.matemagicos.biblioteca.models;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuario;

    private String username;
    private String senha;
    private Integer nivelEscolar;
    private Integer totalPontos;
    private Integer moedasMagicas;

    public Usuario() {}

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Integer getNivelEscolar() {
        return nivelEscolar;
    }

    public void setNivelEscolar(Integer nivelEscolar) {
        this.nivelEscolar = nivelEscolar;
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
}