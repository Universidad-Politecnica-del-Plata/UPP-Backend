#language: es
Característica: Actualizar estado de factura

  Escenario: : Actualizar estado de factura es exitoso
    Dado que existe un responsable de Administración y Cobranzas
    Y existe una factura de un alumno
    Y la factura tiene estado "Impaga"
    Cuando se actualiza el estado de la factura
    Entonces la factura queda con estado "Pagada"