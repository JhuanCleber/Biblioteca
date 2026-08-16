package com.matemagicos.biblioteca.repository;

import com.matemagicos.biblioteca.models.DesempenhoJogo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DesempenhoJogoRepository extends JpaRepository<DesempenhoJogo, Integer> {

  // Últimas 50 partidas do usuário, mais recente primeiro — suficiente pra
  // uma tela de histórico sem precisar de paginação de verdade por enquanto
  List<DesempenhoJogo> findTop50ByUsuario_IdUsuarioOrderByDataHoraDesc(Integer idUsuario);

  // Usado ao excluir a conta — precisa apagar as partidas antes do usuário
  // por causa da chave estrangeira
  void deleteByUsuario_IdUsuario(Integer idUsuario);
}