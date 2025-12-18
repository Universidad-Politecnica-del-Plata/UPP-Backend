package com.upp.repository;

import com.upp.model.Alumno;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
  boolean existsByDniOrEmail(Long dni, String email);

  @Query("SELECT MAX(a.matricula) FROM Alumno a")
  Long findMaxMatricula();

  Optional<Alumno> findByMatricula(Long matricula);

  boolean existsByMatricula(Long matricula);

  List<Alumno> findByHabilitadoTrue();

  Optional<Alumno> findByUsername(String username);

  Optional<Alumno> findByDni(long l);
}
