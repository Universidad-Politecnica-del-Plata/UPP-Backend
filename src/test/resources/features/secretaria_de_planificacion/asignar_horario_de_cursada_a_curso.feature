#language: es
@todo
Característica: Asignar horario de cursada a curso
#Como responsable de Secretaría de Planificación quiero asignar un horario de cursada a un curso para representar el cronograma
  Escenario: : Asignar horario de cursada a curso es exitoso
    Dado existe un curso con "cod. curso" vinculado a un cuatrimestre
    Cuando asigna un horario de cursada al curso con "horario", "dias" y "modalidad"
    Entonces se registra la asignacion de horarios

  Escenario: : Asignar horario a curso no vinculado a cuatrimestre falla
    Dado existe un curso con "cod. curso" sin vinculo a cuatrimestre
    Cuando se intenta asignar un horario de cursada al curso
    Entonces no se asigna el horario porque el curso no está vinculado a un cuatrimestre

  Escenario: : Asignar horario con conflicto de aula falla
    Dado existe un curso con "cod. curso" vinculado a un cuatrimestre
    Y el aula asignada ya tiene un horario ocupado en ese rango
    Cuando se intenta asignar el mismo horario al curso
    Entonces no se asigna el horario porque hay conflicto con otra clase

