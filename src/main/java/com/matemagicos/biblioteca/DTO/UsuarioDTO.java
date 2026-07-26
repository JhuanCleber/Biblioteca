package com.matemagicos.biblioteca.DTO;

public class UsuarioDTO {
    private Integer id;
    private String nome;
    private String email;
    private Integer idade;
    private Integer nivelEscolar;
    private Integer totalPontos;
    private Integer moedasMagicas;

    public UsuarioDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
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
