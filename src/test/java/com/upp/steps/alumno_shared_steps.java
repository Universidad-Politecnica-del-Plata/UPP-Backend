package com.upp.steps;

import com.upp.model.Alumno;
import com.upp.repository.AlumnoRepository;
import com.upp.repository.RolRepository;
import com.upp.repository.UsuarioRepository;
import com.upp.service.AlumnoService;
import com.upp.steps.shared.TokenHolder;
import io.cucumber.java.es.Dado;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class alumno_shared_steps {

  @Autowired private WebTestClient webTestClient;
  @Autowired private UsuarioRepository usuarioRepository;
  @Autowired private RolRepository rolRepository;
  @Autowired private AlumnoRepository alumnoRepository;
  @Autowired private AlumnoService alumnoService;
  @Autowired private TokenHolder tokenHolder;

  @Dado("que hay un gestor estudiantil logueado")
  public void gestorEstudiantilLogueado() {
    // Verificar que el rol existe
    rolRepository
        .findById("ROLE_GESTION_ESTUDIANTIL")
        .orElseThrow(() -> new RuntimeException("ROLE_GESTION_ESTUDIANTIL no encontrado"));

    // Limpiar usuario existente si existe
    usuarioRepository
        .findByUsername("admin_gestion_estudiantil")
        .ifPresent(usuarioRepository::delete);

    // Crear usuario
    Map<String, Object> registroData =
        Map.of(
            "username", "admin_gestion_estudiantil",
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

    // Login
    Map<String, String> loginData =
        Map.of("username", "admin_gestion_estudiantil", "password", "password");

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

  @Dado("existe un alumno con matrícula {long}")
  public void existeUnAlumnoConMatricula(Long matricula) {
    // Limpiar alumno existente
    alumnoRepository.findByMatricula(matricula).ifPresent(alumnoRepository::delete);

    // Crear alumno de prueba
    Alumno alumno = new Alumno();
    alumno.setMatricula(matricula);
    alumno.setNombre("Juan");
    alumno.setApellido("Perez");
    alumno.setDni(12345678L);
    alumno.setEmail("juan.perez@example.com");
    alumno.setDireccion("Calle Falsa 123");
    alumno.setFechaNacimiento(LocalDate.of(1990, 1, 1));
    alumno.setFechaIngreso(LocalDate.of(2023, 3, 1));
    alumno.setTelefonos(List.of("123456789"));
    alumno.setUsername("12345678");
    alumno.setPassword("12345678");
    alumno.setHabilitado(true);

    alumnoRepository.save(alumno);
  }
}
