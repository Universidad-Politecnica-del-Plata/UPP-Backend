package com.upp.repository;

import com.upp.model.Acta;
import com.upp.model.Curso;
import com.upp.model.EstadoActa;
import com.upp.model.TipoDeActa;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActaRepository extends JpaRepository<Acta, Long> {

  List<Acta> findByCurso(Curso curso);

  List<Acta> findByTipoDeActa(TipoDeActa tipoDeActa);

  List<Acta> findByEstado(EstadoActa estado);

  List<Acta> findByCursoAndTipoDeActa(Curso curso, TipoDeActa tipoDeActa);

  boolean existsByCursoAndTipoDeActaAndEstado(Curso curso, TipoDeActa tipoDeActa, EstadoActa estado);
}