package com.matemagicos.biblioteca.service;

import com.matemagicos.biblioteca.DTO.DesempenhoRequestDTO;
import com.matemagicos.biblioteca.DTO.DesempenhoResponseDTO;
import com.matemagicos.biblioteca.models.DesempenhoJogo;
import com.matemagicos.biblioteca.models.Jogo;
import com.matemagicos.biblioteca.models.PontuacaoHistorico;
import com.matemagicos.biblioteca.models.Usuario;
import com.matemagicos.biblioteca.repository.DesempenhoJogoRepository;
import com.matemagicos.biblioteca.repository.JogoRepository;
import com.matemagicos.biblioteca.repository.PontuacaoRepository;
import com.matemagicos.biblioteca.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DesempenhoJogoService {

  
  private static final int PONTOS_POR_ACERTO = 10;
  private static final int PONTOS_POR_MOEDA = 10; 

  private final DesempenhoJogoRepository desempenhoRepository;
  private final UsuarioRepository usuarioRepository;
  private final JogoRepository jogoRepository;
  private final PontuacaoRepository pontuacaoRepository;

  public DesempenhoJogoService(DesempenhoJogoRepository desempenhoRepository,
      UsuarioRepository usuarioRepository,
      JogoRepository jogoRepository,
      PontuacaoRepository pontuacaoRepository) {
    this.desempenhoRepository = desempenhoRepository;
    this.usuarioRepository = usuarioRepository;
    this.jogoRepository = jogoRepository;
    this.pontuacaoRepository = pontuacaoRepository;
  }

  public DesempenhoResponseDTO registrar(Integer idUsuario, DesempenhoRequestDTO dto) {
    Usuario usuario = usuarioRepository.findById(idUsuario)
        .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

    Jogo jogo = jogoRepository.findById(dto.getIdJogo())
        .orElseThrow(() -> new IllegalArgumentException("Jogo não encontrado."));

    
    DesempenhoJogo desempenho = new DesempenhoJogo();
    desempenho.setUsuario(usuario);
    desempenho.setJogo(jogo);
    desempenho.setAcertosPartida(dto.getAcertosPartida());
    desempenho.setTempoGasto(dto.getTempoGasto());
    desempenho.setDataHora(LocalDateTime.now());
    desempenho = desempenhoRepository.save(desempenho);

    
    int pontosGanhos = dto.getAcertosPartida() * PONTOS_POR_ACERTO;
    int moedasGanhas = pontosGanhos / PONTOS_POR_MOEDA;

    
    usuario.setTotalPontos(usuario.getTotalPontos() + pontosGanhos);
    usuario.setMoedasMagicas(usuario.getMoedasMagicas() + moedasGanhas);
    usuarioRepository.save(usuario);

    
    if (pontosGanhos > 0) {
      PontuacaoHistorico historico = new PontuacaoHistorico();
      historico.setUsuario(usuario);
      historico.setValorGanho(pontosGanhos);
      historico.setOrigem(jogo.getNomeFase());
      historico.setDataGanho(LocalDateTime.now());
      pontuacaoRepository.save(historico);
    }

    return new DesempenhoResponseDTO(
        desempenho.getIdDesempenho(),
        desempenho.getAcertosPartida(),
        desempenho.getTempoGasto(),
        pontosGanhos,
        usuario.getTotalPontos(),
        usuario.getMoedasMagicas());
  }
}