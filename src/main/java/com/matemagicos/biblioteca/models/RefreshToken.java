package com.matemagicos.biblioteca.models;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long idRefreshToken;

  @Column(nullable = false, unique = true, length = 512)
  private String token;

  @Column(nullable = false)
  private Integer idUsuario;

  @Column(nullable = false)
  private Instant dataExpiracao;

  @Column(nullable = false)
  private boolean revogado = false;

  public RefreshToken() {
  }

  public Long getIdRefreshToken() {
    return idRefreshToken;
  }

  public void setIdRefreshToken(Long idRefreshToken) {
    this.idRefreshToken = idRefreshToken;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Integer getIdUsuario() {
    return idUsuario;
  }

  public void setIdUsuario(Integer idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Instant getDataExpiracao() {
    return dataExpiracao;
  }

  public void setDataExpiracao(Instant dataExpiracao) {
    this.dataExpiracao = dataExpiracao;
  }

  public boolean isRevogado() {
    return revogado;
  }

  public void setRevogado(boolean revogado) {
    this.revogado = revogado;
  }
}