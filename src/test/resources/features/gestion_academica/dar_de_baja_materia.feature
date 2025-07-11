#language: es
Característica: Dar de baja materia
  Antecedentes:
    Dado que hay un gestor academico logueado
  Escenario: : Dar de baja materia es exitoso
    Dado que existe una materia con el código de materia "123-M" y nombre "Analisis I"
    Cuando se da de baja la materia "123-M"
    Entonces no existe la materia "123-M" en el registro

  Escenario: : Dar de baja materia que no existe
    Cuando se da de baja la materia "123-B"
    Entonces no se elimina la materia y se lanza error
