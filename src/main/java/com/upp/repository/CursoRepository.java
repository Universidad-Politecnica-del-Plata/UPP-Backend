package com.upp.repository;

import com.upp.model.Curso;
import com.upp.model.Materia;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
  boolean existsByCodigo(String codigo);

  Optional<Curso> findByCodigo(String codigo);

  List<Curso> findByMateria(Materia materia);
}