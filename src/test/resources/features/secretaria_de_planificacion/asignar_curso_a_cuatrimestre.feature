#language: es
@todo
Característica: Asignar curso a cuatrimestre
#  Como responsable de Secretaría de Planificación quiero asignar un curso a un cuatrimestre para que los alumnos lo puedan cursar

  Escenario: : Asignar curso a cuatrimestre es exitoso
    Dado existe un curso con "cod. curso"
    Y existe un cuatrimestre con "cod. de cuatrimestre"
    Cuando asigna el curso al cuatrimestre
    Entonces se vincula el curso para cursarse en el cuatrimestre

  Escenario: : Asignar curso a cuatrimestre inexistente falla
    Dado existe un curso con "cod. curso"
    Y no existe un cuatrimestre con código "2099-1"
    Cuando se intenta asignar el curso al cuatrimestre "2099-1"
    Entonces no se asigna el curso porque el cuatrimestre no existe

  Escenario: : Asignar curso inexistente a cuatrimestre falla
    Dado no existe un curso con código "CURSO-999"
    Y existe un cuatrimestre con "cod. de cuatrimestre"
    Cuando se intenta asignar el curso "CURSO-999" al cuatrimestre
    Entonces no se asigna el curso porque no existe

