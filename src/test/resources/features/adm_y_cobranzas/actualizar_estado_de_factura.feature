#language: es
@todo
Característica: Actualizar estado de factura
#Como responsable de Administración y Cobranzas quiero actualizar el estado de una factura para registrar los pagos de los alumnos
  Escenario: : Actualizar estado de factura es exitoso
    Dado existe una factura de un alumno
    Y la factura tiene estado "Impaga"
    Cuando se actualiza el estado de la factura
    Entonces la factura queda con estado "Pagada"

  Escenario: : Actualizar estado de factura inexistente falla
    Dado que no existe una factura con id 99999
    Cuando se intenta actualizar el estado de la factura con id 99999
    Entonces no se puede actualizar la factura

  Escenario: : Actualizar estado de factura ya pagada falla
    Dado existe una factura de un alumno
    Y la factura tiene estado "Pagada"
    Cuando se intenta actualizar el estado de la factura
    Entonces no se puede actualizar la factura porque ya está pagada