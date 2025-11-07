#language: es

Característica: Cerrar acta
  Antecedentes:
    Dado que hay un gestor academico logueado
    Y que existe una materia con el código de materia "999-MA" y nombre "Algoritmos y Programacion I"
    Y que hay un gestor de planificacion logueado
    Y se registra un nuevo cuatrimestre con código "2025-2", fecha de inicio de clases "9999-02-01", fecha de fin de clases "9999-07-15", fecha de inicio de inscripción "2025-01-01", fecha de fin de inscripción "9999-01-31", fecha de inicio de integradores "9999-07-16" y fecha de fin de integradores "9999-07-31"
    Y se registra un nuevo curso con código "CURSO-001", máximo de alumnos 25, materia "999-MA" y cuatrimestres "2025-2"
    Y que hay un docente logueado

  Escenario: Cerrar acta de Cursada es exitoso
    Dado hay un acta de "Cursada" abierta para el curso en el cuatrimiestre
    Cuando cierra un acta de "Cursada" para el curso en el cuatrimestre
    Entonces el acta queda cerrada

  Escenario: Cerrar acta de Final es exitoso
    Dado hay un acta de "Final" abierta para el curso en el cuatrimiestre
    Cuando cierra un acta de "Final" para el curso en el cuatrimestre
    Entonces el acta queda cerrada
