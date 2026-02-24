#language: es
@todo
Característica: Registrar sede
#Como responsable de Logística y Mantenimiento quiero registrar una nueva sede para mantener la información actualizada sobre las instalaciones de la Universidad
  Escenario: : Registrar sede es exitoso
    Dado que no existe una sede con "codigo de sede"
    Cuando registra una nueva sede con "código de sede", "nombre", "dirección" y "teléfonos"
    Entonces se registra la sede

  Escenario: : Registrar sede con código duplicado falla
    Dado que existe una sede con código "SEDE-001"
    Cuando se intenta registrar una nueva sede con código "SEDE-001"
    Entonces no se registra la sede porque el código ya existe

  Escenario: : Registrar sede con datos incompletos falla
    Dado que no existe una sede con código "SEDE-002"
    Cuando se intenta registrar una nueva sede sin dirección
    Entonces no se registra la sede por datos incompletos

