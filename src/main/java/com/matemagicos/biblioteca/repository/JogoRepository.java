package com.matemagicos.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.matemagicos.biblioteca.models.Jogo;

public interface JogoRepository extends JpaRepository<Jogo, Integer> {
}
