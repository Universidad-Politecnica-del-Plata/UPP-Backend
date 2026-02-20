#language: es
@todo
Característica: Dar de baja sede
  # Como responsable de Logística y Mantenimiento quiero dar de baja una sede para mantener la información actualizada sobre las instalaciones de la Universidad

  Escenario: : Dar de baja sede es exitoso
    Dado que existe un responsable de Logística y Mantenimiento
    Y existe una sede con "código de sede", "nombre", "dirección" y "teléfonos"
    Cuando se da de baja la sede
    Entonces se elimina el registro de la sede

