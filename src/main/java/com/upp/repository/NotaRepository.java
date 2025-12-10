package com.upp.repository;

import com.upp.model.Acta;
import com.upp.model.Alumno;
import com.upp.model.Nota;
import com.upp.model.TipoDeActa;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotaRepository extends JpaRepository<Nota, Long> {

  List<Nota> findByActa(Acta acta);

  List<Nota> findByAlumno(Alumno alumno);

  Optional<Nota> findByActaAndAlumno(Acta acta, Alumno alumno);

  boolean existsByActaAndAlumno(Acta acta, Alumno alumno);

  // Métodos para consultar notas específicamente de actas FINAL
  @Query("SELECT n FROM Nota n WHERE n.alumno = :alumno AND n.acta.tipoDeActa = :tipoDeActa")
  List<Nota> findByAlumnoAndActaTipoDeActa(@Param("alumno") Alumno alumno, @Param("tipoDeActa") TipoDeActa tipoDeActa);

  // Verificar si existe una nota aprobada (≥4) en acta FINAL para una materia específica
  @Query("SELECT CASE WHEN COUNT(n) > 0 THEN true ELSE false END " +
         "FROM Nota n " +
         "WHERE n.alumno = :alumno " +
         "AND n.acta.curso.materia.codigoDeMateria = :codigoMateria " +
         "AND n.acta.tipoDeActa = :tipoDeActa " +
         "AND n.valor >= 4")
  boolean existsNotaAprobadaByAlumnoAndMateriaAndActaTipo(
      @Param("alumno") Alumno alumno,
      @Param("codigoMateria") String codigoMateria,
      @Param("tipoDeActa") TipoDeActa tipoDeActa);

  // Obtener materias aprobadas en actas FINAL con sus créditos
  @Query("SELECT n FROM Nota n " +
         "WHERE n.alumno = :alumno " +
         "AND n.acta.tipoDeActa = com.upp.model.TipoDeActa.FINAL " +
         "AND n.valor >= 4")
  List<Nota> findNotasAprobadasEnActasFinalesByAlumno(@Param("alumno") Alumno alumno);
}
