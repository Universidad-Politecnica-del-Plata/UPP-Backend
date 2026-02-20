#language: es
@todo
Característica: Asignar aula a curso
#Como responsable de Secretaría de Planificación quiero asignar un aula a un curso para que las clases presenciales e hibridas se puedan llevar a cabo.
  Escenario: : Asignar aula a curso es exitoso
    Dado que existe un responsable de Secretaría de Planificación
    Y existe un curso con "cod. curso" con modalidad "presencial" o "hibrida"
    Y existe un aula con "id de aula"
    Cuando asigna el aula al curso
    Entonces se registra la asignacion de aula

