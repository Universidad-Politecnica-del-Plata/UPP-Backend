#language: es
@todo
Característica: Enviar factura a alumno
#  Como responsable de Administración y Cobranzas quiero enviarle una factura a un alumno para que este pueda pagarla
  Escenario: : Enviar factura a alumno es exitoso
    Dado que existe un alumno
    Cuando envía una factura para un alumno con "id", "fecha de emisión", "fecha de vencimiento", "número correlativo", "datos del alumno", "datos de la cuota", "recargo por facturas fuera de término" y "monto total"
    Entonces el alumno recibe la factura