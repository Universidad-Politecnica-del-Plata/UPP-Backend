package com.upp.service;

import com.upp.dto.MateriaDTO;
import com.upp.exception.MateriaExisteException;
import com.upp.exception.MateriaNoExisteException;
import com.upp.model.Materia;
import com.upp.repository.MateriaRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class MateriaService {
  private final MateriaRepository materiaRepository;

  public MateriaService(MateriaRepository materiaRepository) {
    this.materiaRepository = materiaRepository;
  }

  public MateriaDTO crearMateria(MateriaDTO materiaDTO) {
    if (materiaRepository.existsByCodigoDeMateria(materiaDTO.getCodigoDeMateria())) {
      throw new MateriaExisteException("Ya existe una materia con ese codigo.");
    }
    Materia materia =
        new Materia(
            materiaDTO.getCodigoDeMateria(),
            materiaDTO.getNombre(),
            materiaDTO.getContenidos(),
            materiaDTO.getCreditosQueOtorga(),
            materiaDTO.getCreditosNecesarios(),
            materiaDTO.getTipo());
    // Buscar correlativas por código
    if (materiaDTO.getCodigosCorrelativas() != null
        && !materiaDTO.getCodigosCorrelativas().isEmpty()) {
      List<Materia> correlativas =
          materiaDTO.getCodigosCorrelativas().stream()
              .map(cod -> materiaRepository.findByCodigoDeMateria(cod))
              .filter(Optional::isPresent)
              .map(Optional::get)
              .collect(Collectors.toList());

      materia.setCorrelativas(correlativas);
    }
    materiaRepository.save(materia);

    return materiaDTO;
  }

  public MateriaDTO modificarMateria(String codigo, MateriaDTO materiaDTO) {
    Optional<Materia> materiaOpt = materiaRepository.findByCodigoDeMateria(codigo);

    if (materiaOpt.isEmpty()) {
      throw new MateriaNoExisteException("No existe una materia con ese codigo.");
    }
    Materia materia = materiaOpt.get();
    materia.setNombre(materiaDTO.getNombre());
    materia.setContenidos(materiaDTO.getContenidos());
    materia.setTipo(materiaDTO.getTipo());
    materia.setCreditosQueOtorga(materiaDTO.getCreditosQueOtorga());
    materia.setCreditosNecesarios(materiaDTO.getCreditosNecesarios());

    if (materiaDTO.getCodigosCorrelativas() != null) {
      List<Materia> correlativas =
          materiaDTO.getCodigosCorrelativas().stream()
              .map(materiaRepository::findByCodigoDeMateria)
              .filter(Optional::isPresent)
              .map(Optional::get)
              .collect(Collectors.toList());

      materia.setCorrelativas(correlativas);
    }
    materiaRepository.save(materia);
    return materiaDTO;
  }

  public void eliminarMateria(String codigo) {
    Optional<Materia> materiaOpt = materiaRepository.findByCodigoDeMateria(codigo);

    if (materiaOpt.isEmpty()) {
      throw new MateriaNoExisteException("No existe una materia con ese codigo.");
    }

    Materia materia = materiaOpt.get();
    List<Materia> materiasConCorrelativa =
        materiaRepository.findAll().stream()
            .filter(m -> m.getCorrelativas().contains(materia))
            .collect(Collectors.toList());

    for (Materia m : materiasConCorrelativa) {
      m.getCorrelativas().remove(materia);
      materiaRepository.save(m);
    }
    materiaRepository.delete(materia);
  }

  public MateriaDTO obtenerMateriaPorCodigo(String codigo) {
    Optional<Materia> materiaOpt = materiaRepository.findByCodigoDeMateria(codigo);

    if (materiaOpt.isEmpty()) {
      throw new MateriaNoExisteException("No existe una materia con ese codigo.");
    }

    Materia materia = materiaOpt.get();

    MateriaDTO materiaDTO =
        new MateriaDTO(
            materia.getCodigoDeMateria(),
            materia.getNombre(),
            materia.getContenidos(),
            materia.getCreditosQueOtorga(),
            materia.getCreditosNecesarios(),
            materia.getTipo(),
            materia.getCodigosCorrelativas());

    return materiaDTO;
  }

  public List<MateriaDTO> obtenerTodasLasMaterias() {
    List<MateriaDTO> materias =
        materiaRepository.findAll().stream()
            .map(
                materia ->
                    new MateriaDTO(
                        materia.getCodigoDeMateria(),
                        materia.getNombre(),
                        materia.getContenidos(),
                        materia.getCreditosQueOtorga(),
                        materia.getCreditosNecesarios(),
                        materia.getTipo(),
                        materia.getCodigosCorrelativas()))
            .toList();

    return materias;
  }
}
