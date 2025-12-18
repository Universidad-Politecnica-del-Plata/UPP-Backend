#language: es
Característica: Dar de baja curso
  Antecedentes:
    Dado que hay un gestor academico logueado
    Y que existe una materia con el código de materia "125-M" y nombre "Cálculo I"
    Y que existe una materia con el código de materia "126-M" y nombre "Física I"
    Y que hay un gestor de planificacion logueado
    Y que existe un cuatrimestre con código "2024-1"
    Y que existe un cuatrimestre con código "2024-2"

  Escenario: Dar de baja curso sin cuatrimestres es exitoso
    Dado se registra un nuevo curso con código "CURSO-DELETE-01", máximo de alumnos 25 y materia "125-M"
    Cuando se da de baja el curso con código "CURSO-DELETE-01"
    Entonces no existe el curso "CURSO-DELETE-01" en el registro

  Escenario: Dar de baja curso con cuatrimestres es exitoso
    Dado se registra un nuevo curso con código "CURSO-DELETE-02", máximo de alumnos 30, materia "126-M" y cuatrimestres "2024-1"
    Cuando se da de baja el curso con código "CURSO-DELETE-02"
    Entonces no existe el curso "CURSO-DELETE-02" en el registro
    Y los cuatrimestres "2024-1" ya no tienen el curso "CURSO-DELETE-02" asignado

  Escenario: Dar de baja curso con múltiples cuatrimestres es exitoso
    Dado se registra un nuevo curso con código "CURSO-DELETE-03", máximo de alumnos 35, materia "125-M" y cuatrimestres "2024-1,2024-2"
    Cuando se da de baja el curso con código "CURSO-DELETE-03"
    Entonces no existe el curso "CURSO-DELETE-03" en el registro
    Y los cuatrimestres "2024-1,2024-2" ya no tienen el curso "CURSO-DELETE-03" asignado

  Escenario: Dar de baja curso que no existe lanza error
    Dado se registra un nuevo curso con código "CURSO-DELETE-04", máximo de alumnos 30 y materia "126-M"
    Cuando se da de baja el curso con código "CURSO-INEXISTENTE"
    Entonces no se elimina el curso y se lanza error
