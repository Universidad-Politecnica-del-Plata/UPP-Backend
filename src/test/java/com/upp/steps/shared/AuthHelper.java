package com.upp.steps.shared;

import com.upp.repository.RolRepository;
import com.upp.repository.UsuarioRepository;
import io.cucumber.spring.ScenarioScope;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Clase helper para manejar la autenticación en los tests de Cucumber.
 * Asegura que un usuario con un rol específico esté logueado antes de ejecutar
 * operaciones que requieran autenticación.
 */
@Component
@ScenarioScope
public class AuthHelper {

  @Autowired private WebTestClient webTestClient;
  @Autowired private UsuarioRepository usuarioRepository;
  @Autowired private RolRepository rolRepository;
  @Autowired private TokenHolder tokenHolder;

  // Enum para los roles disponibles en el sistema
  public enum RolUsuario {
    GESTOR_ACADEMICO("admin_gestion", "ROLE_GESTION_ACADEMICA"),
    GESTOR_PLANIFICACION("admin_planificacion", "ROLE_GESTOR_DE_PLANIFICACION"),
    GESTOR_ESTUDIANTIL("admin_gestion_estudiantil", "ROLE_GESTION_ESTUDIANTIL"),
    DOCENTE("docente_test", "ROLE_DOCENTE");

    private final String username;
    private final String roleName;

    RolUsuario(String username, String roleName) {
      this.username = username;
      this.roleName = roleName;
    }

    public String getUsername() {
      return username;
    }

    public String getRoleName() {
      return roleName;
    }
  }

  private RolUsuario currentRole = null;

  /**
   * Asegura que haya un usuario con el rol especificado logueado. Si ya hay un usuario con ese rol
   * logueado, no hace nada.
   */
  public void login(RolUsuario rol) {
    if (currentRole == rol && tokenHolder.getToken() != null) {
      return; // Ya está logueado con este rol
    }

    // Verificar que el rol existe en la base de datos
    rolRepository
        .findById(rol.getRoleName())
        .orElseThrow(() -> new RuntimeException(rol.getRoleName() + " no encontrado"));

    // Crear usuario si no existe
    if (usuarioRepository.findByUsername(rol.getUsername()).isEmpty()) {
      Map<String, Object> registroData =
          Map.of(
              "username", rol.getUsername(),
              "password", "password",
              "roles", List.of(rol.getRoleName()));

      webTestClient
          .post()
          .uri("/api/auth/register")
          .contentType(MediaType.APPLICATION_JSON)
          .bodyValue(registroData)
          .exchange()
          .expectStatus()
          .isCreated();
    }

    // Login
    Map<String, String> loginData = Map.of("username", rol.getUsername(), "password", "password");

    String token =
        webTestClient
            .post()
            .uri("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(loginData)
            .exchange()
            .expectStatus()
            .isOk()
            .returnResult(Map.class)
            .getResponseBody()
            .blockFirst()
            .get("token")
            .toString();

    tokenHolder.setToken(token);
    currentRole = rol;
  }

  /** Login específico para alumno */
  public void loginAlumno(String username, String password) {
    Map<String, String> loginData = Map.of("username", username, "password", password);

    String token =
        webTestClient
            .post()
            .uri("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(loginData)
            .exchange()
            .expectStatus()
            .isOk()
            .returnResult(Map.class)
            .getResponseBody()
            .blockFirst()
            .get("token")
            .toString();

    tokenHolder.setToken(token);
    currentRole = null; 
  }



  /** Asegura que haya un gestor académico logueado. */
  public void loginGestorAcademico() {
    login(RolUsuario.GESTOR_ACADEMICO);
  }

  /** Asegura que haya un gestor de planificación logueado. */
  public void loginGestorPlanificacion() {
    login(RolUsuario.GESTOR_PLANIFICACION);
  }

  /** Asegura que haya un gestor estudiantil logueado. */
  public void loginGestorEstudiantil() {
    login(RolUsuario.GESTOR_ESTUDIANTIL);
  }

  /** Asegura que haya un docente logueado. */
  public void loginDocente() {
    login(RolUsuario.DOCENTE);
  }
}
