#language: es
Característica: Inscribirse a un curso

  Antecedentes:
    Dado que hay un gestor academico logueado
    Y que existe una materia con el código de materia "997-MA" y nombre "Analisis I"
    Y que existe una materia con el código de materia "998-MA" y nombre "Algebra I"
    Y que existe una materia con el código de materia "999-MA" y nombre "Algoritmos y Programacion I"
    Y que existe una materia con el código de materia "CORRELATIVA" y nombre "Algebra II"
    Y que existe una materia con el código de materia "NECESITA CORRELATIVA", nombre "Algebra III" y correlativa "CORRELATIVA"
    Y que existe una materia con el código de materia "MateriaQueNecesitaCredito", nombre "Algebra Lineal" y creditos necesarios 1
    Y que existe una materia con el código de materia "MateriaDeElMillonDeCreditos", nombre "Algebra Imposible" y creditos necesarios 1000000
    Y que existe una materia con el código de materia "NECESITA 2 CORRELATIVAS", nombre "Algebra 4D" y correlativas "CORRELATIVA" y "998-MA"

    Y que hay un gestor de planificacion logueado
    Y se registra un nuevo cuatrimestre con código "2025-2", fecha de inicio de clases "9999-02-01", fecha de fin de clases "9999-07-15", fecha de inicio de inscripción "2025-01-01", fecha de fin de inscripción "9999-01-31", fecha de inicio de integradores "9999-07-16" y fecha de fin de integradores "9999-07-31"
    Y se registra un nuevo curso con código "CURSO-001", máximo de alumnos 25, materia "997-MA" y cuatrimestres "2025-2"
    Y se registra un nuevo curso con código "CURSO-002", máximo de alumnos 25, materia "998-MA" y cuatrimestres "2025-2"
    Y se registra un nuevo curso con código "CURSO-Correlativa inicial", máximo de alumnos 25, materia "CORRELATIVA" y cuatrimestres "2025-2"
    Y se registra un nuevo curso con código "CURSO-Correlativas", máximo de alumnos 25, materia "NECESITA CORRELATIVA" y cuatrimestres "2025-2"
    Y se registra un nuevo curso con código "CURSO-que-necesita-creditos", máximo de alumnos 25, materia "MateriaQueNecesitaCredito" y cuatrimestres "2025-2"
    Y se registra un nuevo curso con código "CURSO-que-necesita-un-millon-creditos", máximo de alumnos 25, materia "MateriaDeElMillonDeCreditos" y cuatrimestres "2025-2"
    Y se registra un nuevo curso con código "CURSO-NECESITA 2 CORRELATIVAS", máximo de alumnos 25, materia "NECESITA 2 CORRELATIVAS" y cuatrimestres "2025-2"

    Y que hay un gestor estudiantil logueado
    Y registra un nuevo alumno con DNI 12345678, apellido "Perez", nombre "Juan", direccion "Calle Falsa 123", telefono "1234", email "juan@mail.com", fecha de nacimiento "01-01-1990" y fecha de ingreso "01-03-2015"
    Y que hay un docente logueado
    Y el docente abre un acta de "Final" para el curso "CURSO-Correlativa inicial"
    Y que hay un alumno logueado con username "12345678", password "12345678"
    Y el alumno se inscribe al curso "CURSO-Correlativa inicial" en el cuatrimestre actual
    Y que hay un docente logueado
    Y el docente carga la nota de un alumno con dni 12345678 y nota 8 para el curso "CURSO-Correlativa inicial"
    Y que hay un alumno logueado con username "12345678", password "12345678"

  Escenario: Inscribirse a un curso exitosamente
    Cuando el alumno se inscribe al curso "CURSO-001" en el cuatrimestre actual
    Entonces la inscripción se realiza exitosamente

  Escenario: Inscribirse dos veces al mismo curso fracasa
    Dado que el alumno ya está inscrito al curso "CURSO-001" en el cuatrimestre actual
    Cuando el alumno se inscribe al curso "CURSO-001" en el cuatrimestre actual
    Entonces no se puede realizar la inscripción exitosamente
    Y se le informa que ya está inscrito en ese curso

  Escenario: Inscribirse fuera del período de inscripción fracasa
    Dado se registra un nuevo cuatrimestre con código "2024-1", fecha de inicio de clases "2024-02-01", fecha de fin de clases "3024-07-15", fecha de inicio de inscripción "2024-01-01", fecha de fin de inscripción "2024-01-31", fecha de inicio de integradores "3024-07-16" y fecha de fin de integradores "3024-07-31"
    Y se registra un nuevo curso con código "CURSO-003", máximo de alumnos 25, materia "999-M" y cuatrimestres "2024-1"
    Cuando el alumno se inscribe al curso "CURSO-003" en el cuatrimestre actual
    Entonces no se puede realizar la inscripción exitosamente
    Y se le informa que las inscripciones están cerradas para este cuatrimestre

  Escenario: Inscribirse a un curso exitosamente con correlativa
    Cuando el alumno se inscribe al curso "CURSO-Correlativas" en el cuatrimestre actual
    Entonces la inscripción se realiza exitosamente

  Escenario: Inscribirse a un curso exitosamente con creditos
    Cuando el alumno se inscribe al curso "CURSO-que-necesita-creditos" en el cuatrimestre actual
    Entonces la inscripción se realiza exitosamente

  Escenario: Inscribirse a un curso sin los creditos requeridos falla
    Cuando el alumno se inscribe al curso "CURSO-que-necesita-un-millon-creditos" en el cuatrimestre actual
    Entonces no se puede realizar la inscripción exitosamente

  Escenario: Inscribirse a un curso de multiples correlativas sin una correlativa falla
    Cuando el alumno se inscribe al curso "CURSO-NECESITA 2 CORRELATIVAS" en el cuatrimestre actual
    Entonces no se puede realizar la inscripción exitosamente
