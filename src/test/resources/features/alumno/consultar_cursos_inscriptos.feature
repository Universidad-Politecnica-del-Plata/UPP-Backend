#language: es
Característica: Consultar cursos inscriptos
  Antecedentes:
    Dado que existe una materia con el código de materia "997-MA" y nombre "Analisis I"
    Y se registra un nuevo cuatrimestre con código "2025-2", fecha de inicio de clases "9999-02-01", fecha de fin de clases "9999-07-15", fecha de inicio de inscripción "2025-01-01", fecha de fin de inscripción "9999-01-31", fecha de inicio de integradores "9999-07-16" y fecha de fin de integradores "9999-07-31"
    Y se registra un nuevo curso con código "CURSO-001", máximo de alumnos 25, materia "997-MA" y cuatrimestres "2025-2"


  #Como alumno quiero consultar en qué cursos estoy inscripto para organizar mi cronograma academico

  Escenario: Consultar cursos inscriptos exitoso
    Dado que existe un alumno con nombre "Juan", apellido "Perez" y DNI "12345678"
    Y el alumno se inscribe al curso "CURSO-001" en el cuatrimestre actual
    Cuando consulta los cursos en que esta inscripto
    Entonces se informa que se inscribio al curso "CURSO-001" en el cuatrimestre "2025-2"

  Escenario: Consultar cursos inscriptos sin estar inscripto a ningún curso
    Dado que existe un alumno con nombre "Maria", apellido "Garcia" y DNI "87654321"
    Cuando consulta los cursos en que esta inscripto
    Entonces se le informa que no esta inscripto en ningún curso