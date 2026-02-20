#language: es
@todo
Característica: Consultar cursos inscriptos

  #Como alumno quiero consultar en qué cursos estoy inscripto para organizar mi cuatrimestre

  Escenario: : Consultar cursos inscriptos exitoso
    Dado que existe un alumno con nombre "Juan", apellido "Perez" y DNI "12345678"
    Y esta inscripto en el curso "1" de "Fisica I"
    Cuando consulta los cursos en que esta inscripto
    Entonces se informa "horario de cursada", "modalidad", "créditos que otorga", "créditos requeridos" y "aula" de cada curso