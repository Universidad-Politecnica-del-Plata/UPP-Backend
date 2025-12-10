package com.upp.steps;

import static org.junit.jupiter.api.Assertions.*;

import com.upp.dto.ActaRequestDTO;
import com.upp.dto.InscripcionDTO;
import com.upp.dto.MateriaDTO;
import com.upp.dto.NotaRequestDTO;
import com.upp.model.Alumno;
import com.upp.model.EstadoActa;
import com.upp.model.TipoDeActa;
import com.upp.model.TipoMateria;
import com.upp.repository.*;
import com.upp.steps.shared.TokenHolder;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class inscribirse_a_un_curso_steps {

  @Autowired private WebTestClient webTestClient;
  @Autowired private TokenHolder tokenHolder;
  @Autowired private AlumnoRepository alumnoRepository;
  private FluxExchangeResult<InscripcionDTO> inscripcionResult;
  private FluxExchangeResult<List> consultaResult;
  private FluxExchangeResult<Map> eliminarResult;
  private Long codigoInscripcionGuardado;

  @Dado("que hay un alumno logueado con username {string}, password {string}")
  public void crearAlumnoLogueado(String username, String password) {
    // Login del alumno para obtener token
    Map<String, String> loginData =
        Map.of(
            "username", username,
            "password", password);

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

  @Cuando("el alumno se inscribe al curso {string} en el cuatrimestre actual")
  public void elAlumnoSeInscribeAlCurso(String codigoCurso) {

    Map<String, String> inscripcionData = Map.of("codigoCurso", codigoCurso);

    inscripcionResult =
        webTestClient
            .post()
            .uri("/api/inscripciones")
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(inscripcionData)
            .exchange()
            .returnResult(InscripcionDTO.class);
  }

  @Entonces("la inscripción se realiza exitosamente")
  public void laInscripcionSeRealizaExitosamente() {
    assertEquals(HttpStatus.CREATED, inscripcionResult.getStatus());
  }

  @Entonces("no se puede realizar la inscripción exitosamente")
  public void noSePuedeRealizarLaInscripcionExitosamente() {
    assertTrue(
        inscripcionResult.getStatus().is4xxClientError()
            || inscripcionResult.getStatus().is5xxServerError());
  }

  @Y("se le informa que el curso no existe")
  public void seLaInformaQueElCursoNoExiste() {
    // El mensaje específico depende del manejo de errores global
    assertTrue(inscripcionResult.getStatus().is4xxClientError());
  }

  @Y("se le informa que el cuatrimestre no existe")
  public void seLaInformaQueElCuatrimestreNoExiste() {
    assertTrue(inscripcionResult.getStatus().is4xxClientError());
  }

  @Dado("que el alumno ya está inscrito al curso {string} en el cuatrimestre actual")
  public void queElAlumnoYaEstaInscritoAlCurso(String codigoCurso) {
    // Primero inscribir al alumno
    elAlumnoSeInscribeAlCurso(codigoCurso);

    // Verificar que la inscripción fue exitosa
    assertEquals(HttpStatus.CREATED, inscripcionResult.getStatus());

    InscripcionDTO inscripcion = inscripcionResult.getResponseBody().blockFirst();
    codigoInscripcionGuardado = inscripcion.getCodigoDeInscripcion();
  }

  @Y("se le informa que ya está inscrito en ese curso")
  public void seLaInformaQueYaEstaInscritoEnEseCurso() {
    assertTrue(inscripcionResult.getStatus().is4xxClientError());
  }

  @Y("se le informa que las inscripciones están cerradas para este cuatrimestre")
  public void seLaInformaQueLasInscripcionesEstanCerradasParaEsteCuatrimestre() {
    assertTrue(inscripcionResult.getStatus().is4xxClientError());
  }

  @Cuando("el alumno consulta sus inscripciones")
  public void elAlumnoConsultaSusInscripciones() {

    consultaResult =
        webTestClient
            .get()
            .uri("/api/inscripciones/misInscripciones")
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(List.class);
  }

  @Entonces("se le informan {int} inscripciones")
  public void seLaInformanInscripciones(int cantidadEsperada) {
    assertEquals(HttpStatus.OK, consultaResult.getStatus());

    List inscripciones = consultaResult.getResponseBody().blockFirst();
    assertNotNull(inscripciones);
    assertEquals(cantidadEsperada, inscripciones.size());
  }

  @Y("cada inscripción incluye el código de inscripción, curso, cuatrimestre, fecha y horario")
  public void cadaInscripcionIncluyeLosDatos() {
    List<Map> inscripciones = consultaResult.getResponseBody().blockFirst();
    for (Map inscripcion : inscripciones) {
      assertNotNull(inscripcion.get("codigoDeInscripcion"));
      assertNotNull(inscripcion.get("codigoCurso"));
      assertNotNull(inscripcion.get("codigoCuatrimestre"));
      assertNotNull(inscripcion.get("fecha"));
      assertNotNull(inscripcion.get("horario"));
    }
  }

  @Cuando("el alumno cancela su inscripción con código de inscripción obtenido")
  public void elAlumnoCancelaSuInscripcionConCodigoObtenido() {
    assertNotNull(codigoInscripcionGuardado, "No hay código de inscripción guardado");

    eliminarResult =
        webTestClient
            .delete()
            .uri("/api/inscripciones/" + codigoInscripcionGuardado)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(Map.class);
  }

  @Cuando("el alumno cancela su inscripción con código de inscripción {long}")
  public void elAlumnoCancelaSuInscripcionConCodigo(Long codigoInscripcion) {

    eliminarResult =
        webTestClient
            .delete()
            .uri("/api/inscripciones/" + codigoInscripcion)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(Map.class);
  }

  @Entonces("la inscripción se cancela exitosamente")
  public void laInscripcionSeCancelaExitosamente() {
    assertEquals(HttpStatus.OK, eliminarResult.getStatus());
  }

  @Entonces("no se puede cancelar la inscripción exitosamente")
  public void noSePuedeCancelarLaInscripcionExitosamente() {
    assertTrue(
        eliminarResult.getStatus().is4xxClientError()
            || eliminarResult.getStatus().is5xxServerError());
  }

  @Y("se le informa que la inscripción no existe")
  public void seLaInformaQueLaInscripcionNoExiste() {
    assertTrue(eliminarResult.getStatus().is4xxClientError());
  }

  @Y("que el alumno tiene la materia {string} aprobada")
  public void queElAlumnoTieneLaMateriaAprobada(String codigoMateria) {
    // Obtener el alumno actual (último logueado)
    Alumno alumno =
        alumnoRepository
            .findByUsername("12345678")
            .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));

    // Primero crear un curso para la materia correlativa
    String codigoCurso = "CURSO-CORRELATIVA-" + codigoMateria;
    Map<String, Object> cursoRequest =
        Map.of(
            "codigo",
            codigoCurso,
            "maximoDeAlumnos",
            25,
            "codigoDeMateria",
            codigoMateria,
            "codigoCuatrimestre",
            List.of("2025-2"));

    var cursoResult =
        webTestClient
            .post()
            .uri("/api/cursos")
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(cursoRequest)
            .exchange()
            .returnResult(Map.class);

    if (cursoResult.getStatus() == HttpStatus.CREATED) {
      // Crear un acta FINAL para la materia correlativa
      ActaRequestDTO actaRequest = new ActaRequestDTO();
      actaRequest.setCodigoCurso(codigoCurso);
      actaRequest.setTipoDeActa(TipoDeActa.FINAL);
      actaRequest.setEstado(EstadoActa.ABIERTA);

      // Crear el acta
      var actaResult =
          webTestClient
              .post()
              .uri("/api/actas")
              .header("Authorization", "Bearer " + tokenHolder.getToken())
              .contentType(MediaType.APPLICATION_JSON)
              .bodyValue(actaRequest)
              .exchange()
              .returnResult(Map.class);

      if (actaResult.getStatus() == HttpStatus.CREATED) {
        Map actaCreada = actaResult.getResponseBody().blockFirst();
        Long numeroCorrelativo = ((Number) actaCreada.get("numeroCorrelativo")).longValue();

        // Agregar nota aprobada (8) al acta FINAL
        NotaRequestDTO notaRequest = new NotaRequestDTO();
        notaRequest.setValor(8);
        notaRequest.setAlumnoId(alumno.getId());

        webTestClient
            .post()
            .uri("/api/actas/{numeroCorrelativo}/notas", numeroCorrelativo)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(notaRequest)
            .exchange()
            .expectStatus()
            .isCreated();
      }
    }
  }

  @Y(
      "que existe una materia con el código de materia {string}, nombre {string} y correlativa {string}")
  public void queExisteUnaMateriaConcorrelativa(
      String codigoMateria, String nombreMateria, String codigoCorrelativa) {
    // Crear materia con correlativa
    MateriaDTO materiaDTO =
        new MateriaDTO(
            codigoMateria,
            nombreMateria,
            "Contenidos de " + nombreMateria,
            8, // creditosQueOtorga
            0,
            TipoMateria.OBLIGATORIA,
            null, // cuatrimestre
            null, // codigoPlanDeEstudios
            List.of(codigoCorrelativa) // correlativas
            );

    webTestClient
        .post()
        .uri("/api/materias")
        .header("Authorization", "Bearer " + tokenHolder.getToken())
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(materiaDTO)
        .exchange()
        .expectStatus()
        .isCreated();
  }

  @Y(
      "que existe una materia con el código de materia {string}, nombre {string} y correlativas {string} y {string}")
  public void queExisteUnaMateriaConcorrelativa(
      String codigoMateria,
      String nombreMateria,
      String codigoCorrelativa,
      String codigoCorrelativa2) {
    // Crear materia con correlativa
    MateriaDTO materiaDTO =
        new MateriaDTO(
            codigoMateria,
            nombreMateria,
            "Contenidos de " + nombreMateria,
            8, // creditosQueOtorga
            0,
            TipoMateria.OBLIGATORIA,
            null, // cuatrimestre
            null, // codigoPlanDeEstudios
            List.of(codigoCorrelativa, codigoCorrelativa2) // correlativas
            );

    webTestClient
        .post()
        .uri("/api/materias")
        .header("Authorization", "Bearer " + tokenHolder.getToken())
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(materiaDTO)
        .exchange()
        .expectStatus()
        .isCreated();
  }

  @Y(
      "que existe una materia con el código de materia {string}, nombre {string} y creditos necesarios {int}")
  public void queExisteUnaMateriaConcorrelativa(
      String codigoMateria, String nombreMateria, int creditos) {
    // Crear materia con correlativa
    MateriaDTO materiaDTO =
        new MateriaDTO(
            codigoMateria,
            nombreMateria,
            "Contenidos de " + nombreMateria,
            8, // creditosQueOtorga
            creditos,
            TipoMateria.OBLIGATORIA,
            null, // cuatrimestre
            null, // codigoPlanDeEstudios
            List.of() // correlativas
            );

    webTestClient
        .post()
        .uri("/api/materias")
        .header("Authorization", "Bearer " + tokenHolder.getToken())
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(materiaDTO)
        .exchange()
        .expectStatus()
        .isCreated();
  }
}
