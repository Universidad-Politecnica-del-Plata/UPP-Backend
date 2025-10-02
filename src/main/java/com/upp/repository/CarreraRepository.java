package com.upp.repository;

import com.upp.model.Carrera;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarreraRepository extends JpaRepository<Carrera, Long> {
  boolean existsByCodigoDeCarrera(String codigoDeMateria);

  Optional<Carrera> findByCodigoDeCarrera(String cod);
}
