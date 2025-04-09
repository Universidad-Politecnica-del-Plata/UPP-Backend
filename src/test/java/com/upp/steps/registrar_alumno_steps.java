package com.upp.steps;

import com.upp.model.Alumno;
import io.cucumber.java.ast.Cuando;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class registrar_alumno_steps {
    @Autowired
    private WebTestClient webTestClient;

    @Cuando("registra un nuevo alumno con DNI {long}, apellido {string}, nombre {string}, direccion {string}, telefono {int}, email {string}, fecha de nacimiento {string} y fecha de ingreso {string}")
    public void registraAlumnoConDatos(Long dni, String apellido, String nombre, String direccion, Integer telefono, String email, String fechaNacimiento, String fechaIngreso) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        Alumno alumnoEnviado = new Alumno();
        alumnoEnviado.setDni(dni);
        alumnoEnviado.setApellido(apellido);
        alumnoEnviado.setNombre(nombre);
        alumnoEnviado.setDireccion(direccion);
        //alumnoEnviado.setTelefonos(List.of(telefono.toString()));
        alumnoEnviado.setEmail(email);
        alumnoEnviado.setFechaNacimiento(LocalDate.parse(fechaNacimiento, formatter));
        alumnoEnviado.setFechaIngreso(LocalDate.parse(fechaIngreso, formatter));

        Alumno alumnoRecibido = webTestClient.post()
                .uri("/api/alumnos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(alumnoEnviado)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Alumno.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(alumnoRecibido);
    }
}
