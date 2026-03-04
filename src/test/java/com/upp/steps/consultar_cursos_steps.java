package com.upp.steps;

import static org.junit.jupiter.api.Assertions.*;

import com.upp.dto.AlumnoDTO;
import com.upp.dto.CarreraDTO;
import com.upp.dto.CursoDTO;
import com.upp.dto.HistoriaAcademicaDTO;
import com.upp.dto.InscripcionDTO;
import com.upp.dto.MateriaAprobadaDTO;
import com.upp.dto.MateriaDTO;
import com.upp.model.TipoMateria;
import com.upp.steps.shared.AuthHelper;
import com.upp.steps.shared.TokenHolder;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Steps para el feature de consultar cursos de una materia.
 * Permite a un alumno ver los cursos disponibles para una materia específica.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class consultar_cursos_steps {

  @Autowired private WebTestClient webTestClient;
  @Autowired private TokenHolder tokenHolder;
  @Autowired private AuthHelper authHelper;

  private FluxExchangeResult<CursoDTO> consultaResult;
  private List<CursoDTO> cursosObtenidos;
  private String dniAlumnoActual;
  private String codigoCarreraActual;

  // Variables para consultar cursos inscriptos
  private FluxExchangeResult<InscripcionDTO> inscripcionesResult;
  private List<InscripcionDTO> inscripcionesObtenidas;

  // Variables para consultar detalles de carrera
  private FluxExchangeResult<CarreraDTO> carreraResult;
  private CarreraDTO carreraObtenida;

  // Variables para consultar historia académica
  private FluxExchangeResult<HistoriaAcademicaDTO> historiaResult;
  private HistoriaAcademicaDTO historiaObtenida;

  @Dado("que existe un alumno con nombre {string}, apellido {string} y DNI {string}")
  public void queExisteUnAlumnoConNombreApellidoYDni(String nombre, String apellido, String dni) {
    // Guardamos el DNI para usarlo luego como username
    this.dniAlumnoActual = dni;

    // Primero nos logueamos como gestor estudiantil para crear el alumno
    authHelper.loginGestorEstudiantil();

    // Crear el alumno via API
    AlumnoDTO alumnoDTO = new AlumnoDTO();
    alumnoDTO.setNombre(nombre);
    alumnoDTO.setApellido(apellido);
    alumnoDTO.setDni(Long.parseLong(dni));
    alumnoDTO.setEmail(dni + "@test.com");
    alumnoDTO.setDireccion("Direccion de prueba 123");
    alumnoDTO.setFechaNacimiento(LocalDate.of(1995, 5, 15));
    alumnoDTO.setFechaIngreso(LocalDate.of(2023, 3, 1));
    alumnoDTO.setTelefonos(List.of("1234567890"));

    var result =
        webTestClient
            .post()
            .uri("/api/alumnos")
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(alumnoDTO)
            .exchange()
            .returnResult(AlumnoDTO.class);

    // Si el alumno ya existe (CONFLICT), lo ignoramos
    assertTrue(
        result.getStatus() == HttpStatus.CREATED || result.getStatus() == HttpStatus.CONFLICT,
        "El alumno debería haberse creado o ya existir");

    // Logueamos al alumno para que los siguientes steps puedan usarlo
    authHelper.loginAlumno(dni, dni);
  }

  @Y("esta inscripto en la carrera {string}")
  public void estaInscriptoEnLaCarrera(String nombreCarrera) {
    // Generar código de carrera a partir del nombre
    String codigoCarrera = nombreCarrera.toUpperCase().replace(" ", "-").replace(".", "");
    this.codigoCarreraActual = codigoCarrera;

    // Loguearnos como gestor académico para crear la carrera si no existe
    authHelper.loginGestorAcademico();

    // Intentar crear la carrera
    CarreraDTO carreraDTO = new CarreraDTO();
    carreraDTO.setCodigoDeCarrera(codigoCarrera);
    carreraDTO.setNombre(nombreCarrera);
    carreraDTO.setTitulo("Título de " + nombreCarrera);
    carreraDTO.setIncumbencias("Incumbencias de " + nombreCarrera);

    webTestClient
        .post()
        .uri("/api/carreras")
        .header("Authorization", "Bearer " + tokenHolder.getToken())
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(carreraDTO)
        .exchange();
    // Ignoramos el resultado, puede existir

    // Ahora inscribimos al alumno en la carrera actualizando sus datos
    authHelper.loginGestorEstudiantil();

    // Obtener el alumno actual
    var alumnosResult =
        webTestClient
            .get()
            .uri("/api/alumnos")
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(AlumnoDTO.class);

    List<AlumnoDTO> alumnos = alumnosResult.getResponseBody().collectList().block();
    AlumnoDTO alumnoActual =
        alumnos.stream()
            .filter(a -> a.getDni() != null && a.getDni().toString().equals(dniAlumnoActual))
            .findFirst()
            .orElse(null);

    if (alumnoActual != null) {
      // Actualizar el alumno con la carrera
      List<String> carreras = alumnoActual.getCodigosCarreras();
      if (carreras == null) {
        carreras = new ArrayList<>();
      }
      if (!carreras.contains(codigoCarrera)) {
        carreras.add(codigoCarrera);
      }
      alumnoActual.setCodigosCarreras(carreras);

      webTestClient
          .put()
          .uri("/api/alumnos/" + alumnoActual.getMatricula())
          .header("Authorization", "Bearer " + tokenHolder.getToken())
          .contentType(MediaType.APPLICATION_JSON)
          .bodyValue(alumnoActual)
          .exchange();
    }

    // Finalmente, logueamos al alumno
    authHelper.loginAlumno(dniAlumnoActual, dniAlumnoActual);
  }

  @Y("existen cursos para la materia {string}")
  public void existenCursosParaLaMateria(String nombreMateria) {
    // Generar código de materia a partir del nombre
    String codigoMateria = nombreMateria.toUpperCase().replace(" ", "-");

    // Loguearnos como gestor académico para crear la materia
    authHelper.loginGestorAcademico();

    // Crear la materia si no existe
    MateriaDTO materiaDTO =
        new MateriaDTO(
            codigoMateria,
            nombreMateria,
            "Contenidos de " + nombreMateria,
            8, // creditosQueOtorga
            0, // creditosNecesarios
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
        .exchange();
    // Ignoramos si ya existe

    // Loguearnos como gestor de planificación para crear los cursos
    authHelper.loginGestorPlanificacion();

    // Crear dos cursos para la materia
    CursoDTO curso1 = new CursoDTO("Curso 1", 30, codigoMateria, null);
    CursoDTO curso2 = new CursoDTO("Curso 2", 25, codigoMateria, null);

    webTestClient
        .post()
        .uri("/api/cursos")
        .header("Authorization", "Bearer " + tokenHolder.getToken())
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(curso1)
        .exchange();

    webTestClient
        .post()
        .uri("/api/cursos")
        .header("Authorization", "Bearer " + tokenHolder.getToken())
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(curso2)
        .exchange();

    // Volver a loguear al alumno
    authHelper.loginAlumno(dniAlumnoActual, dniAlumnoActual);
  }

  @Y("no existen cursos para la materia {string}")
  public void noExistenCursosParaLaMateria(String nombreMateria) {
    // Generar código de materia a partir del nombre
    String codigoMateria = nombreMateria.toUpperCase().replace(" ", "-");

    // Loguearnos como gestor académico para crear la materia
    authHelper.loginGestorAcademico();

    // Crear la materia si no existe (sin cursos asociados)
    MateriaDTO materiaDTO =
        new MateriaDTO(
            codigoMateria,
            nombreMateria,
            "Contenidos de " + nombreMateria,
            8, // creditosQueOtorga
            0, // creditosNecesarios
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
        .exchange();
    // Ignoramos si ya existe

    // Volver a loguear al alumno
    authHelper.loginAlumno(dniAlumnoActual, dniAlumnoActual);
  }

  @Cuando("consulta todos los cursos de la materia {string}")
  public void consultaTodosLosCursosDeLaMateria(String codigoMateria) {

    consultaResult =
        webTestClient
            .get()
            .uri("/api/cursos/materia/" + codigoMateria)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(CursoDTO.class);

    // Si la consulta fue exitosa, guardamos los cursos obtenidos
    if (consultaResult.getStatus() == HttpStatus.OK) {
      cursosObtenidos = consultaResult.getResponseBody().collectList().block();
    }
  }

  @Entonces("se le informa curso con codigo de curso {string}")
  public void seLeInformaCursoConCodigoDeCurso(String codigoCurso) {
    assertEquals(HttpStatus.OK, consultaResult.getStatus(), "La consulta debería ser exitosa");
    assertNotNull(cursosObtenidos, "Se deberían haber obtenido cursos");

    boolean cursoEncontrado =
        cursosObtenidos.stream().anyMatch(c -> codigoCurso.equals(c.getCodigo()));
    assertTrue(
        cursoEncontrado, "Debería encontrarse el curso con código: " + codigoCurso);
  }

  @Entonces("se le informa que la materia no existe")
  public void seLeInformaQueLaMateriaNoExiste() {
    assertTrue(
        consultaResult.getStatus().is4xxClientError(),
        "Debería retornar un error 4xx cuando la materia no existe");
  }

  @Entonces("se le informa que no hay cursos disponibles para la materia")
  public void seLeInformaQueNoHayCursosDisponiblesParaLaMateria() {
    assertEquals(HttpStatus.OK, consultaResult.getStatus(), "La consulta debería ser exitosa");
    assertNotNull(cursosObtenidos, "La lista de cursos no debería ser null");
    assertTrue(cursosObtenidos.isEmpty(), "La lista de cursos debería estar vacía");
  }

  // =====================================================
  // Steps para Consultar Cursos Inscriptos
  // =====================================================

  // Nota: El step "el alumno se inscribe al curso {string} en el cuatrimestre actual"
  // ya existe en inscribirse_a_un_curso_steps.java y se reutiliza aquí.

  @Y("no esta inscripto en ningún curso")
  public void noEstaInscriptoEnNingunCurso() {
    // Asegurarnos de estar logueados como alumno
    authHelper.loginAlumno(dniAlumnoActual, dniAlumnoActual);

    // Este step simplemente verifica que el alumno no tiene inscripciones
    // No hace nada activamente, solo establece el contexto
    // El alumno recién creado no debería tener inscripciones
  }

  @Cuando("consulta los cursos en que esta inscripto")
  public void consultaLosCursosEnQueEstaInscripto() {
    // Asegurarnos de estar logueados como alumno
    authHelper.loginAlumno(dniAlumnoActual, dniAlumnoActual);

    inscripcionesResult =
        webTestClient
            .get()
            .uri("/api/inscripciones/misInscripciones")
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(InscripcionDTO.class);

    // Si la consulta fue exitosa, guardamos las inscripciones obtenidas
    if (inscripcionesResult.getStatus() == HttpStatus.OK) {
      inscripcionesObtenidas = inscripcionesResult.getResponseBody().collectList().block();
    }
  }

  @Entonces("se informa que se inscribio al curso {string} en el cuatrimestre {string}")
  public void seInformaQueSeInscribioAlCursoEnElCuatrimestre(
      String codigoCurso, String codigoCuatrimestre) {
    assertEquals(
        HttpStatus.OK,
        inscripcionesResult.getStatus(),
        "La consulta de inscripciones debería ser exitosa");
    assertNotNull(inscripcionesObtenidas, "Se deberían haber obtenido inscripciones");
    assertFalse(inscripcionesObtenidas.isEmpty(), "Debería haber al menos una inscripción");

    boolean inscripcionEncontrada =
        inscripcionesObtenidas.stream()
            .anyMatch(
                i ->
                    codigoCurso.equals(i.getCodigoCurso())
                        && codigoCuatrimestre.equals(i.getCodigoCuatrimestre()));

    assertTrue(
        inscripcionEncontrada,
        String.format(
            "Debería encontrarse una inscripción al curso %s en el cuatrimestre %s",
            codigoCurso, codigoCuatrimestre));
  }

  @Entonces("se le informa que no esta inscripto en ningún curso")
  public void seLeInformaQueNoEstaInscriptoEnNingunCurso() {
    assertEquals(
        HttpStatus.OK,
        inscripcionesResult.getStatus(),
        "La consulta de inscripciones debería ser exitosa");
    assertNotNull(inscripcionesObtenidas, "La lista de inscripciones no debería ser null");
    assertTrue(
        inscripcionesObtenidas.isEmpty(),
        "El alumno no debería estar inscripto en ningún curso");
  }

  // =====================================================
  // Steps para Consultar Detalles de Carrera
  // =====================================================

  @Cuando("consulta la información de su carrera {string}")
  public void consultaLaInformacionDeSuCarrera(String codigoCarrera) {
    // Asegurarnos de estar logueados como alumno
    authHelper.loginAlumno(dniAlumnoActual, dniAlumnoActual);

    carreraResult =
        webTestClient
            .get()
            .uri("/api/carreras/" + codigoCarrera)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(CarreraDTO.class);

    // Si la consulta fue exitosa, guardamos la carrera obtenida
    if (carreraResult.getStatus() == HttpStatus.OK) {
      carreraObtenida = carreraResult.getResponseBody().blockFirst();
    }
  }

  @Entonces(
      "se le informa nombre {string}, titulo {string} e incumbencias {string}")
  public void seLeInformaNombreTituloEIncumbencias(
      String nombre, String titulo, String incumbencias) {
    assertEquals(
        HttpStatus.OK,
        carreraResult.getStatus(),
        "La consulta de carrera debería ser exitosa");
    assertNotNull(carreraObtenida, "Se debería haber obtenido la carrera");

    assertEquals(nombre, carreraObtenida.getNombre(), "El nombre de la carrera no coincide");
    assertEquals(titulo, carreraObtenida.getTitulo(), "El título de la carrera no coincide");
    assertEquals(
        incumbencias,
        carreraObtenida.getIncumbencias(),
        "Las incumbencias de la carrera no coinciden");
  }

  @Entonces("se le informa que la carrera no existe")
  public void seLeInformaQueLaCarreraNoExiste() {
    assertTrue(
        carreraResult.getStatus().is4xxClientError(),
        "Debería retornar un error 4xx cuando la carrera no existe");
  }

  // =====================================================
  // Steps para Consultar Historia Académica
  // =====================================================

  @Cuando("consulta su historia académica")
  public void consultaSuHistoriaAcademica() {
    // El alumno ya debería estar logueado desde steps anteriores
    // Si tenemos dniAlumnoActual, nos aseguramos de estar logueados
    if (dniAlumnoActual != null) {
      authHelper.loginAlumno(dniAlumnoActual, dniAlumnoActual);
    }

    historiaResult =
        webTestClient
            .get()
            .uri("/api/historia-academica/mi-historia")
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(HistoriaAcademicaDTO.class);

    // Si la consulta fue exitosa, guardamos la historia obtenida
    if (historiaResult.getStatus() == HttpStatus.OK) {
      historiaObtenida = historiaResult.getResponseBody().blockFirst();
    }
  }

  @Entonces("se informa materia aprobada {string} con nota {int}")
  public void seInformaMateriaAprobadaConNota(String nombreMateria, Integer nota) {
    assertEquals(
        HttpStatus.OK,
        historiaResult.getStatus(),
        "La consulta de historia académica debería ser exitosa");
    assertNotNull(historiaObtenida, "Se debería haber obtenido la historia académica");
    assertNotNull(
        historiaObtenida.getMateriasAprobadas(), "La lista de materias aprobadas no debería ser null");

    boolean materiaEncontrada =
        historiaObtenida.getMateriasAprobadas().stream()
            .anyMatch(
                m -> nombreMateria.equals(m.getNombre()) && nota.equals(m.getNota()));

    assertTrue(
        materiaEncontrada,
        String.format(
            "Debería encontrarse la materia %s con nota %d en la historia académica",
            nombreMateria, nota));
  }

  @Entonces("se le informa que no tiene materias aprobadas")
  public void seLeInformaQueNoTieneMateriasAprobadas() {
    assertEquals(
        HttpStatus.OK,
        historiaResult.getStatus(),
        "La consulta de historia académica debería ser exitosa");
    assertNotNull(historiaObtenida, "Se debería haber obtenido la historia académica");

    assertTrue(
        historiaObtenida.getMateriasAprobadas() == null
            || historiaObtenida.getMateriasAprobadas().isEmpty(),
        "El alumno no debería tener materias aprobadas");
  }
}