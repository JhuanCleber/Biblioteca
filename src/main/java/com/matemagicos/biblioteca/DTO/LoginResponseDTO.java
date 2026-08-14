package com.matemagicos.biblioteca.DTO;

public class LoginResponseDTO {
    private UsuarioDTO usuario;
    private String mensagem;
    private String token;
    private String refreshToken;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(UsuarioDTO usuario, String mensagem, String token, String refreshToken) {
        this.usuario = usuario;
        this.mensagem = mensagem;
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public UsuarioDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}