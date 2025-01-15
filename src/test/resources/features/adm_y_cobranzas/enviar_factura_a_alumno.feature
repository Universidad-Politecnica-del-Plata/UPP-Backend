#language: es
Característica: Enviar factura a alumno

  Escenario: : Enviar factura a alumno es exitoso
    Dado que existe un responsable de Administración y Cobranzas
    Y existe un alumno
    Cuando envía una factura para un alumno con "id", "fecha de emisión", "fecha de vencimiento", "número correlativo", "datos del alumno", "datos de la cuota", "recargo por facturas fuera de término" y "monto total"
    Entonces el alumno recibe la factura