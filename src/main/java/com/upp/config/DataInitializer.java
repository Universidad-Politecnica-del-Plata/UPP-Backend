package com.upp.config;

import com.upp.model.Rol;
import com.upp.repository.RolRepository;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final RolRepository rolRepository;

    private static final List<String> ROLES_INICIALES = List.of(
            "GESTION_ACADEMICA",
            "GESTION_ESTUDIANTIL",
            "DOCENTE",
            "ESTUDIANTE"
    );

    @PostConstruct
    public void initRoles() {
        ROLES_INICIALES.forEach(nombre -> {
            if (!rolRepository.existsById(nombre)) {
                rolRepository.save(new Rol(nombre));
            }
        });
    }
}
