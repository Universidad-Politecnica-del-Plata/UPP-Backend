#language: es
@todo
Característica: Dar de alta aula en sede
  # Como responsable de Logística y Mantenimiento quiero registrar un aula en una sede para poder gestionarla y asignarla a actividades académicas.

  Escenario: Dar de alta un aula en una sede exitoso
    Dado que existe una sede con "código de sede", "nombre", "dirección" y "teléfonos"
    Cuando se da de alta una nueva aula con "id de aula", "sede a la que pertenece", "capacidad" y si "permite hibrido"
    Entonces se registra una nueva aula.

  Escenario: Dar de alta aula en sede inexistente falla
    Dado que no existe una sede con código "SEDE-999"
    Cuando se intenta dar de alta una nueva aula en la sede "SEDE-999"
    Entonces no se registra el aula porque la sede no existe

  Escenario: Dar de alta aula con id duplicado falla
    Dado que existe una sede con código "SEDE-001"
    Y existe un aula con id "AULA-001" en la sede "SEDE-001"
    Cuando se intenta dar de alta un aula con id "AULA-001" en la sede "SEDE-001"
    Entonces no se registra el aula porque el id ya existe

