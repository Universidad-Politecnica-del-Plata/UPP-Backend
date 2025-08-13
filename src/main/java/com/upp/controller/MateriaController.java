package com.upp.controller;

import com.upp.dto.MateriaDTO;
import com.upp.service.MateriaService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
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
  public ResponseEntity<?> crearMateria(@Valid @RequestBody MateriaDTO materiaDTO) {
    MateriaDTO resultado = materiaService.crearMateria(materiaDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
  }

  @PutMapping("/{codigo}")
  public ResponseEntity<?> modificarMateria(
      @PathVariable String codigo, @RequestBody MateriaDTO materiaDTO) {
    MateriaDTO resultado = materiaService.modificarMateria(codigo, materiaDTO);
    return ResponseEntity.status(HttpStatus.OK).body(resultado);
  }

  @DeleteMapping("/{codigo}")
  public ResponseEntity<?> eliminarMateria(@PathVariable String codigo) {
    materiaService.eliminarMateria(codigo);
    return ResponseEntity.status(HttpStatus.OK)
        .body(Map.of("message", "Materia eliminada exitosamente"));
  }

  @GetMapping("/{codigo}")
  public ResponseEntity<?> obtenerMateriaPorCodigo(@PathVariable String codigo) {
    MateriaDTO materia = materiaService.obtenerMateriaPorCodigo(codigo);
    return ResponseEntity.status(HttpStatus.OK).body(materia);
  }

  @GetMapping
  public ResponseEntity<List<MateriaDTO>> obtenerTodasLasMaterias() {
    List<MateriaDTO> materias = materiaService.obtenerTodasLasMaterias();

    return ResponseEntity.ok(materias);
  }
}
