package com.matemagicos.biblioteca.DTO;

import jakarta.validation.constraints.*;

public class EditarPerfilRequestDTO {

  @NotBlank(message = "O nome é obrigatório")
  @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
  @Pattern(regexp = "^[\\p{L} '\\-]+$", message = "Use só letras, espaços, hífen ou apóstrofo no nome")
  private String nome;

  @NotNull(message = "A idade é obrigatória")
  @Min(value = 5, message = "Idade mínima: 5 anos")
  @Max(value = 10, message = "Idade máxima: 10 anos")
  private Integer idade;

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public Integer getIdade() {
    return idade;
  }

  public void setIdade(Integer idade) {
    this.idade = idade;
  }
}