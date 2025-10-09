package com.upp.repository;

import com.upp.model.Cuatrimestre;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CuatrimestreRepository extends JpaRepository<Cuatrimestre, String> {
  Optional<Cuatrimestre> findByCodigo(String codigo);
  
  List<Cuatrimestre> findByCodigoIn(List<String> codigos);
}