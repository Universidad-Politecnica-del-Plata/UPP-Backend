#language: es
@todo
Característica: Dar de baja curso

  Escenario: : Dar de baja curso es exitoso
    Dado que existe un responsable de Secretaría de Planificación
    Y existe un curso con "cod.curso" para la materia "cod materia"
    Cuando se da de baja el curso
    Entonces se elimina el registro del curso
