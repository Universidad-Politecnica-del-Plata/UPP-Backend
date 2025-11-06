#language: es
Característica: Inscribirse a un curso

  Antecedentes:
    Dado que hay un gestor academico logueado
    Y que existe una materia con el código de materia "997-MA" y nombre "Analisis I"
    Y que existe una materia con el código de materia "998-MA" y nombre "Algebra I"
    Y que existe una materia con el código de materia "999-MA" y nombre "Algoritmos y Programacion I"
    Y se registra un nuevo plan de estudios con codigo "P1-2025", fecha de entrada en vigencia "01-01-2025", fecha de vencimiento "31-12-9999", materias en el plan "997-M", "998-M" y "999-M" y total de créditos optativos 20
    Y que hay un gestor de planificacion logueado
    Y se registra un nuevo cuatrimestre con código "2025-2", fecha de inicio de clases "9999-02-01", fecha de fin de clases "9999-07-15", fecha de inicio de inscripción "2025-01-01", fecha de fin de inscripción "9999-01-31", fecha de inicio de integradores "9999-07-16" y fecha de fin de integradores "9999-07-31"
    Y se registra un nuevo curso con código "CURSO-001", máximo de alumnos 25, materia "997-MA" y cuatrimestres "2025-2"
    Y se registra un nuevo curso con código "CURSO-002", máximo de alumnos 25, materia "998-MA" y cuatrimestres "2025-2"
    Y que hay un gestor estudiantil logueado
    Y registra un nuevo alumno con DNI 12345678, apellido "Perez", nombre "Juan", direccion "Calle Falsa 123", telefono "1234", email "juan@mail.com", fecha de nacimiento "01-01-1990" y fecha de ingreso "01-03-2015"
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

