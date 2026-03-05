#language: es
Característica: Consultar informacion de acta
#Como docente quiero consultar la información de un acta de un curso para corroborar que las notas cargadas sean correctas

  Antecedentes:
    Dado que existe una materia con el código de materia "997-MA" y nombre "Analisis I"
    Y se registra un nuevo cuatrimestre con código "2025-2", fecha de inicio de clases "9999-02-01", fecha de fin de clases "9999-07-15", fecha de inicio de inscripción "2025-01-01", fecha de fin de inscripción "9999-01-31", fecha de inicio de integradores "9999-07-16" y fecha de fin de integradores "9999-07-31"
    Y se registra un nuevo curso con código "CURSO-001", máximo de alumnos 25, materia "997-MA" y cuatrimestres "2025-2"
    Y se registra un nuevo curso con código "CURSO-002", máximo de alumnos 25, materia "997-MA" y cuatrimestres "2025-2"
    Y registra un nuevo alumno con DNI 56789, apellido "Perez", nombre "Juana", direccion "Calle Falsa 123", telefono "1234", email "juan2@mail.com", fecha de nacimiento "01-01-1990" y fecha de ingreso "01-03-2015"
    Y registra un nuevo alumno con DNI 12345678, apellido "Perez", nombre "Juan", direccion "Calle Falsa 123", telefono "1234", email "juan@mail.com", fecha de nacimiento "01-01-1990" y fecha de ingreso "01-03-2015"
    Y que hay un alumno logueado con username "12345678", password "12345678"
    Y el alumno se inscribe al curso "CURSO-001" en el cuatrimestre actual

  Escenario: Consultar informacion de acta exitoso
    Dado el docente abre un acta de "Cursada" para el curso "CURSO-001"
    Y el docente carga la nota de un alumno con dni 12345678 y nota 8 para el curso "CURSO-001"
    Cuando el docente consulta las actas del curso "CURSO-001"
    Entonces se le informa que el acta tiene numero correlativo
    Y se le informa que el tipo de acta es "Cursada"
    Y se le informa que el estado del acta es "ABIERTA"
    Y se le informa que el codigo del curso es "CURSO-001"
    Y se le informa una nota con nombre "Juan", apellido "Perez" y valor 8

  Escenario: Consultar actas de curso sin actas retorna lista vacia
    Dado el docente abre un acta de "Cursada" para el curso "CURSO-001"
    Cuando el docente consulta las actas del curso "CURSO-002"
    Entonces se le informa que no hay actas para el curso

  Escenario: Consultar actas de curso inexistente falla
    Cuando el docente consulta las actas del curso "CURSO-INEXISTENTE"
    Entonces se le informa que el curso buscado no existe
