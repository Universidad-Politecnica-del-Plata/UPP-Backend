#language: es
Característica: Dar de alta curso
  Antecedentes:
    Dado que hay un gestor academico logueado
    Y que existe una materia con el código de materia "123-M" y nombre "Análisis I"
    Y que existe una materia con el código de materia "124-M" y nombre "Álgebra I"
    Y que hay un gestor de planificacion logueado
    Y que existe un cuatrimestre con código "2024-1"
    Y que existe un cuatrimestre con código "2024-2"

  Escenario: Dar de alta curso sin cuatrimestres es exitoso
    Cuando se registra un nuevo curso con código "CURSO-001", máximo de alumnos 30 y materia "123-M"
    Entonces se registra el curso "CURSO-001" exitosamente

  Escenario: Dar de alta curso con un cuatrimestre es exitoso
    Cuando se registra un nuevo curso con código "CURSO-002", máximo de alumnos 25, materia "123-M" y cuatrimestres "2024-1"
    Entonces se registra el curso "CURSO-002" exitosamente
    Y el curso "CURSO-002" está asignado al cuatrimestre "2024-1"

  Escenario: Dar de alta curso con múltiples cuatrimestres es exitoso
    Cuando se registra un nuevo curso con código "CURSO-003", máximo de alumnos 30, materia "124-M" y cuatrimestres "2024-1,2024-2"
    Entonces se registra el curso "CURSO-003" exitosamente
    Y el curso "CURSO-003" está asignado a los cuatrimestres "2024-1,2024-2"

  Escenario: Dar de alta curso con código ya en uso fracasa
    Dado se registra un nuevo curso con código "CURSO-001", máximo de alumnos 30 y materia "123-M"
    Cuando se registra un nuevo curso con código "CURSO-001", máximo de alumnos 25 y materia "124-M"
    Entonces no se registra el curso exitosamente

  Escenario: Dar de alta curso con materia inexistente fracasa
    Cuando se registra un nuevo curso con código "CURSO-004", máximo de alumnos 30 y materia "999-M"
    Entonces no se registra el curso exitosamente

  Escenario: Dar de alta curso con cuatrimestre inexistente fracasa
    Cuando se registra un nuevo curso con código "CURSO-005", máximo de alumnos 30, materia "123-M" y cuatrimestres "2099-1"
    Entonces no se registra el curso exitosamente

  Escenario: Dar de alta curso con máximo de alumnos inválido fracasa
    Cuando se registra un nuevo curso con código "CURSO-006", máximo de alumnos 0 y materia "123-M"
    Entonces no se registra el curso exitosamente

