package com.upp.repository;

import com.upp.model.PlanDeEstudios;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanDeEstudiosRepository extends JpaRepository<PlanDeEstudios, Long> {
  boolean existsByCodigoDePlanDeEstudios(String codgioDePlanDeEstudios);

  Optional<PlanDeEstudios> findByCodigoDePlanDeEstudios(String codigo);
}
