#language: es
Característica: Dar de baja sede

  Escenario: : Dar de baja sede es exitoso
    Dado que existe un responsable de Logística y Mantenimiento
    Y existe una sede con "código de sede", "nombre", "dirección" y "teléfonos"
    Cuando se da de baja la sede
    Entonces se elimina el registro de la sede

