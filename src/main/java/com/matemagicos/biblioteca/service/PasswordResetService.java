package com.matemagicos.biblioteca.service;

import com.matemagicos.biblioteca.models.PasswordResetToken;
import com.matemagicos.biblioteca.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;

@Service
public class PasswordResetService {

  private final PasswordResetTokenRepository repository;
  private final SecureRandom random = new SecureRandom();

  @Value("${password-reset.expiration-ms}")
  private long expirationMs;

  public PasswordResetService(PasswordResetTokenRepository repository) {
    this.repository = repository;
  }

  // Gera um código numérico de 6 dígitos (tipo os de SMS/2FA — mais fácil de
  // digitar
  // no celular do que um link longo) e salva com validade curta
  public String gerarCodigo(Integer idUsuario) {
    String codigo = String.valueOf(100000 + random.nextInt(900000));

    PasswordResetToken t = new PasswordResetToken();
    t.setCodigo(codigo);
    t.setIdUsuario(idUsuario);
    t.setDataExpiracao(Instant.now().plusMillis(expirationMs));
    t.setUsado(false);

    repository.save(t);
    return codigo;
  }

  // Mesma mensagem genérica pra código errado, expirado ou já usado —
  // não damos pista de qual é o problema exato
  public PasswordResetToken validarCodigo(Integer idUsuario, String codigo) {
    PasswordResetToken t = repository
        .findTopByIdUsuarioAndCodigoAndUsadoFalseOrderByIdTokenDesc(idUsuario, codigo)
        .orElseThrow(() -> new IllegalArgumentException("Código inválido ou expirado."));

    if (t.getDataExpiracao().isBefore(Instant.now())) {
      throw new IllegalArgumentException("Código inválido ou expirado.");
    }

    return t;
  }

  public void marcarComoUsado(PasswordResetToken token) {
    token.setUsado(true);
    repository.save(token);
  }

  // Usado ao excluir a conta
  public void excluirTodosDoUsuario(Integer idUsuario) {
    repository.deleteByIdUsuario(idUsuario);
  }
}