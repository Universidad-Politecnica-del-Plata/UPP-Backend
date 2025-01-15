#language: es
Característica: Modificar datos de una sede

  Escenario: : Modificar datos de una sede exitoso
    Dado que existe un responsable de Logística y Mantenimiento
    Y existe una sede
    Cuando se modifica el "código de sede", "nombre", "dirección" o "teléfonos" de la sede
    Entonces se actualiza la información de la sede

