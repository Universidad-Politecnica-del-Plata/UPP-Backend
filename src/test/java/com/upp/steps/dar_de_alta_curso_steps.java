package com.upp.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.upp.dto.CursoDTO;
import com.upp.model.Rol;
import com.upp.model.Usuario;
import com.upp.repository.RolRepository;
import com.upp.repository.UsuarioRepository;
import com.upp.steps.shared.TokenHolder;
import io.cucumber.java.ast.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class dar_de_alta_curso_steps {
  @Autowired private WebTestClient webTestClient;
  @Autowired private UsuarioRepository usuarioRepository;
  @Autowired private RolRepository rolRepository;
  @Autowired private TokenHolder tokenHolder;

  private FluxExchangeResult<CursoDTO> result;

  @Dado("que hay un gestor de planificacion logueado")
  public void queHayUnGestorDePlanificacionLogueado() {
    Rol rolGestor =
        rolRepository
            .findById("ROLE_GESTOR_DE_PLANIFICACION")
            .orElseThrow(() -> new RuntimeException("ROLE_GESTOR_DE_PLANIFICACION no encontrado"));

    Usuario usuarioExistente = usuarioRepository.findByUsername("admin_planificacion").orElse(null);

    if (usuarioExistente == null) {
      Map<String, Object> registroData =
          Map.of(
              "username", "admin_planificacion",
              "password", "password",
              "roles", List.of("ROLE_GESTOR_DE_PLANIFICACION"));

      webTestClient
          .post()
          .uri("/api/auth/register")
          .contentType(MediaType.APPLICATION_JSON)
          .bodyValue(registroData)
          .exchange()
          .expectStatus()
          .isCreated();
    }

    Map<String, String> loginData = Map.of("username", "admin_planificacion", "password", "password");

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
  }

  @Cuando("se registra un nuevo curso con código {string}, máximo de alumnos {int} y materia {string}")
  public void darDeAltaCurso(String codigo, Integer maximoAlumnos, String codigoMateria) {
    CursoDTO cursoEnviado = new CursoDTO(codigo, maximoAlumnos, codigoMateria);

    this.result =
        webTestClient
            .post()
            .uri("/api/cursos")
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(cursoEnviado)
            .exchange()
            .returnResult(CursoDTO.class);
  }

  @Entonces("se registra el curso {string} exitosamente")
  public void seRegistraElCursoExitosamente(String codigo) {
    var resultGetCurso =
        webTestClient
            .get()
            .uri("/api/cursos/{codigo}", codigo)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(CursoDTO.class);

    assertEquals(HttpStatus.OK, resultGetCurso.getStatus());
  }

  @Entonces("no se registra el curso exitosamente")
  public void noSeRegistraElCursoExitosamente() {
    // Puede ser CONFLICT (409) para código duplicado o BAD_REQUEST (400) para validaciones
    assertTrue(
        result.getStatus() == HttpStatus.CONFLICT || 
        result.getStatus() == HttpStatus.BAD_REQUEST ||
        result.getStatus() == HttpStatus.NOT_FOUND,
        "Expected CONFLICT, BAD_REQUEST or NOT_FOUND but got: " + result.getStatus());
  }

  private void assertTrue(boolean condition, String message) {
    if (!condition) {
      throw new AssertionError(message);
    }
  }
}