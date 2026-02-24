#language: es
@todo
Característica: Asignar aula a curso
#Como responsable de Secretaría de Planificación quiero asignar un aula a un curso para que las clases presenciales e hibridas se puedan llevar a cabo.
  Escenario: : Asignar aula a curso es exitoso
    Dado existe un curso con "cod. curso" con modalidad "presencial" o "hibrida"
    Y existe un aula con "id de aula"
    Cuando asigna el aula al curso
    Entonces se registra la asignacion de aula

  Escenario: : Asignar aula a curso virtual falla
    Dado existe un curso con modalidad "virtual"
    Y existe un aula con "id de aula"
    Cuando se intenta asignar el aula al curso
    Entonces no se asigna el aula porque el curso es virtual

  Escenario: : Asignar aula inexistente a curso falla
    Dado existe un curso con "cod. curso" con modalidad "presencial"
    Y no existe un aula con id "AULA-999"
    Cuando se intenta asignar el aula "AULA-999" al curso
    Entonces no se asigna el aula porque no existe

