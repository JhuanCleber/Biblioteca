package com.matemagicos.biblioteca.DTO;

import jakarta.validation.constraints.*;

public class CadastroRequestDTO {

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    @Size(max = 150, message = "Email muito longo")
    private String email;

    @NotNull(message = "A idade é obrigatória")
    @Min(value = 5, message = "Idade mínima: 5 anos")
    @Max(value = 10, message = "Idade máxima: 10 anos")
    private Integer idade;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 4, max = 100, message = "A senha deve ter entre 4 e 100 caracteres")
    private String senha;

    private Integer nivelEscolar;

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
}
