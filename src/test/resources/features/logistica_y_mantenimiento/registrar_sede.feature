#language: es
@todo
Característica: Registrar sede
#Como responsable de Logística y Mantenimiento quiero registrar una nueva sede para mantener la información actualizada sobre las instalaciones de la Universidad
  Escenario: : Registrar sede es exitoso
    Dado que no existe una sede con "codigo de sede"
    Cuando registra una nueva sede con "código de sede", "nombre", "dirección" y "teléfonos"
    Entonces se registra la sede

