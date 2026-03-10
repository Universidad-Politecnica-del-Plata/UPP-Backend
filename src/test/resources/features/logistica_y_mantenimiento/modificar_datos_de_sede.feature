#language: es
@todo
Característica: Modificar datos de una sede
#Como responsable de Logística y Mantenimiento quiero modificar datos de una sede para garantizar la exactitud de la información sobre las instalaciones de la Universidad
  Escenario: : Modificar datos de una sede exitoso
    Dado que existe una sede con "código de sede", "nombre", "dirección" y "teléfonos"
    Cuando se modifica el "código de sede", "nombre", "dirección" o "teléfonos" de la sede
    Entonces se actualiza la información de la sede

  Escenario: : Modificar datos de sede inexistente falla
    Dado que no existe una sede con código "SEDE-999"
    Cuando se intenta modificar los datos de la sede con código "SEDE-999"
    Entonces no se puede modificar la sede porque no existe

  Escenario: : Modificar sede con código duplicado falla
    Dado que existe una sede con código "SEDE-001"
    Y existe otra sede con código "SEDE-002"
    Cuando se intenta modificar la sede "SEDE-001" con el código "SEDE-002"
    Entonces no se puede modificar la sede porque el código ya está en uso

