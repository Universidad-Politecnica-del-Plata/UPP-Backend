package com.upp.controller;

import com.upp.dto.CuatrimestreDTO;
import com.upp.service.CuatrimestreService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cuatrimestres")
@PreAuthorize("hasRole('GESTOR_DE_PLANIFICACION')")
public class CuatrimestreController {
  private final CuatrimestreService cuatrimestreService;

  public CuatrimestreController(CuatrimestreService cuatrimestreService) {
    this.cuatrimestreService = cuatrimestreService;
  }

  @PostMapping
  public ResponseEntity<?> crearCuatrimestre(@Valid @RequestBody CuatrimestreDTO cuatrimestreDTO) {
    CuatrimestreDTO resultado = cuatrimestreService.crearCuatrimestre(cuatrimestreDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
  }

  @PutMapping("/{codigo}")
  public ResponseEntity<?> modificarCuatrimestre(
      @PathVariable String codigo, @RequestBody CuatrimestreDTO cuatrimestreDTO) {
    CuatrimestreDTO resultado = cuatrimestreService.modificarCuatrimestre(codigo, cuatrimestreDTO);
    return ResponseEntity.status(HttpStatus.OK).body(resultado);
  }

  @DeleteMapping("/{codigo}")
  public ResponseEntity<?> eliminarCuatrimestre(@PathVariable String codigo) {
    cuatrimestreService.eliminarCuatrimestre(codigo);
    return ResponseEntity.status(HttpStatus.OK)
        .body(Map.of("message", "Cuatrimestre eliminado exitosamente"));
  }

  @GetMapping("/{codigo}")
  public ResponseEntity<?> obtenerCuatrimestrePorCodigo(@PathVariable String codigo) {
    CuatrimestreDTO cuatrimestre = cuatrimestreService.obtenerCuatrimestrePorCodigo(codigo);
    return ResponseEntity.status(HttpStatus.OK).body(cuatrimestre);
  }

  @GetMapping
  public ResponseEntity<List<CuatrimestreDTO>> obtenerTodosLosCuatrimestres() {
    List<CuatrimestreDTO> cuatrimestres = cuatrimestreService.obtenerTodosLosCuatrimestres();
    return ResponseEntity.ok(cuatrimestres);
  }
}