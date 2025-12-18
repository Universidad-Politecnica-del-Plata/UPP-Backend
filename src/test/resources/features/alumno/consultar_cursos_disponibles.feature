#language: es
Característica: Consultar cursos disponibles

  Antecedentes:
    Dado que hay un gestor academico logueado
    Y que existe una materia con el código de materia "997-M" y nombre "Analisis I"
    Y que existe una materia con el código de materia "998-M" y nombre "Algebra I"
    Y que existe una materia con el código de materia "999-M" y nombre "Algoritmos y Programacion I"
    Y se registra un nuevo plan de estudios con codigo "P1-2025", fecha de entrada en vigencia "01-01-2025", fecha de vencimiento "31-12-9999", materias en el plan "997-M", "998-M" y "999-M" y total de créditos optativos 20
    Y que hay un gestor de planificacion logueado
    Y se registra un nuevo cuatrimestre con código "2025-2", fecha de inicio de clases "2025-02-01", fecha de fin de clases "9999-07-15", fecha de inicio de inscripción "2025-01-01", fecha de fin de inscripción "2025-01-31", fecha de inicio de integradores "9999-07-16" y fecha de fin de integradores "9999-07-31"

  Escenario: Consultar cursos disponibles para un plan de estudios es exitoso
    Dado se registra un nuevo curso con código "CURSO-001", máximo de alumnos 25, materia "997-M" y cuatrimestres "2025-2"
    Y se registra un nuevo curso con código "CURSO-002", máximo de alumnos 25, materia "998-M" y cuatrimestres "2025-2"
    Y se registra un nuevo curso con código "CURSO-003", máximo de alumnos 25 y materia "999-M"
    Cuando consulta los cursos disponibles para el plan de estudios "P1-2025"
    Entonces se le informan 2 cursos disponibles

  Escenario: Consultar cursos disponibles con plan de estudios inexistente fracasa
    Cuando consulta los cursos disponibles para el plan de estudios "PLAN-INEXISTENTE"
    Entonces no se obtienen cursos disponibles exitosamente

  Escenario: Consultar cursos disponibles para cuatrimestre inexistente retorna lista vacía
    Dado se da de baja el cuatrimestre con código "2025-2"
    Cuando consulta los cursos disponibles para el plan de estudios "P1-2025"
    Entonces se le informan 0 cursos disponibles

  Escenario: Consultar cursos disponibles en cuatrimestre que no tiene cursos asignados retorna lista vacía
    Cuando consulta los cursos disponibles para el plan de estudios "P1-2025"
    Entonces se le informan 0 cursos disponibles