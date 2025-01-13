#language: es
Característica: Consultar cursos inscriptos

  Escenario: : Consultar cursos inscriptos exitoso
    Dado que existe un alumno con nombre "Juan", apellido "Perez" y DNI "12345678"
    Y esta inscripto en el curso "1" de "Fisica I"
    Cuando consulta los cursos en que esta inscripto
    Entonces se informa "horario de cursada", "modalidad", "créditos que otorga", "créditos requeridos" y "aula" de cada curso