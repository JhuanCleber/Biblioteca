package com.matemagicos.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.matemagicos.biblioteca.models.DesempenhoJogo;

import java.util.List;

public interface DesempenhoJogoRepository extends JpaRepository<DesempenhoJogo, Integer> {

  // Últimas 50 partidas do usuário, mais recente primeiro — suficiente pra
  // uma tela de histórico sem precisar de paginação de verdade por enquanto
  List<DesempenhoJogo> findTop50ByUsuario_IdUsuarioOrderByDataHoraDesc(Integer idUsuario);
}