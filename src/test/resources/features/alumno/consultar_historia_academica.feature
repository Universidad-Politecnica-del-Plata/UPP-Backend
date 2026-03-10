#language: es
Característica: Consultar historia académica

  #Como alumno quiero consultar mi historia académica para saber qué materias puedo cursar
  Antecedentes:
    Dado que existe una materia con el código de materia "997-MA" y nombre "Analisis I"
    Y que existe una materia con el código de materia "998-MA" y nombre "Algebra I"
    Y se registra un nuevo cuatrimestre con código "2025-2", fecha de inicio de clases "9999-02-01", fecha de fin de clases "9999-07-15", fecha de inicio de inscripción "2025-01-01", fecha de fin de inscripción "9999-01-31", fecha de inicio de integradores "9999-07-16" y fecha de fin de integradores "9999-07-31"
    Y se registra un nuevo curso con código "CURSO-001", máximo de alumnos 25, materia "997-MA" y cuatrimestres "2025-2"
    Y se registra un nuevo curso con código "CURSO-002", máximo de alumnos 25, materia "998-MA" y cuatrimestres "2025-2"
    Y registra un nuevo alumno con DNI 12345678, apellido "Perez", nombre "Juan", direccion "Calle Falsa 123", telefono "1234", email "juan@mail.com", fecha de nacimiento "01-01-1990" y fecha de ingreso "01-03-2015"
    Y el docente abre un acta de "Final" para el curso "CURSO-001"
    Y el docente abre un acta de "Final" para el curso "CURSO-002"


  Escenario: Consultar historia académica exitoso
    Dado que hay un alumno logueado con username "12345678", password "12345678"
    Y el alumno se inscribe al curso "CURSO-001" en el cuatrimestre actual
    Y el alumno se inscribe al curso "CURSO-002" en el cuatrimestre actual
    Y el docente carga la nota de un alumno con dni 12345678 y nota 8 para el curso "CURSO-001"
    Y el docente carga la nota de un alumno con dni 12345678 y nota 7 para el curso "CURSO-002"
    Y que hay un alumno logueado con username "12345678", password "12345678"
    Cuando consulta su historia académica
    Entonces se informa materia aprobada "Analisis I" con nota 8
    Y se informa materia aprobada "Algebra I" con nota 7

  Escenario: Consultar historia académica sin materias aprobadas
    Dado que existe un alumno con nombre "Maria", apellido "Lopez" y DNI "87654321"
    Cuando consulta su historia académica
    Entonces se le informa que no tiene materias aprobadas