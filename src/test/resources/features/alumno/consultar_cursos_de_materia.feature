#language: es
@todo
Característica: Consultar cursos de materia
#Como alumno quiero consultar los cursos disponibles en el cuatrimestre para elegir qué materias cursar
  Escenario: : Consultar cursos de materia es exitoso
    Dado que existe un alumno con nombre "Juan", apellido "Perez" y DNI "12345678"
    Y esta inscripto en la carrera "Ing. Informatica"
    Y existen cursos para la materia "Fisica II"
    Cuando consulta todos los cursos de la materia "Fisica II"
    Entonces se le informa curso "Curso 1", horario de cursada "Lunes-Martes 18:00-21:00", modalidad "Presencial", créditos que otorga 8, créditos requeridos 0 y aula "Aula 21"
    Y se le informa curso "Curso 2", horario de cursada "Lunes-Viernes 18:00-21:00", modalidad "Virtual", créditos que otorga 8, créditos requeridos 0 y aula ""

  Escenario: : Consultar cursos de materia inexistente falla
    Dado que existe un alumno con nombre "Juan", apellido "Perez" y DNI "12345678"
    Y esta inscripto en la carrera "Ing. Informatica"
    Cuando consulta todos los cursos de la materia "Materia Inexistente"
    Entonces se le informa que la materia no existe

  Escenario: : Consultar cursos de materia sin cursos disponibles
    Dado que existe un alumno con nombre "Juan", apellido "Perez" y DNI "12345678"
    Y esta inscripto en la carrera "Ing. Informatica"
    Y no existen cursos para la materia "Algebra III"
    Cuando consulta todos los cursos de la materia "Algebra III"
    Entonces se le informa que no hay cursos disponibles para la materia