#language: es
@todo
Característica: Dar de baja aula
  # Como responsable de Logística y Mantenimiento quiero dar de baja una aula para mantener la información actualizada sobre las instalaciones de la sede

  Escenario: : Dar de baja aula es exitoso
    Dado que existe un responsable de Logística y Mantenimiento
    Y existe un aula con "id de aula", "sede" a la que pertenece, "capacidad" y si "permite hibrido"
    Cuando se da de baja el aula
    Entonces se elimina el registro del aula

