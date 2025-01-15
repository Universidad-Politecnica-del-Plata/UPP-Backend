#language: es
Característica: Consultar cursos de materia

  Escenario: : Consultar cursos de materia es exitoso
    Dado que existe un alumno con nombre "Juan", apellido "Perez" y DNI "12345678"
    Y esta inscripto en la carrera "Ing. Informatica"
    Y existen cursos para la materia "Fisica II"
    Cuando consulta todos los cursos de la materia "Fisica II"
    Entonces se le informa curso "Curso 1", horario de cursada "Lunes-Martes 18:00-21:00", modalidad "Presencial", créditos que otorga 8, créditos requeridos 0 y aula "Aula 21"
    Y se le informa curso "Curso 2", horario de cursada "Lunes-Viernes 18:00-21:00", modalidad "Virtual", créditos que otorga 8, créditos requeridos 0 y aula ""