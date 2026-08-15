package com.matemagicos.biblioteca.models;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "email_verification_tokens")
public class EmailVerificationToken {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long idToken;

  @Column(nullable = false, length = 6)
  private String codigo;

  @Column(nullable = false)
  private Integer idUsuario;

  @Column(nullable = false)
  private Instant dataExpiracao;

  @Column(nullable = false)
  private boolean usado = false;

  public EmailVerificationToken() {
  }

  public Long getIdToken() {
    return idToken;
  }

  public void setIdToken(Long idToken) {
    this.idToken = idToken;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
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

  public boolean isUsado() {
    return usado;
  }

  public void setUsado(boolean usado) {
    this.usado = usado;
  }
}