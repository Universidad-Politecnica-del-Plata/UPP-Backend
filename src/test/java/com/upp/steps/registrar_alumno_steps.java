package com.upp.steps;

import com.upp.model.Alumno;
import io.cucumber.java.ast.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import jakarta.transaction.Transactional;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class registrar_alumno_steps {
    @Autowired
    private WebTestClient webTestClient;
    private FluxExchangeResult<Alumno> result;

    @Cuando("registra un nuevo alumno con DNI {long}, apellido {string}, nombre {string}, direccion {string}, telefono {string}, email {string}, fecha de nacimiento {string} y fecha de ingreso {string}")
    public void registraAlumnoConDatos(Long dni, String apellido, String nombre, String direccion, String telefono, String email, String fechaNacimiento, String fechaIngreso) {
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

        this.result = webTestClient.post()
                .uri("/api/alumnos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(alumnoEnviado)
                .exchange()
                .returnResult(Alumno.class);
    }

    @Entonces("se registra el alumno exitosamente")
    public void seRegistraElAlumnoExitosamente() {
        assertEquals(HttpStatus.CREATED, result.getStatus());
    }

    @Dado("un alumno registrado con DNI {long}")
    public void unAlumnoRegistradoConDni(Long dni) {
        this.registraAlumnoConDatos(dni, "perez", "juan", "avenida libre 123", "12345678", "email@email.com", "01-01-1990", "01-01-1995");
    }

    @Entonces("no se registra el alumno")
    public void noSeRegistraElAlumnoExitosamente() {
        assertEquals(HttpStatus.CONFLICT, result.getStatus());
    }

    @Dado("un alumno registrado con email {string}")
    public void unAlumnoRegistradoConEmail(String email) {
        this.registraAlumnoConDatos(1111111L, "perez", "juan", "avenida libre 123", "12345678", email, "01-01-1990", "01-01-1995");
    }
}
