#language: es
@todo
Característica: Asignar horario de cursada a curso
#Como responsable de Secretaría de Planificación quiero asignar un horario de cursada a un curso para representar el cronograma
  Escenario: : Asignar horario de cursada a curso es exitoso
    Dado existe un curso con "cod. curso" vinculado a un cuatrimestre
    Cuando asigna un horario de cursada al curso con "horario", "dias" y "modalidad"
    Entonces se registra la asignacion de horarios

