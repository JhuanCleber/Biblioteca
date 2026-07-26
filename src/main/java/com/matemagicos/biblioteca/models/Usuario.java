package com.matemagicos.biblioteca.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "usuario", uniqueConstraints = {
        @UniqueConstraint(name = "uk_usuario_email", columnNames = "email")
})
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 150)
    private String email;

    @JsonIgnore
    @Column(nullable = false, length = 255)
    private String senha;

    @Column(nullable = false)
    private Integer idade;

    @Column(name = "nivel_escolar")
    private Integer nivelEscolar;

    @Column(name = "total_pontos")
    private Integer totalPontos = 0;

    @Column(name = "moedas_magicas")
    private Integer moedasMagicas = 0;

    public Usuario() {
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
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
