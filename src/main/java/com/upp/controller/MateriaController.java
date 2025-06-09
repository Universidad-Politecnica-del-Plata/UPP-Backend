package com.upp.controller;

import com.upp.dto.MateriaDTO;
import com.upp.exception.MateriaExisteException;
import com.upp.model.Materia;
import com.upp.service.MateriaService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/materias")
public class MateriaController {
  private final MateriaService materiaService;

  public MateriaController(MateriaService materiaService) {
    this.materiaService = materiaService;
  }

  @PostMapping
  public ResponseEntity<MateriaDTO> crearMateria(@Valid @RequestBody MateriaDTO materiaDTO) {
    try {
      MateriaDTO resultado = materiaService.crearMateria(materiaDTO);

      return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    } catch (MateriaExisteException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }
//
//  @PutMapping("/{codigo}")
//  public ResponseEntity<MateriaDTO> modificarMateria(
//      @PathVariable String codigo, @RequestBody MateriaDTO materiaDTO) {
//    Optional<Materia> materiaOpt = materiaRepository.findByCodigoDeMateria(codigo);
//
//    if (materiaOpt.isEmpty()) {
//      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//    }
//    Materia materia = materiaOpt.get();
//    materia.setNombre(materiaDTO.getNombre());
//    materia.setContenidos(materiaDTO.getContenidos());
//    materia.setTipo(materiaDTO.getTipo());
//    materia.setCreditosQueOtorga(materiaDTO.getCreditosQueOtorga());
//    materia.setCreditosNecesarios(materiaDTO.getCreditosNecesarios());
//
//    if (materiaDTO.getCodigosCorrelativas() != null) {
//      List<Materia> correlativas =
//          materiaDTO.getCodigosCorrelativas().stream()
//              .map(materiaRepository::findByCodigoDeMateria)
//              .filter(Optional::isPresent)
//              .map(Optional::get)
//              .collect(Collectors.toList());
//
//      materia.setCorrelativas(correlativas);
//    }
//    materiaRepository.save(materia);
//    return ResponseEntity.status(HttpStatus.OK).body(materiaDTO);
//  }
//
//  @DeleteMapping("/{codigo}")
//  public ResponseEntity<Void> eliminarMateria(@PathVariable String codigo) {
//    Optional<Materia> materiaOpt = materiaRepository.findByCodigoDeMateria(codigo);
//
//    if (materiaOpt.isEmpty()) {
//      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//    }
//    Materia materia = materiaOpt.get();
//    List<Materia> materiasConCorrelativa =
//        materiaRepository.findAll().stream()
//            .filter(m -> m.getCorrelativas().contains(materia))
//            .collect(Collectors.toList());
//
//    for (Materia m : materiasConCorrelativa) {
//      m.getCorrelativas().remove(materia);
//      materiaRepository.save(m);
//    }
//    materiaRepository.delete(materia);
//    return ResponseEntity.status(HttpStatus.OK).build();
//  }
//
//  @GetMapping("/{codigo}")
//  public ResponseEntity<MateriaDTO> obtenerMateriaPorCodigo(@PathVariable String codigo) {
//    Optional<Materia> materiaOpt = materiaRepository.findByCodigoDeMateria(codigo);
//
//    if (materiaOpt.isEmpty()) {
//      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//    }
//
//    Materia materia = materiaOpt.get();
//
//    MateriaDTO materiaDTO =
//        new MateriaDTO(
//            materia.getCodigoDeMateria(),
//            materia.getNombre(),
//            materia.getContenidos(),
//            materia.getCreditosQueOtorga(),
//            materia.getCreditosNecesarios(),
//            materia.getTipo(),
//            materia.getCodigosCorrelativas());
//
//    return ResponseEntity.status(HttpStatus.OK).body(materiaDTO);
//  }
//
//  @GetMapping
//  public ResponseEntity<List<MateriaDTO>> obtenerTodasLasMaterias() {
//    List<MateriaDTO> materias =
//        materiaRepository.findAll().stream()
//            .map(
//                materia ->
//                    new MateriaDTO(
//                        materia.getCodigoDeMateria(),
//                        materia.getNombre(),
//                        materia.getContenidos(),
//                        materia.getCreditosQueOtorga(),
//                        materia.getCreditosNecesarios(),
//                        materia.getTipo(),
//                        materia.getCodigosCorrelativas()))
//            .toList();
//
//    return ResponseEntity.ok(materias);
//  }
}
