#language: es
@todo
Característica: Dar de baja aula
  # Como responsable de Logística y Mantenimiento quiero dar de baja una aula para mantener la información actualizada sobre las instalaciones de la sede

  Escenario: : Dar de baja aula es exitoso
    Dado que existe un aula con "id de aula", "sede" a la que pertenece, "capacidad" y si "permite hibrido"
    Cuando se da de baja el aula
    Entonces se elimina el registro del aula

  Escenario: : Dar de baja aula inexistente falla
    Dado que no existe un aula con id "AULA-999"
    Cuando se intenta dar de baja el aula con id "AULA-999"
    Entonces no se puede dar de baja el aula porque no existe

  Escenario: : Dar de baja aula con cursos asignados falla
    Dado que existe un aula con cursos asignados en el cuatrimestre actual
    Cuando se intenta dar de baja el aula
    Entonces no se puede dar de baja el aula porque tiene cursos asignados

