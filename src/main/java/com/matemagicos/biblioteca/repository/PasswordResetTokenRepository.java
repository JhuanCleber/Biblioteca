package com.matemagicos.biblioteca.repository;

import com.matemagicos.biblioteca.models.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

  // Pega o código mais recente não usado que bate com usuário + código digitado.
  // "Mais recente" importa: se o usuário pedir o código de novo, o antigo
  // continua
  // no banco (não usado) até expirar — não queremos validar um código velho por
  // engano.
  Optional<PasswordResetToken> findTopByIdUsuarioAndCodigoAndUsadoFalseOrderByIdTokenDesc(
      Integer idUsuario, String codigo);

  // Usado ao excluir a conta
  void deleteByIdUsuario(Integer idUsuario);
}