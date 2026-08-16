package com.matemagicos.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.matemagicos.biblioteca.models.PontuacaoHistorico;

public interface PontuacaoRepository extends JpaRepository<PontuacaoHistorico, Integer> {

  // Usado ao excluir a conta — precisa apagar o histórico de pontos antes
  // do usuário por causa da chave estrangeira
  void deleteByUsuario_IdUsuario(Integer idUsuario);
}