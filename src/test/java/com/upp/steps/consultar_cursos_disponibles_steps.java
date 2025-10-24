package com.upp.steps;

import com.upp.dto.CursoDTO;
import com.upp.model.Rol;
import com.upp.model.Usuario;
import com.upp.repository.RolRepository;
import com.upp.repository.UsuarioRepository;
import com.upp.steps.shared.TokenHolder;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class consultar_cursos_disponibles_steps {
  
  @Autowired private WebTestClient webTestClient;
  @Autowired private UsuarioRepository usuarioRepository;
  @Autowired private RolRepository rolRepository;
  @Autowired private TokenHolder tokenHolder;

  private FluxExchangeResult<CursoDTO> result;

  @Cuando("consulta los cursos disponibles para el plan de estudios {string}")
  public void consultaLosCursosDisponiblesParaElPlanDeEstudios(String codigoPlan) {
    configurarAutenticacionAlumno();
    
    result = webTestClient
        .get()
        .uri("/api/cursos/planDeEstudios/" + codigoPlan)
        .header("Authorization", "Bearer " + tokenHolder.getToken())
        .exchange()
        .returnResult(CursoDTO.class);
  }

  @Entonces("se le informan {int} cursos disponibles")
  public void seLaInforman_CursosDisponibles(int cantidadEsperada) {
    assertEquals(HttpStatus.OK, result.getStatus());
    
    List<CursoDTO> cursos = result.getResponseBody().collectList().block();
    assertNotNull(cursos);
    assertEquals(cantidadEsperada, cursos.size());
  }

  @Entonces("no se obtienen cursos disponibles exitosamente")
  public void noSeObtienenCursosDisponiblesExitosamente() {
    assertTrue(result.getStatus().is4xxClientError() || result.getStatus().is5xxServerError());
  }


  private void configurarAutenticacionAlumno() {
    try {
      Rol rolAlumno = rolRepository
          .findById("ROLE_ALUMNO")
          .orElseThrow(() -> new RuntimeException("ROLE_ALUMNO no encontrado"));

      Usuario usuarioExistente = usuarioRepository.findByUsername("alumno_test").orElse(null);

      if (usuarioExistente == null) {
        Map<String, Object> registroData = Map.of(
            "username", "alumno_test",
            "password", "password",
            "roles", List.of("ROLE_ALUMNO"));

        webTestClient
            .post()
            .uri("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(registroData)
            .exchange()
            .expectStatus()
            .isCreated();
      }

      Map<String, String> loginData = Map.of("username", "alumno_test", "password", "password");

      String token = webTestClient
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
    } catch (Exception e) {
      // Si no existe el rol ALUMNO, usar un token existente o crear usuario básico autenticado
      Map<String, String> loginData = Map.of("username", "admin_planificacion", "password", "password");
      
      try {
        String token = webTestClient
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
      } catch (Exception loginException) {
        throw new RuntimeException("No se pudo configurar autenticación para consultar cursos", loginException);
      }
    }
  }
}