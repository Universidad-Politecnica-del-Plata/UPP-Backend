package com.upp.controller;

import com.upp.dto.PlanDeEstudiosResponseDTO;
import com.upp.dto.PlanDeEstudiosRequestDTO;
import com.upp.exception.MateriaNoExisteException;
import com.upp.exception.PlanDeEstudiosExisteException;
import com.upp.service.PlanDeEstudiosService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/planDeEstudios")
@PreAuthorize("hasRole('GESTION_ACADEMICA')")
public class PlanDeEstudiosController {
    private final PlanDeEstudiosService planDeEstudiosService;

    public PlanDeEstudiosController(PlanDeEstudiosService planDeEstudiosService) {
        this.planDeEstudiosService = planDeEstudiosService;
    }

    @PostMapping
    public ResponseEntity<PlanDeEstudiosResponseDTO> crearPlanDeEstidios(@Valid @RequestBody PlanDeEstudiosRequestDTO planDeEstudiosRequestDTO) {
        try {
            PlanDeEstudiosResponseDTO resultado = planDeEstudiosService.crearPlanDeEstudios(planDeEstudiosRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(resultado);

        } catch (MateriaNoExisteException | PlanDeEstudiosExisteException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
