#language: es
@todo
Característica: Consultar detalles de carrera

  Escenario: : Consultar detalles de carrera es exitoso
  Dado que existe un alumno con nombre "Juan", apellido "Perez" y DNI "12345678"
  Y esta inscripto en la carrera "Ing. Informatica"
  Cuando consulta la información de su carrera
  Entonces se le informa "Ingeniero en Informatica", "II123", "incumbencias" y "Ing. Informatica"