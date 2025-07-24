#language: es
@todo
Característica: Modificar curso

  Escenario: : Modificar curso es exitoso
    Dado que existe un responsable de Secretaría de Planificación
    Y existe un curso con "codigo" con "cod. curso", "docentes a cargo", "cant max de alumnos"
    Cuando cuando se modifica el curso con "codigo" con "cod. curso", "docentes a cargo", "cant max de alumnos"
    Entonces se actualiza la informacion del curso

