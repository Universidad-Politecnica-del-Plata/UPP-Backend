package com.upp.controller;

import com.upp.dto.MateriaDTO;
import com.upp.exception.MateriaExisteException;
import com.upp.exception.MateriaNoExisteException;
import com.upp.service.MateriaService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/materias")
@PreAuthorize("hasRole('GESTION_ACADEMICA')")
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

  @PutMapping("/{codigo}")
  public ResponseEntity<MateriaDTO> modificarMateria(
      @PathVariable String codigo, @RequestBody MateriaDTO materiaDTO) {

    try {
      MateriaDTO resultado = materiaService.modificarMateria(codigo, materiaDTO);
      return ResponseEntity.status(HttpStatus.OK).body(resultado);

    } catch (MateriaNoExisteException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @DeleteMapping("/{codigo}")
  public ResponseEntity<Void> eliminarMateria(@PathVariable String codigo) {
    try {
      materiaService.eliminarMateria(codigo);
      return ResponseEntity.status(HttpStatus.OK).build();

    } catch (MateriaNoExisteException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @GetMapping("/{codigo}")
  public ResponseEntity<MateriaDTO> obtenerMateriaPorCodigo(@PathVariable String codigo) {
    try {
      MateriaDTO materia = materiaService.obtenerMateriaPorCodigo(codigo);
      return ResponseEntity.status(HttpStatus.OK).body(materia);

    } catch (MateriaNoExisteException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @GetMapping
  public ResponseEntity<List<MateriaDTO>> obtenerTodasLasMaterias() {
    List<MateriaDTO> materias = materiaService.obtenerTodasLasMaterias();

    return ResponseEntity.ok(materias);
  }
}
