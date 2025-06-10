#language: es
@todo
Característica: Asignar horario de cursada a curso

  Escenario: : Asignar horario de cursada a curso es exitoso
    Dado que existe un responsable de Secretaría de Planificación
    Y existe un curso con "cod. curso" vinculado a un cuatrimestre
    Cuando asigna un horario de cursada al curso con "horario", "dias" y "modalidad"
    Entonces se registra la asignacion de horarios

