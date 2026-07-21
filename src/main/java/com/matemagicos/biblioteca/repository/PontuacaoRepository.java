package com.matemagicos.biblioteca.repository;

import com.matemagicos.biblioteca.models.PontuacaoHistorico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PontuacaoRepository extends JpaRepository<PontuacaoHistorico, Long> {
}