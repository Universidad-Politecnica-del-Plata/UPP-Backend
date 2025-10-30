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

  private static final List<String> ROLES_INICIALES =
      List.of(
          "ROLE_GESTION_ACADEMICA",
          "ROLE_GESTION_ESTUDIANTIL",
          "ROLE_DOCENTE",
          "ROLE_ALUMNO",
          "ROLE_GESTOR_DE_PLANIFICACION");

  @PostConstruct
  public void initRoles() {
    ROLES_INICIALES.forEach(
        nombre -> {
          if (!rolRepository.existsById(nombre)) {
            try {
              rolRepository.save(new Rol(nombre));
            } catch (Exception e) {
              // Si falla por constraint violation, el rol ya existe - esto es normal
              // en entornos de test donde puede haber race conditions
            }
          }
        });
  }
}
