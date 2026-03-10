#language: es
Característica: Consultar cursos de materia
#Como alumno quiero consultar los cursos disponibles en el cuatrimestre para elegir qué materias cursar

  Escenario: : Consultar cursos de materia es exitoso
    Dado que existe un alumno con nombre "Juan", apellido "Perez" y DNI "12345678"
    Y que existe una materia con el código de materia "997-MA" y nombre "Analisis I"
    Y se registra un nuevo cuatrimestre con código "2025-2", fecha de inicio de clases "9999-02-01", fecha de fin de clases "9999-07-15", fecha de inicio de inscripción "2025-01-01", fecha de fin de inscripción "9999-01-31", fecha de inicio de integradores "9999-07-16" y fecha de fin de integradores "9999-07-31"
    Y se registra un nuevo curso con código "CURSO-001", máximo de alumnos 25, materia "997-MA" y cuatrimestres "2025-2"
    Y se registra un nuevo curso con código "CURSO-002", máximo de alumnos 25, materia "997-MA" y cuatrimestres "2025-2"
    Cuando consulta todos los cursos de la materia "997-MA"
    Entonces se le informa curso con codigo de curso "CURSO-001"
    Y se le informa curso con codigo de curso "CURSO-002"

  Escenario: Consultar cursos de materia inexistente falla
    Dado que existe un alumno con nombre "Juan", apellido "Perez" y DNI "12345678"
    Cuando consulta todos los cursos de la materia "Materia Inexistente"
    Entonces se le informa que la materia no existe