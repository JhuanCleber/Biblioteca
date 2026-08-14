package com.matemagicos.biblioteca.service;

import com.matemagicos.biblioteca.models.RefreshToken;
import com.matemagicos.biblioteca.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class RefreshTokenService {

  private final RefreshTokenRepository repository;

  @Value("${jwt.refresh-expiration-ms}")
  private long refreshExpirationMs;

  public RefreshTokenService(RefreshTokenRepository repository) {
    this.repository = repository;
  }

  // Cria e salva um novo refresh token pro usuário, com validade longa (dias)
  public String gerar(Integer idUsuario) {
    RefreshToken rt = new RefreshToken();
    rt.setToken(UUID.randomUUID().toString());
    rt.setIdUsuario(idUsuario);
    rt.setDataExpiracao(Instant.now().plusMillis(refreshExpirationMs));
    rt.setRevogado(false);

    repository.save(rt);
    return rt.getToken();
  }

  // Confere se o refresh token existe, não foi revogado e ainda não expirou.
  // Mesma mensagem genérica nos três casos: não damos pista pra quem tenta
  // adivinhar tokens.
  public RefreshToken validar(String token) {
    RefreshToken rt = repository.findByToken(token)
        .orElseThrow(() -> new IllegalArgumentException("Sessão expirada. Faça login novamente."));

    if (rt.isRevogado() || rt.getDataExpiracao().isBefore(Instant.now())) {
      throw new IllegalArgumentException("Sessão expirada. Faça login novamente.");
    }

    return rt;
  }

  // Revoga o token antigo e gera um novo (rotação a cada uso: se um refresh token
  // vazar e for usado por outra pessoa, o próximo uso do dono original já
  // invalida a sessão do invasor)
  public String rotacionar(RefreshToken antigo) {
    antigo.setRevogado(true);
    repository.save(antigo);
    return gerar(antigo.getIdUsuario());
  }

  // Usado no logout — revoga sem lançar erro se o token já não existir/for
  // inválido
  public void revogar(String token) {
    repository.findByToken(token).ifPresent(rt -> {
      rt.setRevogado(true);
      repository.save(rt);
    });
  }

  // Usado depois de redefinir a senha — derruba todas as sessões ativas do
  // usuário de uma vez.
  // Faz sentido: se a senha mudou (esqueceu/foi comprometida), ninguém deveria
  // continuar
  // logado em outro aparelho com a sessão antiga.
  public void revogarTodosDoUsuario(Integer idUsuario) {
    List<RefreshToken> tokensAtivos = repository.findAllByIdUsuarioAndRevogadoFalse(idUsuario);
    tokensAtivos.forEach(t -> t.setRevogado(true));
    repository.saveAll(tokensAtivos);
  }
}