package com.upp.repository;

import com.upp.model.Materia;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MateriaRepository extends JpaRepository<Materia, Long> {
  boolean existsByCodigoDeMateria(String codigoDeMateria);

  Optional<Materia> findByCodigoDeMateria(String cod);

  List<Materia> findByCodigoDeMateria_In(List<String> codigosMaterias);
}
