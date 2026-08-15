package com.matemagicos.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.matemagicos.biblioteca.models.EmailVerificationToken;

import java.util.Optional;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {

  Optional<EmailVerificationToken> findTopByIdUsuarioAndCodigoAndUsadoFalseOrderByIdTokenDesc(
      Integer idUsuario, String codigo);
}