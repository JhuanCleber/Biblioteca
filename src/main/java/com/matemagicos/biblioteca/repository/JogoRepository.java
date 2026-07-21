package com.matemagicos.biblioteca.repository;

import com.matemagicos.biblioteca.models.Jogo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JogoRepository extends JpaRepository<Jogo, Long> {
}