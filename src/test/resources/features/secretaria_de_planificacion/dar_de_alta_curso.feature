#language: es
Característica: Dar de alta curso
  Antecedentes:
    Dado que hay un gestor academico logueado
    Y que existe una materia con el código de materia "123-M" y nombre "Análisis I"
    Y que existe una materia con el código de materia "124-M" y nombre "Álgebra I"
    Y que hay un gestor de planificacion logueado

  Escenario: Dar de alta curso es exitoso
    Cuando se registra un nuevo curso con código "CURSO-001", máximo de alumnos 30 y materia "123-M"
    Entonces se registra el curso "CURSO-001" exitosamente

  Escenario: Dar de alta curso con código ya en uso fracasa
    Dado se registra un nuevo curso con código "CURSO-001", máximo de alumnos 30 y materia "123-M"
    Cuando se registra un nuevo curso con código "CURSO-001", máximo de alumnos 25 y materia "124-M"
    Entonces no se registra el curso exitosamente

  Escenario: Dar de alta curso con materia inexistente fracasa
    Cuando se registra un nuevo curso con código "CURSO-002", máximo de alumnos 30 y materia "999-M"
    Entonces no se registra el curso exitosamente

  Escenario: Dar de alta curso con máximo de alumnos inválido fracasa
    Cuando se registra un nuevo curso con código "CURSO-003", máximo de alumnos 0 y materia "123-M"
    Entonces no se registra el curso exitosamente

