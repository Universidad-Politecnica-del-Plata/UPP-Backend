#language: es
@todo
Característica: Modificar datos de un aula
#Como responsable de Logística y Mantenimiento quiero modificar datos de un aula para mantener la información actualizada sobre las instalaciones de la sede
  Escenario: : Modificar datos de un aula exitoso
    Dado que existe un responsable de Logística y Mantenimiento
    Y existe un aula
    Cuando modifica la "sede", "capacidad" o si "permite hibrido" del aula
    Entonces se actualiza la información del aula.

