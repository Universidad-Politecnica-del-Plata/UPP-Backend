#language: es
@todo
Característica: Consultar facturas impagas

  Escenario: : Consultar facturas impagas es exitoso
    Dado que existe un alumno con nombre "Juan", apellido "Perez" y DNI "12345678"
    Y esta inscripto en la carrera "Ing. Informatica"
    Y tiene facturas impagas emitidas a su nombre
    Cuando consulta sus facturas impagas
    Entonces se le informa fecha de emisión "31/03/2025", fecha de vencimiento "15/04/2025", número correlativo 12345, nombre "Juan", apellido "Perez",DNI "12345678", datos de la cuota, recargo por pagos previos fuera de término e importe final 250000

