package com.matemagicos.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.matemagicos.biblioteca.models.AvaliacaoFinal;

public interface AvaliacaoFinalRepository extends JpaRepository<AvaliacaoFinal, Integer> {

  // Usado ao excluir a conta — a tabela ainda não é populada por nenhuma
  // funcionalidade hoje, mas o FK existe, então precisa limpar mesmo assim
  void deleteByUsuario_IdUsuario(Integer idUsuario);
}