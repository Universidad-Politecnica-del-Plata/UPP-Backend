#language: es
@todo
Característica: Consultar historia académica

  Escenario: : Consultar historia académica exitoso
    Dado que existe un alumno con nombre "Juan", apellido "Perez" y DNI "12345678"
    Y tiene "Fisica I", "Fisica II" aprobadas
    Cuando consulta su historia académica
    Entonces se informa el nombre "Fisica I", número de acta "123", la nota "7" y el curso "Curso 1"
    Y se informa el nombre "Fisica II", número de acta "1234", la nota "7" y el curso "Curso 1"