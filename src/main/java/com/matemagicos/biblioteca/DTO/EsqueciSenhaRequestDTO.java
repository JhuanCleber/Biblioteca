package com.matemagicos.biblioteca.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class EsqueciSenhaRequestDTO {

  @NotBlank(message = "O email é obrigatório")
  @Email(message = "Email inválido")
  private String email;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}