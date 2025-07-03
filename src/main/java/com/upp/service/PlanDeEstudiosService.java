package com.upp.service;

import com.upp.dto.PlanDeEstudiosRequestDTO;
import com.upp.dto.PlanDeEstudiosResponseDTO;
import com.upp.exception.MateriaExisteException;
import com.upp.exception.MateriaNoExisteException;
import com.upp.exception.PlanDeEstudiosExisteException;
import com.upp.model.Materia;
import com.upp.model.PlanDeEstudios;
import com.upp.repository.MateriaRepository;
import com.upp.repository.PlanDeEstudiosRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PlanDeEstudiosService {
    private final PlanDeEstudiosRepository planDeEstudiosRepository;
    private final MateriaRepository materiaRepository;
    public PlanDeEstudiosService(PlanDeEstudiosRepository planDeEstudiosRepository, MateriaRepository materiaRepository) {
        this.planDeEstudiosRepository = planDeEstudiosRepository;
        this.materiaRepository = materiaRepository;
    }
    public PlanDeEstudiosResponseDTO crearPlanDeEstudios(PlanDeEstudiosRequestDTO planDeEstudiosRequestDTO) {
        if (planDeEstudiosRepository.existsByCodigoDePlanDeEstudios(planDeEstudiosRequestDTO.getCodigoDePlanDeEstudios())) {
            throw new PlanDeEstudiosExisteException("Ya existe un plan de estudios con ese codigo.");
        }
        ArrayList<Materia> materias = new ArrayList<>();
        for (String codigoMateria : planDeEstudiosRequestDTO.getCodigosMaterias()) {
            Materia materia = materiaRepository.findByCodigoDeMateria(codigoMateria)
                    .orElseThrow(() -> new MateriaNoExisteException("No se encontró la materia con código: " + codigoMateria));
            materias.add(materia);
        }
        List<String> codigosMaterias = materias.stream()
                .map(Materia::getCodigoDeMateria)
                .toList();
        PlanDeEstudios planDeEstudios = new PlanDeEstudios(planDeEstudiosRequestDTO.getCodigoDePlanDeEstudios(),planDeEstudiosRequestDTO.getCreditosElectivos(),planDeEstudiosRequestDTO.getFechaEntradaEnVigencia(),materias,planDeEstudiosRequestDTO.getFechaVencimiento());
        planDeEstudiosRepository.save(planDeEstudios);
        return new PlanDeEstudiosResponseDTO(planDeEstudios.getCodigoDePlanDeEstudios(),planDeEstudios.getCreditosElectivos(),planDeEstudios.getFechaEntradaEnVigencia(),planDeEstudios.getFechaVencimiento(),codigosMaterias,planDeEstudios.getCreditosObligatorios());}
}
