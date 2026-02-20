#language: es
@todo
Característica: Actualizar estado de factura
#Como responsable de Administración y Cobranzas quiero actualizar el estado de una factura para registrar los pagos de los alumnos
  Escenario: : Actualizar estado de factura es exitoso
    Dado existe una factura de un alumno
    Y la factura tiene estado "Impaga"
    Cuando se actualiza el estado de la factura
    Entonces la factura queda con estado "Pagada"