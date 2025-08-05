#language: es
Característica: Dar de baja carrera
  Antecedentes:
    Dado que hay un gestor academico logueado
    Y que existe una materia con el código de materia "CAR997-M" y nombre "Analisis I"
    Y que existe una materia con el código de materia "CAR998-M" y nombre "Algebra I"
    Y que existe una materia con el código de materia "CAR999-M" y nombre "Algoritmos y Programacion I"
    Y que existe un plan de estudios con codigo "PCAR1-2025", fecha de entrada en vigencia "01-01-2025", fecha de vencimiento "31-12-2035", materias en el plan "CAR997-M", "CAR998-M" y "CAR999-M" y total de créditos optativos 20
    Y se registra una nueva carrera con codigo "ING-SIS", nombre "Ingeniería en Sistemas", titulo "Ingeniero en Sistemas", incumbencias "Desarrollo de software, análisis de sistemas" y planes de estudio "PCAR1-2025"

  Escenario: Dar de baja carrera con planes de estudio es exitoso
    Cuando se da de baja la carrera con codigo "ING-SIS"
    Entonces se elimina el registro de la carrera "ING-SIS" exitosamente

  Escenario: Dar de baja carrera inexistente fracasa
    Cuando se da de baja la carrera con codigo "ING-INEXISTENTE"
    Entonces no se elimina el registro de la carrera exitosamente
