package com.upp.service;

import com.upp.dto.EstadoAcademicoDTO;
import com.upp.dto.HistoriaAcademicaDTO;
import com.upp.dto.MateriaAprobadaDTO;
import com.upp.model.Alumno;
import com.upp.model.Materia;
import com.upp.model.Nota;
import com.upp.model.TipoDeActa;
import com.upp.repository.NotaRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class HistoriaAcademicaService {

  private final NotaRepository notaRepository;

  public HistoriaAcademicaService(NotaRepository notaRepository) {
    this.notaRepository = notaRepository;
  }

  public Integer calcularCreditosAcumulados(Alumno alumno) {
    List<Nota> notasAprobadas = notaRepository.findNotasAprobadasEnActasFinalesByAlumno(alumno, TipoDeActa.FINAL);

    return notasAprobadas.stream()
        .mapToInt(nota -> nota.getActa().getCurso().getMateria().getCreditosQueOtorga())
        .sum();
  }

  public boolean verificarMateriaAprobada(Alumno alumno, String codigoMateria) {
    return notaRepository.existsNotaAprobadaByAlumnoAndMateriaAndActaTipo(
        alumno, codigoMateria, TipoDeActa.FINAL);
  }

  public List<MateriaAprobadaDTO> obtenerMateriasAprobadas(Alumno alumno) {
    List<Nota> notasAprobadas = notaRepository.findNotasAprobadasEnActasFinalesByAlumno(alumno, TipoDeActa.FINAL);

    return notasAprobadas.stream()
        .map(this::convertirNotaAMateriaAprobadaDTO)
        .collect(Collectors.toList());
  }

  public boolean verificarCorrelativasAprobadas(Alumno alumno, Materia materia) {
    List<Materia> correlativas = materia.getCorrelativas();

    if (correlativas == null || correlativas.isEmpty()) {
      return true;
    }

    for (Materia correlativa : correlativas) {
      if (!verificarMateriaAprobada(alumno, correlativa.getCodigoDeMateria())) {
        return false;
      }
    }

    return true;
  }

  public List<String> obtenerCorrelativasNoAprobadas(Alumno alumno, Materia materia) {
    List<String> correlativasNoAprobadas = new ArrayList<>();
    List<Materia> correlativas = materia.getCorrelativas();

    if (correlativas != null) {
      for (Materia correlativa : correlativas) {
        if (!verificarMateriaAprobada(alumno, correlativa.getCodigoDeMateria())) {
          correlativasNoAprobadas.add(
              correlativa.getCodigoDeMateria() + " - " + correlativa.getNombre());
        }
      }
    }

    return correlativasNoAprobadas;
  }

  public HistoriaAcademicaDTO obtenerHistoriaAcademica(Alumno alumno) {
    List<MateriaAprobadaDTO> materiasAprobadas = obtenerMateriasAprobadas(alumno);
    Integer creditosAcumulados = calcularCreditosAcumulados(alumno);

    return new HistoriaAcademicaDTO(
        alumno.getId(),
        alumno.getNombre() + " " + alumno.getApellido(),
        alumno.getMatricula(),
        creditosAcumulados,
        materiasAprobadas);
  }

  public EstadoAcademicoDTO evaluarEstadoParaInscripcion(Alumno alumno, Materia materia) {
    Integer creditosAcumulados = calcularCreditosAcumulados(alumno);
    Integer creditosNecesarios = materia.getCreditosNecesarios();
    List<String> correlativasNoAprobadas = obtenerCorrelativasNoAprobadas(alumno, materia);

    boolean tieneCreditosSuficientes = creditosAcumulados >= creditosNecesarios;
    boolean tieneCorrelativasAprobadas = correlativasNoAprobadas.isEmpty();

    boolean puedeInscribirse = tieneCreditosSuficientes && tieneCorrelativasAprobadas;
    String motivoNoInscripcion =
        construirMotivoNoInscripcion(
            tieneCreditosSuficientes,
            tieneCorrelativasAprobadas,
            creditosAcumulados,
            creditosNecesarios,
            correlativasNoAprobadas);

    return new EstadoAcademicoDTO(
        alumno.getId(),
        creditosAcumulados,
        creditosNecesarios,
        correlativasNoAprobadas,
        puedeInscribirse,
        motivoNoInscripcion);
  }

  private MateriaAprobadaDTO convertirNotaAMateriaAprobadaDTO(Nota nota) {
    Materia materia = nota.getActa().getCurso().getMateria();

    return new MateriaAprobadaDTO(
        materia.getCodigoDeMateria(),
        materia.getNombre(),
        nota.getValor(),
        materia.getCreditosQueOtorga(),
        nota.getActa().getFechaDeCreacion(),
        nota.getActa().getNumeroCorrelativo());
  }

  private String construirMotivoNoInscripcion(
      boolean tieneCreditosSuficientes,
      boolean tieneCorrelativasAprobadas,
      Integer creditosAcumulados,
      Integer creditosNecesarios,
      List<String> correlativasNoAprobadas) {

    if (tieneCreditosSuficientes && tieneCorrelativasAprobadas) {
      return null;
    }

    List<String> motivos = new ArrayList<>();

    if (!tieneCreditosSuficientes) {
      motivos.add(
          String.format(
              "Créditos insuficientes (tiene %d, necesita %d)",
              creditosAcumulados, creditosNecesarios));
    }

    if (!tieneCorrelativasAprobadas) {
      motivos.add("Correlativas no aprobadas: " + String.join(", ", correlativasNoAprobadas));
    }

    return String.join("; ", motivos);
  }
}
