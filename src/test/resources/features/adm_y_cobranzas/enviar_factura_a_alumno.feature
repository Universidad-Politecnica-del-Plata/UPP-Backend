#language: es
@todo
Característica: Enviar factura a alumno
#  Como responsable de Administración y Cobranzas quiero emitir y enviar una factura a un alumno para registrar formalmente la obligación de pago
  Escenario: : Enviar factura a alumno es exitoso
    Dado que existe un alumno y una factura
    Cuando envía una factura para un alumno con "id", "fecha de emisión", "fecha de vencimiento", "número correlativo", "datos del alumno", "datos de la cuota", "recargo por facturas fuera de término" y "monto total"
    Entonces el alumno recibe la factura

  Escenario: : Enviar factura a alumno inexistente falla
    Dado que no existe un alumno con matrícula 99999
    Cuando se intenta enviar una factura al alumno con matrícula 99999
    Entonces no se puede enviar la factura porque el alumno no existe

  Escenario: : Enviar factura con datos incompletos falla
    Dado que existe un alumno y una factura
    Cuando se intenta enviar una factura sin fecha de vencimiento
    Entonces no se puede enviar la factura por datos incompletos