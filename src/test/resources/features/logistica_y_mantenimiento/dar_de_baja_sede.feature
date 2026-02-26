#language: es
@todo
Característica: Dar de baja sede
  # Como responsable de Logística y Mantenimiento quiero dar de baja una sede para asegurar que la estructura física registrada refleje únicamente sedes operativas.

  Escenario: : Dar de baja sede es exitoso
    Dado que existe una sede con "código de sede", "nombre", "dirección" y "teléfonos"
    Cuando se da de baja la sede
    Entonces se elimina el registro de la sede

  Escenario: : Dar de baja sede inexistente falla
    Dado que no existe una sede con código "SEDE-999"
    Cuando se intenta dar de baja la sede con código "SEDE-999"
    Entonces no se puede dar de baja la sede porque no existe

  Escenario: : Dar de baja sede con aulas activas falla
    Dado que existe una sede con aulas registradas
    Cuando se intenta dar de baja la sede
    Entonces no se puede dar de baja la sede porque tiene aulas activas

