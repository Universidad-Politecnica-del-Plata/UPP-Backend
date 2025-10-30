package com.upp.steps;

import static org.junit.jupiter.api.Assertions.*;

import com.upp.dto.CuatrimestreDTO;
import com.upp.dto.CursoDTO;
import com.upp.model.Rol;
import com.upp.model.Usuario;
import com.upp.repository.CursoRepository;
import com.upp.repository.RolRepository;
import com.upp.repository.UsuarioRepository;
import com.upp.steps.shared.TokenHolder;
import io.cucumber.java.Before;
import io.cucumber.java.ast.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
  @Autowired private CursoRepository cursoRepository;

  private FluxExchangeResult<CursoDTO> result;

  @Before
  public void limpiarCursos() {
    // Limpiar cursos antes de cada escenario para evitar interferencias
    cursoRepository.deleteAll();
  }

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

    Map<String, String> loginData =
        Map.of("username", "admin_planificacion", "password", "password");

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

  @Dado("que existe un cuatrimestre con código {string}")
  public void queExisteUnCuatrimestreConCodigo(String codigo) {
    CuatrimestreDTO cuatrimestreEnviado =
        new CuatrimestreDTO(
            codigo,
            LocalDate.of(2024, 3, 1),
            LocalDate.of(2024, 7, 15),
            LocalDate.of(2024, 2, 1),
            LocalDate.of(2024, 2, 28),
            LocalDate.of(2024, 7, 16),
            LocalDate.of(2024, 7, 31));

    webTestClient
        .post()
        .uri("/api/cuatrimestres")
        .header("Authorization", "Bearer " + tokenHolder.getToken())
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(cuatrimestreEnviado)
        .exchange();
  }

  @Cuando(
      "se registra un nuevo curso con código {string}, máximo de alumnos {int} y materia {string}")
  public void darDeAltaCurso(String codigo, Integer maximoAlumnos, String codigoMateria) {

    CursoDTO cursoEnviado = new CursoDTO(codigo, maximoAlumnos, codigoMateria, null);

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

  @Cuando(
      "se registra un nuevo curso con código {string}, máximo de alumnos {int}, materia {string} y cuatrimestres {string}")
  public void darDeAltaCursoConCuatrimestres(
      String codigo, Integer maximoAlumnos, String codigoMateria, String cuatrimestres) {
    List<String> codigosCuatrimestres = null;
    if (cuatrimestres != null && !cuatrimestres.trim().isEmpty()) {
      codigosCuatrimestres =
          Arrays.stream(cuatrimestres.split(",")).map(String::trim).collect(Collectors.toList());
    }

    CursoDTO cursoEnviado =
        new CursoDTO(codigo, maximoAlumnos, codigoMateria, codigosCuatrimestres);

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

  @Entonces("el curso {string} está asignado al cuatrimestre {string}")
  public void elCursoEstaAsignadoAlCuatrimestre(String codigoCurso, String codigoCuatrimestre) {
    var resultGetCurso =
        webTestClient
            .get()
            .uri("/api/cursos/{codigo}", codigoCurso)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(CursoDTO.class);

    assertEquals(HttpStatus.OK, resultGetCurso.getStatus());

    CursoDTO curso = resultGetCurso.getResponseBody().blockFirst();
    assertTrue(
        curso.getCodigosCuatrimestres() != null
            && curso.getCodigosCuatrimestres().contains(codigoCuatrimestre),
        "El curso no está asignado al cuatrimestre esperado");
  }

  @Entonces("el curso {string} está asignado a los cuatrimestres {string}")
  public void elCursoEstaAsignadoALosCuatrimestres(String codigoCurso, String cuatrimestres) {
    var resultGetCurso =
        webTestClient
            .get()
            .uri("/api/cursos/{codigo}", codigoCurso)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(CursoDTO.class);

    assertEquals(HttpStatus.OK, resultGetCurso.getStatus());

    CursoDTO curso = resultGetCurso.getResponseBody().blockFirst();
    List<String> cuatrimestresList =
        Arrays.stream(cuatrimestres.split(",")).map(String::trim).collect(Collectors.toList());

    assertTrue(
        curso.getCodigosCuatrimestres() != null
            && curso.getCodigosCuatrimestres().containsAll(cuatrimestresList),
        "El curso no está asignado a todos los cuatrimestres esperados");
  }

  @Entonces("no se registra el curso exitosamente")
  public void noSeRegistraElCursoExitosamente() {
    // Puede ser CONFLICT (409) para código duplicado o BAD_REQUEST (400) para validaciones
    assertNotEquals(HttpStatus.OK, result.getStatus());
  }
}
