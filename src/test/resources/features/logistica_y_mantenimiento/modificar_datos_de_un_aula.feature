#language: es
@todo
Característica: Modificar datos de un aula
#Como responsable de Logística y Mantenimiento quiero modificar datos de un aula para mantener la información actualizada sobre las instalaciones de la sede
  Escenario: : Modificar datos de un aula exitoso
    Dado que existe un aula con "id de aula", "sede a la que pertenece", "capacidad" y si "permite hibrido"
    Cuando modifica la "sede", "capacidad" o si "permite hibrido" del aula
    Entonces se actualiza la información del aula.

  Escenario: : Modificar datos de aula inexistente falla
    Dado que no existe un aula con id "AULA-999"
    Cuando se intenta modificar los datos del aula con id "AULA-999"
    Entonces no se puede modificar el aula porque no existe

  Escenario: : Modificar aula con capacidad inválida falla
    Dado que existe un aula con id "AULA-001"
    Cuando se intenta modificar el aula con capacidad -1
    Entonces no se puede modificar el aula porque la capacidad es inválida

