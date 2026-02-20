#language: es
@todo
Característica: Asignar curso a cuatrimestre
#  Como responsable de Secretaría de Planificación quiero asignar un curso a un cuatrimestre para que los alumnos lo puedan cursar

  Escenario: : Asignar curso a cuatrimestre es exitoso
    Dado existe un curso con "cod. curso"
    Y existe un cuatrimestre con "cod. de cuatrimestre"
    Cuando asigna el curso al cuatrimestre
    Entonces se vincula el curso para cursarse en el cuatrimestre

