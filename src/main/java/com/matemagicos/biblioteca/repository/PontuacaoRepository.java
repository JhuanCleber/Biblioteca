package com.matemagicos.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.matemagicos.biblioteca.models.PontuacaoHistorico;

public interface PontuacaoRepository extends JpaRepository<PontuacaoHistorico, Integer> {
}
