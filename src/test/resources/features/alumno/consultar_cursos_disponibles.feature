#language: es
@todo
Característica: Consultar cursos disponibles

  Escenario: : Consultar cursos disponibles exitoso
    Dado que existe un alumno con nombre "Juan", apellido "Perez" y DNI "12345678"
    Y esta inscripto en la carrera "Ing. Informatica"
    Y existen cursos disponibles para cursar en el cuatrimestre
    Cuando consulta los cursos disponibles en el cuatrimestre
    Entonces se informa "horario de cursada", "modalidad", "créditos que otorga", "créditos requeridos" y "aula" de cada curso que tengo habilitado