# UPP Backend

Backend del sistema de gestión universitaria UPP (Universidad Politécnica del Plata). Permite administrar alumnos, carreras, materias, planes de estudio, cursos, inscripciones y actas de examen.

## Tecnologías

- **Java 17** con **Spring Boot 3.4.0**
- **PostgreSQL** (producción) / **H2** (tests)
- **Spring Security** con autenticación JWT
- **Spring Data JPA** para persistencia
- **Cucumber** para tests BDD

## Cómo levantar la aplicación

### 1. Requisitos previos

- Java 17+
- Maven 3.8+
- PostgreSQL

### 2. Configurar variables de entorno

Configurar estas variables en el IDE o en el sistema:

```
DB_HOST=localhost
DB_PORT=5432
DB_NAME=upp
DB_USERNAME=postgres
DB_PASSWORD=postgres
```

### 3. Ejecutar
Desde una consola
```bash
mvn spring-boot:run
```
O ejecutar desde el IDE.

La aplicación se levanta en `http://localhost:8080`

### 4. Verificar que funciona

```bash
curl http://localhost:8080/api/ping
```

## Cómo correr los tests

```bash
# Todos los tests
mvn test

# Solo los tests de Cucumber
mvn clean -Dtest=CucumberTest test

# Excluir escenarios marcados como @todo
mvn test -Dcucumber.filter.tags="not @todo"
```

## Linter (Spotless)

```bash
# Verificar formato
mvn spotless:check

# Aplicar formato automáticamente
mvn spotless:apply
```

## Estructura del código

```
src/main/java/com/upp/
├── controller/     # Endpoints REST
├── service/        # Lógica de negocio
├── repository/     # Interacción con base de datos
├── model/          # Entidades
├── dto/            # Objetos de transferencia de datos con el frontend
├── security/       # Configuración JWT y autenticación
├── config/         # Configuración de Spring
└── exception/      # Excepciones
```

## Controllers (endpoints)

- **AuthController** (`/api/auth`): Login (devuelve JWT) y registro de usuarios.

- **PingController** (`/api/ping`): Ping para verificar que el backend está activo.

- **AlumnoController** (`/api/alumnos`): ABM de alumnos y consulta del alumno logueado.

- **CarreraController** (`/api/carreras`): ABM de carreras.

- **MateriaController** (`/api/materias`): ABM de materias.

- **PlanDeEstudiosController** (`/api/planDeEstudios`): ABM de planes de estudio.

- **CursoController** (`/api/cursos`): ABM de cursos y consulta de cursos disponibles por materia o por plan de estudios.

- **CuatrimestreController** (`/api/cuatrimestres`): ABM de cuatrimestres.

- **InscripcionController** (`/api/inscripciones`): ABM de inscripciones a cursos para el alumno.

- **ActaController** (`/api/actas`): Usado por docentes para crear actas, cargar notas, cerrar actas y ver alumnos inscriptos.

- **HistoriaAcademicaController** (`/api/historia-academica`): Endpoints para consultar la historia académica de un alumno (materias aprobadas, créditos y si está habilitado para una inscripción).

## Services (lógica de negocio)

- **AlumnoService**: Gestiona alumnos (alta, baja lógica, modificación). Valida fechas de nacimiento/ingreso/egreso y que los planes de estudio correspondan a las carreras asignadas.

- **CarreraService**: Gestiona carreras. No permite eliminar una carrera que tenga planes de estudio asociados.

- **MateriaService**: Gestiona materias y sus correlatividades.

- **PlanDeEstudiosService**: Gestiona planes de estudio. Asocia materias y calcula los créditos obligatorios automáticamente.

- **CursoService**: Gestiona cursos. 

- **CuatrimestreService**: Gestiona cuatrimestres. Define períodos de inscripción, de clases y de integradores, y valida que las fechas sean coherentes.

- **InscripcionService**: Inscribe alumnos a cursos. Valida que el alumno tenga los créditos necesarios, las correlativas aprobadas, y que esté dentro del período de inscripción.

- **ActaService**: Gestiona actas de examen (parciales y finales). Permite cargar notas (solo aprobatorias 4-10) y cerrar actas.

- **HistoriaAcademicaService**: Consulta la historia académica de un alumno: materias aprobadas (materias que tienen una nota aprobatoria en un acta de tipo FINAL), créditos acumulados, y si cumple los requisitos para inscribirse a una materia.


## Modelo de datos (entidades principales)

- **Usuario**: Base para autenticación (username, password, roles)
- **Alumno**: Extiende Usuario. Tiene matrícula, datos personales, carreras y planes.
- **Carrera**: Tiene código, nombre, título y planes de estudio.
- **PlanDeEstudios**: Pertenece a una carrera, tiene materias y créditos.
- **Materia**: Tiene código, nombre, créditos y correlativas.
- **Curso**: Instancia de una materia para uno o más cuatrimestres.
- **Cuatrimestre**: Define períodos de inscripción, clases e integradores.
- **Inscripción**: Vincula un alumno con un curso en un cuatrimestre.
- **Acta**: Acta de examen (parcial o final) para un curso.
- **Nota**: Nota de un alumno en un acta.

## Roles del sistema

| Rol | Permisos                                                                                          |
|-----|---------------------------------------------------------------------------------------------------|
| ROLE_ALUMNO | Inscribirse a cursos, ver detalles de cursos, ver detalles de materias, ver su historia académica |
| ROLE_DOCENTE | Crear actas, cargar notas                                                                         |
| ROLE_GESTION_ESTUDIANTIL | ABM de alumnos                                                                                    |
| ROLE_GESTION_ACADEMICA | ABM de carreras, materias y planes                                                                |
| ROLE_GESTOR_DE_PLANIFICACION | ABM de cursos y cuatrimestres                                                                     |

## Tests

Los tests están organizados en:

```
src/test/
├── java/com/upp/
│   ├── steps/           # Step definitions de Cucumber
│   ├── service/         # Tests unitarios de services
│   └── model/           # Tests unitarios de entidades
└── resources/features/  # Features de Cucumber (BDD)
    ├── gestion_academica/
    ├── gestion_estudiantil/
    ├── alumno/
    ├── docente/
    └── ...
```

## Autenticación

La API usa JWT. Para autenticarte desde la consola:

```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "tu_usuario", "password": "tu_password"}'

# Respuesta: {"token": "eyJhbGciOiJIUzUxMiJ9..."}

# Usar el token en requests
curl http://localhost:8080/api/alumnos \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."
```
