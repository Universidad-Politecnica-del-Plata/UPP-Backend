#language: es
@todo
Característica: Consultar facturas impagas
#Como alumno quiero consultar mis facturas impagas para ponerme al día con los pagos
  Escenario: : Consultar facturas impagas es exitoso
    Dado que existe un alumno con nombre "Juan", apellido "Perez" y DNI "12345678"
    Y esta inscripto en la carrera "Ing. Informatica"
    Y tiene facturas impagas emitidas a su nombre
    Cuando consulta sus facturas impagas
    Entonces se le informa fecha de emisión "31/03/2025", fecha de vencimiento "15/04/2025", número correlativo 12345, nombre "Juan", apellido "Perez",DNI "12345678", datos de la cuota, recargo por pagos previos fuera de término e importe final 250000

  Escenario: : Consultar facturas impagas sin facturas pendientes
    Dado que existe un alumno con nombre "Juan", apellido "Perez" y DNI "12345678"
    Y esta inscripto en la carrera "Ing. Informatica"
    Y no tiene facturas impagas
    Cuando consulta sus facturas impagas
    Entonces se le informa que no tiene facturas impagas

