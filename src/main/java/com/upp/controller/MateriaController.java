package com.upp.controller;

import com.upp.dto.MateriaDTO;
import com.upp.model.Materia;
import com.upp.repository.MateriaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/materias")
public class MateriaController {
    private final MateriaRepository materiaRepository;

    public MateriaController(MateriaRepository materiaRepository) {
        this.materiaRepository = materiaRepository;
    }

    @PostMapping
    public ResponseEntity<MateriaDTO> crearMateria(@RequestBody MateriaDTO materiaDTO) {
        if (materiaRepository.existsByCodigoDeMateria(materiaDTO.getCodigoDeMateria())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Materia materia = new Materia(
                materiaDTO.getCodigoDeMateria(),
                materiaDTO.getNombre(),
                materiaDTO.getContenidos(),
                materiaDTO.getCreditosQueOtorga(),
                materiaDTO.getCreditosNecesarios(),
                materiaDTO.getTipo()
        );
        // Buscar correlativas por código
        if (materiaDTO.getCodigosCorrelativas() != null && !materiaDTO.getCodigosCorrelativas().isEmpty()) {
            List<Materia> correlativas = materiaDTO.getCodigosCorrelativas().stream()
                    .map(cod -> materiaRepository.findByCodigoDeMateria(cod))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());

            materia.setCorrelativas(correlativas);
        }
        materiaRepository.save(materia);

        return ResponseEntity.status(HttpStatus.CREATED).body(materiaDTO);
    }

}
