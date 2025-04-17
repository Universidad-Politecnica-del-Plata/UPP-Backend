package com.upp.controller;

import com.upp.dto.MateriaDTO;
import com.upp.model.Materia;
import com.upp.repository.MateriaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{codigo}")
    public ResponseEntity<MateriaDTO> modificarMateria(
            @PathVariable String codigo,
            @RequestBody MateriaDTO materiaDTO)
    {
        Optional<Materia> materiaOpt = materiaRepository.findByCodigoDeMateria(codigo);

        if (materiaOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Materia materia = materiaOpt.get();
        materia.setNombre(materiaDTO.getNombre());
        materia.setContenidos(materiaDTO.getContenidos());
        materia.setTipo(materiaDTO.getTipo());
        materia.setCreditosQueOtorga(materiaDTO.getCreditosQueOtorga());
        materia.setCreditosNecesarios(materiaDTO.getCreditosNecesarios());

        if (materiaDTO.getCodigosCorrelativas() != null) {
            List<Materia> correlativas = materiaDTO.getCodigosCorrelativas().stream()
                    .map(materiaRepository::findByCodigoDeMateria)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());

            materia.setCorrelativas(correlativas);
        }
        materiaRepository.save(materia);
        return ResponseEntity.status(HttpStatus.OK).body(materiaDTO);


    }
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> eliminarMateria(@PathVariable String codigo){
        Optional<Materia> materiaOpt = materiaRepository.findByCodigoDeMateria(codigo);

        if (materiaOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Materia materia = materiaOpt.get();
        List<Materia> materiasConCorrelativa = materiaRepository.findAll().stream()
                .filter(m -> m.getCorrelativas().contains(materia))
                .collect(Collectors.toList());

        for (Materia m : materiasConCorrelativa) {
            m.getCorrelativas().remove(materia);
            materiaRepository.save(m);
        }
        materiaRepository.delete(materia);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @GetMapping("/{codigo}")
    public ResponseEntity<MateriaDTO> obtenerMateriaPorCodigo(@PathVariable String codigo) {
        Optional<Materia> materiaOpt = materiaRepository.findByCodigoDeMateria(codigo);

        if (materiaOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Materia materia = materiaOpt.get();

        MateriaDTO materiaDTO = new MateriaDTO(
                materia.getCodigoDeMateria(),
                materia.getNombre(),
                materia.getContenidos(),
                materia.getCreditosQueOtorga(),
                materia.getCreditosNecesarios(),
                materia.getTipo(),
                materia.getCodigosCorrelativas()
        );

        return ResponseEntity.status(HttpStatus.OK).body(materiaDTO);    }

}
