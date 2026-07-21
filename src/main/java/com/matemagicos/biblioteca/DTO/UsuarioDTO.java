package com.matemagicos.biblioteca.DTO;

public class UsuarioDTO {

    private Integer id;
    private String username;
    private Integer nivelEscolar;
    private Integer totalPontos;
    private Integer moedasMagicas;

    public UsuarioDTO() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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