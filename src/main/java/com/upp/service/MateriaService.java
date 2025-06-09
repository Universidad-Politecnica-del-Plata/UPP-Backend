package com.upp.service;

import com.upp.dto.MateriaDTO;
import com.upp.exception.MateriaExisteException;
import com.upp.model.Materia;
import com.upp.repository.MateriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
}
