#language: es
@todo
Característica: Modificar datos de una sede
#Como responsable de Logística y Mantenimiento quiero modificar datos de una sede para mantener la información actualizada sobre las instalaciones de la Universidad
  Escenario: : Modificar datos de una sede exitoso
    Dado que existe un responsable de Logística y Mantenimiento
    Y existe una sede
    Cuando se modifica el "código de sede", "nombre", "dirección" o "teléfonos" de la sede
    Entonces se actualiza la información de la sede

