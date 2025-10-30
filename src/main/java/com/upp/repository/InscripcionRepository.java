package com.upp.repository;

import com.upp.model.Alumno;
import com.upp.model.Cuatrimestre;
import com.upp.model.Curso;
import com.upp.model.Inscripcion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

  List<Inscripcion> findByAlumno(Alumno alumno);

  boolean existsByAlumnoAndCursoAndCuatrimestre(
      Alumno alumno, Curso curso, Cuatrimestre cuatrimestre);
}
