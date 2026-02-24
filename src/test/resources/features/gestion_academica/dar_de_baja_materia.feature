#language: es

Característica: Dar de baja materia
#Como responsable de Gestión Academica quiero dar de baja una materia para eliminarla de las materias que se dictan en la Universidad
  Escenario: Dar de baja materia es exitoso
    Dado que existe una materia con el código de materia "123-M" y nombre "Analisis I"
    Cuando se da de baja la materia "123-M"
    Entonces no existe la materia "123-M" en el registro

  Escenario: : Dar de baja materia que no existe
    Dado que no existe una materia con el código de materia "123-B"
    Cuando se da de baja la materia "123-B"
    Entonces no se elimina la materia y se lanza error
