package com.upp.repository;

import com.upp.model.PlanDeEstudios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanDeEstudiosRepository extends JpaRepository<PlanDeEstudios, Long> {
    boolean existsByCodigoDePlanDeEstudios(String codgioDePlanDeEstudios);


}
