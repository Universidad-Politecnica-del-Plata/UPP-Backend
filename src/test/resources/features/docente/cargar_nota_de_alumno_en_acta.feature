#language: es

Característica: Cargar nota de alumno a un acta
  Antecedentes:
    Dado que hay un gestor academico logueado
    Y que existe una materia con el código de materia "997-MA" y nombre "Analisis I"
    Y que hay un gestor de planificacion logueado
    Y se registra un nuevo cuatrimestre con código "2025-2", fecha de inicio de clases "9999-02-01", fecha de fin de clases "9999-07-15", fecha de inicio de inscripción "2025-01-01", fecha de fin de inscripción "9999-01-31", fecha de inicio de integradores "9999-07-16" y fecha de fin de integradores "9999-07-31"
    Y se registra un nuevo curso con código "CURSO-001", máximo de alumnos 25, materia "997-MA" y cuatrimestres "2025-2"
    Y que hay un gestor estudiantil logueado
    Y registra un nuevo alumno con DNI 56789, apellido "Perez", nombre "Juana", direccion "Calle Falsa 123", telefono "1234", email "juan2@mail.com", fecha de nacimiento "01-01-1990" y fecha de ingreso "01-03-2015"
    Y registra un nuevo alumno con DNI 12345678, apellido "Perez", nombre "Juan", direccion "Calle Falsa 123", telefono "1234", email "juan@mail.com", fecha de nacimiento "01-01-1990" y fecha de ingreso "01-03-2015"
    Y que hay un alumno logueado con username "12345678", password "12345678"
    Y el alumno se inscribe al curso "CURSO-001" en el cuatrimestre actual
    Y que hay un docente logueado
    Y el docente abre un acta de "Cursada" para el curso "CURSO-001"

  Escenario: Cargar nota aprobatoria de alumno exitoso
    Cuando el docente carga la nota de un alumno con dni 12345678 y nota 8
    Entonces se guarda la información en el acta

  Escenario: Cargar nota desaprobatoria de alumno lanza excepcion
    Cuando el docente carga la nota de un alumno con dni 12345678 y nota 2
    Entonces no se guarda la información en el acta

  Escenario: Cargar nota de mayor a 10 alumno lanza excepcion
    Cuando el docente carga la nota de un alumno con dni 12345678 y nota 12
    Entonces no se guarda la información en el acta


  Escenario: Intenta cargar nota en acta inexistente
    Cuando el docente intenta cargar nota para un acta inexistente con numero correlativo 9999
    Entonces no se puede cargar la nota por acta inexistente

  Escenario: Intenta cargar nota en acta cerrada
    Dado que el acta está cerrada
    Cuando el docente carga la nota de un alumno con dni 12345678 y nota 8
    Entonces no se puede cargar la nota por acta cerrada

  Escenario: Intenta cargar nota para alumno inexistente
    Cuando el docente carga la nota de un alumno con dni 99999999 y nota 8
    Entonces no se puede cargar la nota por alumno inexistente

  Escenario: Intenta cargar nota para alumno no inscrito
    Cuando el docente carga la nota de un alumno con dni 56789 y nota 8
    Entonces no se puede cargar la nota por alumno no inscrito
    