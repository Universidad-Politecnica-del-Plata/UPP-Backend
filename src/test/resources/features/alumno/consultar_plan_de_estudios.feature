#language: es
Característica: Consultar plan de estudios

  #Como alumno quiero consultar mi plan de estudios para saber el orden de las materias

  Antecedentes:
    Dado que existe una materia con el código de materia "997-M" y nombre "Analisis I"
    Y que existe una materia con el código de materia "998-M" y nombre "Algebra I"
    Y que existe una materia con el código de materia "999-M" y nombre "Algoritmos y Programacion I"
    Y se registra un nuevo plan de estudios con codigo "P1-2025", fecha de entrada en vigencia "01-01-2025", fecha de vencimiento "31-12-2035", materias en el plan "997-M", "998-M" y "999-M" y total de créditos optativos 20
    Y se registra una nueva carrera con codigo "ING-SIS", nombre "Ingeniería en Sistemas", titulo "Ingeniero en Sistemas", incumbencias "Desarrollo de software, análisis de sistemas" y planes de estudio "P1-2025"


  Escenario: : Consultar plan de estudios es exitoso
    Dado que existe un alumno con nombre "Juan", apellido "Perez" y DNI "12345678"
    Y esta inscripto en la carrera "ING-SIS"
    Cuando consulta su plan de estudios
    Entonces se le informa que el codigo del plan es "P1-2025"
    Y se le informa que la fecha de entrada en vigencia es "01-01-2025"
    Y se le informa que la fecha de vencimiento es "31-12-2035"
    Y se le informa que el codigo de carrera es "ING-SIS"
    Y se le informa que la materia "997-M" esta en el plan
    Y se le informa que la materia "998-M" esta en el plan
    Y se le informa que la materia "999-M" esta en el plan
    Y se le informa que el plan tiene 20 creditos optativos