#language: es
Característica: Dar de baja aula

  Escenario: : Dar de baja aula es exitoso
    Dado que existe un responsable de Logística y Mantenimiento
    Y existe un aula con "id de aula", "sede" a la que pertenece, "capacidad" y si "permite hibrido"
    Cuando se da de baja el aula
    Entonces se elimina el registro del aula

