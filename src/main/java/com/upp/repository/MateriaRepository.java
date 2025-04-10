package com.upp.repository;

import com.upp.model.Materia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MateriaRepository extends JpaRepository<Materia, Long> {
    boolean existsByCodigoDeMateria(String codigoDeMateria);

    Optional<Materia> findByCodigoDeMateria(String cod);
}