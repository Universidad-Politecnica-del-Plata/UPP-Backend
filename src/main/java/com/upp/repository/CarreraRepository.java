package com.upp.repository;

import com.upp.model.Carrera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarreraRepository extends JpaRepository<Carrera, Long> {
  boolean existsByCodigoDeCarrera(String codigoDeMateria);

  Optional<Carrera> findByCodigoDeCarrera(String cod);
}
