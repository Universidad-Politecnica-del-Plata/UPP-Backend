package com.upp.steps;

import static org.junit.jupiter.api.Assertions.*;

import com.upp.dto.AlumnoDTO;
import com.upp.model.Rol;
import com.upp.model.Usuario;
import com.upp.repository.RolRepository;
import com.upp.repository.UsuarioRepository;
import com.upp.steps.shared.TokenHolder;
import io.cucumber.java.Before;
import io.cucumber.java.ast.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class registrar_alumno_steps {

  @Autowired private WebTestClient webTestClient;
  @Autowired private UsuarioRepository usuarioRepository;
  @Autowired private RolRepository rolRepository;
  @Autowired private TokenHolder tokenHolder;
  private String token;
  private FluxExchangeResult<AlumnoDTO> result;

  @Before
  public void setupUsuarioYLogin() {
    // Crear rol si no existe
    Rol rolGestion =
        rolRepository
            .findById("ROLE_GESTION_ESTUDIANTIL")
            .orElseGet(
                () -> {
                  Rol nuevo = new Rol("ROLE_GESTION_ESTUDIANTIL");
                  return rolRepository.save(nuevo);
                });

    // Crear usuario si no existe
    usuarioRepository
        .findByUsername("admin_gestion_estudiantil2")
        .ifPresent(usuarioRepository::delete);
    Usuario usuarioExistente =
        usuarioRepository.findByUsername("admin_gestion_estudiantil2").orElse(null);

    if (usuarioExistente == null) {
      Map<String, Object> registroData =
          Map.of(
              "username", "admin_gestion_estudiantil2",
              "password", "password",
              "roles", List.of("ROLE_GESTION_ESTUDIANTIL"));

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
        Map.of("username", "admin_gestion_estudiantil2", "password", "password");

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
    this.token = token;
  }

  @Cuando(
      "registra un nuevo alumno con DNI {long}, apellido {string}, nombre {string}, direccion {string}, telefono {string}, email {string}, fecha de nacimiento {string} y fecha de ingreso {string}")
  public void registraAlumnoConDatos(
      Long dni,
      String apellido,
      String nombre,
      String direccion,
      String telefono,
      String email,
      String fechaNacimiento,
      String fechaIngreso) {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    AlumnoDTO alumnoDTO = new AlumnoDTO();
    alumnoDTO.setDni(dni);
    alumnoDTO.setApellido(apellido);
    alumnoDTO.setNombre(nombre);
    alumnoDTO.setDireccion(direccion);
    alumnoDTO.setTelefonos(List.of(telefono));
    alumnoDTO.setEmail(email);
    alumnoDTO.setFechaNacimiento(LocalDate.parse(fechaNacimiento, formatter));
    alumnoDTO.setFechaIngreso(LocalDate.parse(fechaIngreso, formatter));

    this.result =
        webTestClient
            .post()
            .uri("/api/alumnos")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(alumnoDTO)
            .exchange()
            .returnResult(AlumnoDTO.class);
  }

  @Entonces("se registra el alumno exitosamente")
  public void seRegistraElAlumnoExitosamente() {
    assertEquals(HttpStatus.CREATED, result.getStatus());
  }

  @Dado("un alumno registrado con DNI {long}")
  public void unAlumnoRegistradoConDni(Long dni) {
    this.registraAlumnoConDatos(
        dni,
        "perez",
        "juan",
        "avenida libre 123",
        "12345678",
        "email@email.com",
        "01-01-1990",
        "01-01-1995");
  }

  @Entonces("no se registra el alumno")
  public void noSeRegistraElAlumnoExitosamente() {
    assertEquals(HttpStatus.CONFLICT, result.getStatus());
  }

  @Dado("un alumno registrado con email {string}")
  public void unAlumnoRegistradoConEmail(String email) {
    this.registraAlumnoConDatos(
        1111111L,
        "perez",
        "juan",
        "avenida libre 123",
        "12345678",
        email,
        "01-01-1990",
        "01-01-1995");
  }
}
