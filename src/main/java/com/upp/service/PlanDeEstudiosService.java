package com.upp.service;

import com.upp.dto.PlanDeEstudiosRequestDTO;
import com.upp.dto.PlanDeEstudiosResponseDTO;
import com.upp.exception.MateriaNoExisteException;
import com.upp.exception.PlanDeEstudiosExisteException;
import com.upp.exception.PlanDeEstudiosNoExisteException;
import com.upp.model.Materia;
import com.upp.model.PlanDeEstudios;
import com.upp.repository.MateriaRepository;
import com.upp.repository.PlanDeEstudiosRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class PlanDeEstudiosService {
  private final PlanDeEstudiosRepository planDeEstudiosRepository;
  private final MateriaRepository materiaRepository;

  public PlanDeEstudiosService(
      PlanDeEstudiosRepository planDeEstudiosRepository, MateriaRepository materiaRepository) {
    this.planDeEstudiosRepository = planDeEstudiosRepository;
    this.materiaRepository = materiaRepository;
  }

  public PlanDeEstudiosResponseDTO crearPlanDeEstudios(
      PlanDeEstudiosRequestDTO planDeEstudiosRequestDTO) {
    if (planDeEstudiosRepository.existsByCodigoDePlanDeEstudios(
        planDeEstudiosRequestDTO.getCodigoDePlanDeEstudios())) {
      throw new PlanDeEstudiosExisteException("Ya existe un plan de estudios con ese codigo.");
    }
    ArrayList<Materia> materias = obtenerMaterias(planDeEstudiosRequestDTO);
    List<String> codigosMaterias = materias.stream().map(Materia::getCodigoDeMateria).toList();
    PlanDeEstudios planDeEstudios =
        new PlanDeEstudios(
            planDeEstudiosRequestDTO.getCodigoDePlanDeEstudios(),
            planDeEstudiosRequestDTO.getCreditosElectivos(),
            planDeEstudiosRequestDTO.getFechaEntradaEnVigencia(),
            materias,
            planDeEstudiosRequestDTO.getFechaVencimiento());
    planDeEstudiosRepository.save(planDeEstudios);
    return new PlanDeEstudiosResponseDTO(
        planDeEstudios.getCodigoDePlanDeEstudios(),
        planDeEstudios.getCreditosElectivos(),
        planDeEstudios.getFechaEntradaEnVigencia(),
        planDeEstudios.getFechaVencimiento(),
        codigosMaterias,
        planDeEstudios.getCreditosObligatorios(),
        planDeEstudios.getCarrera() != null
            ? planDeEstudios.getCarrera().getCodigoDeCarrera()
            : null);
  }

  public PlanDeEstudiosResponseDTO obtenerPlanDeEstudiosPorCodigo(String codigo) {
    Optional<PlanDeEstudios> planDeEstudiosOpt =
        planDeEstudiosRepository.findByCodigoDePlanDeEstudios(codigo);

    if (planDeEstudiosOpt.isEmpty()) {
      throw new PlanDeEstudiosNoExisteException("No existe un plan de estudios con ese codigo.");
    }

    PlanDeEstudios planDeEstudios = planDeEstudiosOpt.get();
    List<String> codigosMaterias = planDeEstudios.getCodigosMaterias();

    return new PlanDeEstudiosResponseDTO(
        planDeEstudios.getCodigoDePlanDeEstudios(),
        planDeEstudios.getCreditosElectivos(),
        planDeEstudios.getFechaEntradaEnVigencia(),
        planDeEstudios.getFechaVencimiento(),
        codigosMaterias,
        planDeEstudios.getCreditosObligatorios(),
        planDeEstudios.getCarrera() != null
            ? planDeEstudios.getCarrera().getCodigoDeCarrera()
            : null);
  }

  private ArrayList<Materia> obtenerMaterias(PlanDeEstudiosRequestDTO planDeEstudiosRequestDTO) {
    ArrayList<Materia> materias = new ArrayList<>();
    for (String codigoMateria : planDeEstudiosRequestDTO.getCodigosMaterias()) {
      Materia materia =
          materiaRepository
              .findByCodigoDeMateria(codigoMateria)
              .orElseThrow(
                  () ->
                      new MateriaNoExisteException(
                          "No se encontró la materia con código: " + codigoMateria));
      materias.add(materia);
    }
    return materias;
  }

  public List<PlanDeEstudiosResponseDTO> obtenerTodosLosPlanesDeEstudios() {
    List<PlanDeEstudiosResponseDTO> planesDeEstudios =
        planDeEstudiosRepository.findAll().stream()
            .map(
                planDeEstudios ->
                    new PlanDeEstudiosResponseDTO(
                        planDeEstudios.getCodigoDePlanDeEstudios(),
                        planDeEstudios.getCreditosElectivos(),
                        planDeEstudios.getFechaEntradaEnVigencia(),
                        planDeEstudios.getFechaVencimiento(),
                        planDeEstudios.getCodigosMaterias(),
                        planDeEstudios.getCreditosObligatorios(),
                        planDeEstudios.getCarrera() != null
                            ? planDeEstudios.getCarrera().getCodigoDeCarrera()
                            : null))
            .toList();

    return planesDeEstudios;
  }

  public PlanDeEstudiosResponseDTO modificarPlanDeEstudios(
      String codigo, PlanDeEstudiosRequestDTO planDeEstudiosRequestDTO) {
    Optional<PlanDeEstudios> planDeEstudiosOpt =
        planDeEstudiosRepository.findByCodigoDePlanDeEstudios(codigo);

    if (planDeEstudiosOpt.isEmpty()) {
      throw new PlanDeEstudiosNoExisteException("No existe un plan de estudios con ese codigo.");
    }

    PlanDeEstudios planDeEstudios = planDeEstudiosOpt.get();

    ArrayList<Materia> materias = obtenerMaterias(planDeEstudiosRequestDTO);

    planDeEstudios.setCreditosElectivos(planDeEstudiosRequestDTO.getCreditosElectivos());
    planDeEstudios.setFechaEntradaEnVigencia(planDeEstudiosRequestDTO.getFechaEntradaEnVigencia());
    planDeEstudios.setFechaVencimiento(planDeEstudiosRequestDTO.getFechaVencimiento());
    planDeEstudios.setMaterias(materias);

    planDeEstudiosRepository.save(planDeEstudios);

    List<String> codigosMaterias = materias.stream().map(Materia::getCodigoDeMateria).toList();

    return new PlanDeEstudiosResponseDTO(
        planDeEstudios.getCodigoDePlanDeEstudios(),
        planDeEstudios.getCreditosElectivos(),
        planDeEstudios.getFechaEntradaEnVigencia(),
        planDeEstudios.getFechaVencimiento(),
        codigosMaterias,
        planDeEstudios.getCreditosObligatorios(),
        planDeEstudios.getCarrera() != null
            ? planDeEstudios.getCarrera().getCodigoDeCarrera()
            : null);
  }

  public void eliminarPlanDeEstudios(String codigo) {
    Optional<PlanDeEstudios> planDeEstudiosOpt =
        planDeEstudiosRepository.findByCodigoDePlanDeEstudios(codigo);

    if (planDeEstudiosOpt.isEmpty()) {
      throw new PlanDeEstudiosNoExisteException("No existe un plan de estudios con ese codigo.");
    }

    PlanDeEstudios planDeEstudios = planDeEstudiosOpt.get();
    planDeEstudiosRepository.delete(planDeEstudios);
    //    TODO: Cuando se implemente Carrera, cuando se elimine plan de estudios eliminarlo tambien
    // de carrera
  }
}
