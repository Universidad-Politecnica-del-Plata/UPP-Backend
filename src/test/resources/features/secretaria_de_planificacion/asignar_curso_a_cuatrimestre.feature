#language: es
@todo
Característica: Asignar curso a cuatrimestre

  Escenario: : Asignar curso a cuatrimestre es exitoso
    Dado que existe un responsable de Secretaría de Planificación
    Y existe un curso con "cod. curso"
    Y existe un cuatrimestre con "cod. de cuatrimestre"
    Cuando asigna el curso al cuatrimestre
    Entonces se vincula el curso para cursarse en el cuatrimestre

