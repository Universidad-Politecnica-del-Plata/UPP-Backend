#language: es
@todo
Característica: Modificar datos de un aula
#Como responsable de Logística y Mantenimiento quiero modificar datos de un aula para mantener la información actualizada sobre las instalaciones de la sede
  Escenario: : Modificar datos de un aula exitoso
    Dado que existe un aula con "id de aula", "sede a la que pertenece", "capacidad" y si "permite hibrido"
    Cuando modifica la "sede", "capacidad" o si "permite hibrido" del aula
    Entonces se actualiza la información del aula.

