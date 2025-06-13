package com.upp.steps;

import static org.junit.jupiter.api.Assertions.*;

import com.upp.dto.MateriaDTO;
import com.upp.model.Rol;
import com.upp.model.TipoMateria;
import com.upp.model.Usuario;
import com.upp.repository.RolRepository;
import com.upp.repository.UsuarioRepository;
import io.cucumber.java.Before;
import io.cucumber.java.ast.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class dar_de_alta_materia_steps {
  @Autowired private WebTestClient webTestClient;
  @Autowired private UsuarioRepository usuarioRepository;

  @Autowired private RolRepository rolRepository;

  private String token;
  private FluxExchangeResult<MateriaDTO> result;

  @Before
  public void setupUsuarioYLogin() {
    // Crear rol si no existe
    Rol rolGestion = rolRepository.findById("GESTION_ACADEMICA").orElse(null);
    if (rolGestion == null) {
      rolGestion = new Rol("GESTION_ACADEMICA");
      rolRepository.save(rolGestion);
    }

    // Crear usuario si no existe
    Usuario usuario = usuarioRepository.findByUsername("admin_gestion").orElse(null);
    if (usuario == null) {
      usuario = new Usuario();
      usuario.setUsername("admin_gestion");
      usuario.setPassword("{noop}password");
      usuario.setHabilitado(true);
      usuario.getRoles().add(rolGestion);
      usuarioRepository.save(usuario);
    }

    // Login para obtener token
    Map<String, String> loginData = Map.of("username", "admin_gestion", "password", "password");

    token =
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
  }


  @Cuando(
      "se registra una materia con código de materia {string}, nombre {string}, contenidos {string}, tipo de materia {string}, cantidad de créditos que otorga {int} y créditos necesarios {int}")
  public void darDeAltaMateria(
      String codigo,
      String nombre,
      String contenidos,
      String tipoMateria,
      Integer creditosOtorga,
      Integer creditosNecesarios) {

    this.darDeAltaMateriaConCorrelativa(
        codigo, nombre, contenidos, tipoMateria, "", creditosOtorga, creditosNecesarios);
  }

  @Entonces("se registra la materia exitosamente")
  public void seRegistraLaMateriaExitosamente() {
    assertEquals(HttpStatus.CREATED, result.getStatus());
  }

  @Entonces("se registra la materia {string} exitosamente")
  public void seRegistraLaMateriaConCodigoExitosamente(String codigo) {
    var resultGetMateria =
        webTestClient
            .get()
            .uri("/api/materias/{codigo}", codigo)
            .exchange()
            .returnResult(MateriaDTO.class);
    assertEquals(HttpStatus.OK, resultGetMateria.getStatus());
  }

  @Dado("que existe una materia con el código de materia {string} y nombre {string}")
  public void queExisteUnaMateria(String codigo, String nombre) {
    this.darDeAltaMateria(codigo, nombre, "Contenido", "Optativa", 1, 1);
  }

  @Cuando(
      "se registra una materia con código de materia {string}, nombre {string}, contenidos {string}, tipo de materia {string}, con correlativa {string}, cantidad de créditos que otorga {int} y créditos necesarios {int}")
  public void darDeAltaMateriaConCorrelativa(
      String codigo,
      String nombre,
      String contenidos,
      String tipoMateria,
      String correlativas,
      Integer creditosOtorga,
      Integer creditosNecesarios) {
    List<String> listaDeCorrelativas =
        Arrays.stream(correlativas.split(",")).map(String::trim).toList();

    MateriaDTO materiaEnviada =
        new MateriaDTO(
            codigo,
            nombre,
            contenidos,
            creditosOtorga,
            creditosNecesarios,
            TipoMateria.valueOf(tipoMateria.toUpperCase()),
            listaDeCorrelativas);

    this.result =
        webTestClient
            .post()
            .uri("/api/materias")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(materiaEnviada)
            .exchange()
            .returnResult(MateriaDTO.class);
  }

  @Entonces("no se registra la materia exitosamente")
  public void noSeRegistraLaMateriaExitosamente() {
    assertEquals(HttpStatus.CONFLICT, result.getStatus());
  }
}
