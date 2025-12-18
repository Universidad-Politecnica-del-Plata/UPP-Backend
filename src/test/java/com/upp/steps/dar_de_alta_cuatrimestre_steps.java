package com.upp.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.upp.dto.CuatrimestreDTO;
import com.upp.repository.RolRepository;
import com.upp.repository.UsuarioRepository;
import com.upp.steps.shared.TokenHolder;
import io.cucumber.java.ast.Cuando;
import io.cucumber.java.es.Entonces;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class dar_de_alta_cuatrimestre_steps {
  @Autowired private WebTestClient webTestClient;
  @Autowired private UsuarioRepository usuarioRepository;
  @Autowired private RolRepository rolRepository;
  @Autowired private TokenHolder tokenHolder;

  private FluxExchangeResult<CuatrimestreDTO> result;

  @Cuando(
      "se registra un nuevo cuatrimestre con código {string}, fecha de inicio de clases {string}, fecha de fin de clases {string}, fecha de inicio de inscripción {string}, fecha de fin de inscripción {string}, fecha de inicio de integradores {string} y fecha de fin de integradores {string}")
  public void darDeAltaCuatrimestre(
      String codigo,
      String fechaInicioClases,
      String fechaFinClases,
      String fechaInicioInscripcion,
      String fechaFinInscripcion,
      String fechaInicioIntegradores,
      String fechaFinIntegradores) {

    CuatrimestreDTO cuatrimestreEnviado =
        new CuatrimestreDTO(
            codigo,
            LocalDate.parse(fechaInicioClases),
            LocalDate.parse(fechaFinClases),
            LocalDate.parse(fechaInicioInscripcion),
            LocalDate.parse(fechaFinInscripcion),
            LocalDate.parse(fechaInicioIntegradores),
            LocalDate.parse(fechaFinIntegradores));

    this.result =
        webTestClient
            .post()
            .uri("/api/cuatrimestres")
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(cuatrimestreEnviado)
            .exchange()
            .returnResult(CuatrimestreDTO.class);
  }

  @Entonces("se registra el cuatrimestre {string} exitosamente")
  public void seRegistraElCuatrimestreExitosamente(String codigo) {
    var resultGetCuatrimestre =
        webTestClient
            .get()
            .uri("/api/cuatrimestres/{codigo}", codigo)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(CuatrimestreDTO.class);

    assertEquals(HttpStatus.OK, resultGetCuatrimestre.getStatus());
  }

  @Entonces("no se registra el cuatrimestre exitosamente")
  public void noSeRegistraElCuatrimestreExitosamente() {
    // Puede ser CONFLICT (409) para código duplicado o BAD_REQUEST (400) para validaciones
    assertNotEquals(HttpStatus.OK, result.getStatus());
  }
}
