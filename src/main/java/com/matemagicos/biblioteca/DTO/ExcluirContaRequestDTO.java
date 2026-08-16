package com.matemagicos.biblioteca.DTO;

import jakarta.validation.constraints.NotBlank;

public class ExcluirContaRequestDTO {

  @NotBlank(message = "A senha é obrigatória")
  private String senha;

  public String getSenha() {
    return senha;
  }

  public void setSenha(String senha) {
    this.senha = senha;
  }
}