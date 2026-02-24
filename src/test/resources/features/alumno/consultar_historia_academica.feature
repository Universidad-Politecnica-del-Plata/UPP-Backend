#language: es
@todo
Característica: Consultar historia académica

  #Como alumno quiero consultar mi historia académica para saber qué materias puedo cursar

  Escenario: : Consultar historia académica exitoso
    Dado que existe un alumno con nombre "Juan", apellido "Perez" y DNI "12345678"
    Y tiene "Fisica I", "Fisica II" aprobadas
    Cuando consulta su historia académica
    Entonces se informa el nombre "Fisica I", número de acta "123", la nota "7" y el curso "Curso 1"
    Y se informa el nombre "Fisica II", número de acta "1234", la nota "7" y el curso "Curso 1"

  Escenario: : Consultar historia académica sin materias aprobadas
    Dado que existe un alumno con nombre "Maria", apellido "Lopez" y DNI "87654321"
    Y no tiene materias aprobadas
    Cuando consulta su historia académica
    Entonces se le informa que no tiene materias aprobadas