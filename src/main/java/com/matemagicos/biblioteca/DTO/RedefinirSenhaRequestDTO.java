package com.matemagicos.biblioteca.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RedefinirSenhaRequestDTO {

  @NotBlank(message = "O email é obrigatório")
  @Email(message = "Email inválido")
  private String email;

  @NotBlank(message = "O código é obrigatório")
  private String codigo;

  @NotBlank(message = "A nova senha é obrigatória")
  @Size(min = 4, message = "A senha precisa ter pelo menos 4 caracteres")
  private String novaSenha;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getNovaSenha() {
    return novaSenha;
  }

  public void setNovaSenha(String novaSenha) {
    this.novaSenha = novaSenha;
  }
}