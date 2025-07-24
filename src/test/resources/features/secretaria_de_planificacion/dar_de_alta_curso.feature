#language: es
@todo
Característica: Dar de alta curso

  Escenario: : Dar de alta curso es exitoso
    Dado que existe un responsable de Secretaría de Planificación
    Y existe una materia con "codigo"
    Cuando registra un nuevo curso para la materia de "codigo" con "cod. curso", "docentes a cargo", "cant max de alumnos"
    Entonces se registra el curso

