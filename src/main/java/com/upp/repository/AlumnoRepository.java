package com.upp.repository;

import com.upp.model.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
  boolean existsByDniOrEmail(Long dni, String email);
}
