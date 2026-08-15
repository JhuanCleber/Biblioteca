package com.matemagicos.biblioteca.service;

import com.matemagicos.biblioteca.models.EmailVerificationToken;
import com.matemagicos.biblioteca.repository.EmailVerificationTokenRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;

@Service
public class EmailVerificationService {

  private final EmailVerificationTokenRepository repository;
  private final SecureRandom random = new SecureRandom();

  @Value("${email-verification.expiration-ms}")
  private long expirationMs;

  public EmailVerificationService(EmailVerificationTokenRepository repository) {
    this.repository = repository;
  }

  // Mesmo esquema do código de recuperação de senha: 6 dígitos, fácil de digitar
  // no celular
  public String gerarCodigo(Integer idUsuario) {
    String codigo = String.valueOf(100000 + random.nextInt(900000));

    EmailVerificationToken t = new EmailVerificationToken();
    t.setCodigo(codigo);
    t.setIdUsuario(idUsuario);
    t.setDataExpiracao(Instant.now().plusMillis(expirationMs));
    t.setUsado(false);

    repository.save(t);
    return codigo;
  }

  public EmailVerificationToken validarCodigo(Integer idUsuario, String codigo) {
    EmailVerificationToken t = repository
        .findTopByIdUsuarioAndCodigoAndUsadoFalseOrderByIdTokenDesc(idUsuario, codigo)
        .orElseThrow(() -> new IllegalArgumentException("Código inválido ou expirado."));

    if (t.getDataExpiracao().isBefore(Instant.now())) {
      throw new IllegalArgumentException("Código inválido ou expirado.");
    }

    return t;
  }

  public void marcarComoUsado(EmailVerificationToken token) {
    token.setUsado(true);
    repository.save(token);
  }
}