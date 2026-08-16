package com.matemagicos.biblioteca.repository;

import com.matemagicos.biblioteca.models.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {

  Optional<EmailVerificationToken> findTopByIdUsuarioAndCodigoAndUsadoFalseOrderByIdTokenDesc(
      Integer idUsuario, String codigo);

  // Usado ao excluir a conta
  void deleteByIdUsuario(Integer idUsuario);
}