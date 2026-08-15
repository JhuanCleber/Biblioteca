package com.matemagicos.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.matemagicos.biblioteca.models.RefreshToken;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByToken(String token);

  // Usado depois de redefinir a senha — derruba todas as sessões ativas do
  // usuário
  List<RefreshToken> findAllByIdUsuarioAndRevogadoFalse(Integer idUsuario);
}