package com.upp.service;

import com.upp.dto.CarreraDTO;
import com.upp.exception.CarreraExisteException;
import com.upp.exception.CarreraNoExisteException;
import com.upp.exception.PlanDeEstudiosNoExisteException;
import com.upp.model.Carrera;
import com.upp.model.PlanDeEstudios;
import com.upp.repository.CarreraRepository;
import com.upp.repository.PlanDeEstudiosRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class CarreraService {
  private final CarreraRepository carreraRepository;
  private final PlanDeEstudiosRepository planDeEstudiosRepository;

  public CarreraService(
      CarreraRepository carreraRepository, PlanDeEstudiosRepository planDeEstudiosRepository) {
    this.carreraRepository = carreraRepository;
    this.planDeEstudiosRepository = planDeEstudiosRepository;
  }

  public CarreraDTO crearCarrera(CarreraDTO carreraDTO) {
    if (carreraRepository.existsByCodigoDeCarrera(carreraDTO.getCodigoDeCarrera())) {
      throw new CarreraExisteException("Ya existe una carrera con ese código.");
    }

    ArrayList<PlanDeEstudios> planesDeEstudio = obtenerPlanesDeEstudio(carreraDTO);

    Carrera carrera =
        new Carrera(
            carreraDTO.getCodigoDeCarrera(),
            carreraDTO.getNombre(),
            carreraDTO.getTitulo(),
            carreraDTO.getIncumbencias(),
            planesDeEstudio);

    carreraRepository.save(carrera);
    return carreraDTO;
  }

  public CarreraDTO obtenerCarreraPorCodigo(String codigo) {
    Optional<Carrera> carreraOpt = carreraRepository.findByCodigoDeCarrera(codigo);

    if (carreraOpt.isEmpty()) {
      throw new CarreraNoExisteException("No existe una carrera con ese código.");
    }

    Carrera carrera = carreraOpt.get();

    return new CarreraDTO(
        carrera.getCodigoDeCarrera(),
        carrera.getNombre(),
        carrera.getTitulo(),
        carrera.getIncumbencias(),
        carrera.getPlanesDeEstudio() != null
            ? carrera.getPlanesDeEstudio().stream()
                .map(PlanDeEstudios::getCodigoDePlanDeEstudios)
                .toList()
            : null);
  }

  public List<CarreraDTO> obtenerTodasLasCarreras() {
    List<CarreraDTO> carreras =
        carreraRepository.findAll().stream()
            .map(
                carrera ->
                    new CarreraDTO(
                        carrera.getCodigoDeCarrera(),
                        carrera.getNombre(),
                        carrera.getTitulo(),
                        carrera.getIncumbencias(),
                        carrera.getPlanesDeEstudio() != null
                            ? carrera.getPlanesDeEstudio().stream()
                                .map(PlanDeEstudios::getCodigoDePlanDeEstudios)
                                .toList()
                            : null))
            .toList();

    return carreras;
  }

  private ArrayList<PlanDeEstudios> obtenerPlanesDeEstudio(CarreraDTO carreraDTO) {
    ArrayList<PlanDeEstudios> planesDeEstudio = new ArrayList<>();
    if (carreraDTO.getCodigosPlanesDeEstudio() != null) {
      for (String codigoPlan : carreraDTO.getCodigosPlanesDeEstudio()) {
        PlanDeEstudios planDeEstudios =
            planDeEstudiosRepository
                .findByCodigoDePlanDeEstudios(codigoPlan)
                .orElseThrow(
                    () ->
                        new PlanDeEstudiosNoExisteException(
                            "No se encontró el plan de estudios con código: " + codigoPlan));
        planesDeEstudio.add(planDeEstudios);
      }
    }
    return planesDeEstudio;
  }
}
