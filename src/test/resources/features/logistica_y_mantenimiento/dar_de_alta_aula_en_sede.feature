#language: es
@todo
Característica: Dar de alta aula en sede
  # Como responsable de Logística y Mantenimiento quiero dar de alta un aula en una sede para mantener la información actualizada sobre las instalaciones de la sede

  Escenario: : Dar de alta un aula en una sede exitoso
    Dado que existe un responsable de Logística y Mantenimiento
    Y existe una sede
    Cuando se da de alta una nueva aula con "id de aula", "sede a la que pertenece", "capacidad" y si "permite hibrido"
    Entonces se registra una nueva aula.

