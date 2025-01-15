#language: es
Característica: Modificar datos de un aula

  Escenario: : Modificar datos de un aula exitoso
    Dado que existe un responsable de Logística y Mantenimiento
    Y existe un aula
    Cuando modifica la "sede", "capacidad" o si "permite hibrido" del aula
    Entonces se actualiza la información del aula.

