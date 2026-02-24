#language: es
@todo
Característica: Consultar todas las facturas
#Como alumno quiero consultar todas mis facturas para conocer mi historial de pagos
  Escenario: : Consultar todas las facturas es exitoso
    Dado que existe un alumno con nombre "Juan", apellido "Perez" y DNI "12345678"
    Y esta inscripto en la carrera "Ing. Informatica"
    Y tiene facturas emitidas a su nombre
    Cuando consulta todas sus facturas
    Entonces se le informa fecha de emisión "31/03/2025", fecha de vencimiento "15/04/2025", número correlativo 12345, nombre "Juan", apellido "Perez",DNI "12345678", datos de la cuota, recargo por pagos previos fuera de término e importe final 250000
    Entonces se le informa fecha de emisión "31/04/2025", fecha de vencimiento "15/05/2025", número correlativo 65884, nombre "Juan", apellido "Perez",DNI "12345678", datos de la cuota, recargo por pagos previos fuera de término e importe final 250000

  Escenario: : Consultar todas las facturas sin facturas emitidas
    Dado que existe un alumno con nombre "Maria", apellido "Lopez" y DNI "87654321"
    Y esta inscripto en la carrera "Ing. Informatica"
    Y no tiene facturas emitidas a su nombre
    Cuando consulta todas sus facturas
    Entonces se le informa que no tiene facturas emitidas

