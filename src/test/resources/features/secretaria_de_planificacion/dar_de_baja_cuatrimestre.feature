#language: es
Característica: Dar de baja cuatrimestre

  Escenario: : Dar de baja cuatrimestre es exitoso
    Dado que existe un responsable de Secretaría de Planificación
    Y existe un cuatrimestre con "cod. de cuatrimestre", "fecha de inicio", "fecha de fin", "periodo de inscripcion" y "periodo de examenes integradores"
    Cuando se da de baja el cuatrimestre
    Entonces se elimina el registro del cuatrimestre