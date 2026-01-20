package com.example.academsoap.repository;

import com.example.academsoap.modelo.Curso;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {

    Optional<Curso> findByCodigo(String codigo);
}
