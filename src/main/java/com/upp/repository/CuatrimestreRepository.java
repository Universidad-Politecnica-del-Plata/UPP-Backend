package com.upp.repository;

import com.upp.model.Cuatrimestre;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CuatrimestreRepository extends JpaRepository<Cuatrimestre, String> {
  Optional<Cuatrimestre> findByCodigo(String codigo);

  List<Cuatrimestre> findByCodigoIn(List<String> codigos);

  @Query(
      "SELECT c FROM Cuatrimestre c WHERE :fecha >= c.fechaInicioPeriodoDeInscripcion AND :fecha <= c.fechaFinPeriodoIntegradores")
  List<Cuatrimestre> findCuatrimestresByFecha(LocalDate fecha);
}
