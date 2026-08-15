package com.matemagicos.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.matemagicos.biblioteca.models.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    
    List<Usuario> findAllByOrderByTotalPontosDesc();
}