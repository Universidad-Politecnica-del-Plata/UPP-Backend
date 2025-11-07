package com.upp.repository;

import com.upp.model.Acta;
import com.upp.model.Alumno;
import com.upp.model.Nota;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotaRepository extends JpaRepository<Nota, Long> {

  List<Nota> findByActa(Acta acta);

  List<Nota> findByAlumno(Alumno alumno);

  Optional<Nota> findByActaAndAlumno(Acta acta, Alumno alumno);

  boolean existsByActaAndAlumno(Acta acta, Alumno alumno);
}
