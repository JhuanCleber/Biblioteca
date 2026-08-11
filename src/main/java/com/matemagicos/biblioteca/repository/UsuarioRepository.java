package com.matemagicos.biblioteca.repository;

import com.matemagicos.biblioteca.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    // Usado pelo ranking: todos os usuários, do maior pontuador pro menor.
    List<Usuario> findAllByOrderByTotalPontosDesc();
}