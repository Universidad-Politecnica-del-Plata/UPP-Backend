#language: es
Característica: Dar de alta carrera
  Antecedentes:
    Dado que hay un gestor academico logueado
    Y que existe una materia con el código de materia "CAR997-M" y nombre "Analisis I"
    Y que existe una materia con el código de materia "CAR998-M" y nombre "Algebra I"
    Y que existe una materia con el código de materia "CAR999-M" y nombre "Algoritmos y Programacion I"
    Y que existe un plan de estudios con codigo "PCAR1-2025", fecha de entrada en vigencia "01-01-2025", fecha de vencimiento "31-12-2035", materias en el plan "CAR997-M", "CAR998-M" y "CAR999-M" y total de créditos optativos 20
    Y que existe un plan de estudios con codigo "PCAR2-2025", fecha de entrada en vigencia "01-01-2025", fecha de vencimiento "31-12-2035", materias en el plan "CAR997-M", "CAR998-M" y "CAR999-M" y total de créditos optativos 15

  Escenario: Dar de alta carrera con planes de estudio es exitoso
    Cuando se registra una nueva carrera con codigo "ING-SIS", nombre "Ingeniería en Sistemas", titulo "Ingeniero en Sistemas", incumbencias "Desarrollo de software, análisis de sistemas" y planes de estudio "PCAR1-2025" y "PCAR2-2025"
    Entonces se registra la carrera "ING-SIS" exitosamente

  Escenario: Dar de alta carrera sin planes de estudio es exitoso
    Cuando se registra una nueva carrera con codigo "MED", nombre "Medicina", titulo "Médico", incumbencias "Atención médica integral" y sin planes de estudio
    Entonces se registra la carrera "MED" exitosamente

  Escenario: Dar de alta carrera con codigo ya en uso fracasa
    Dado se registra una nueva carrera con codigo "ING-IND", nombre "Ingeniería Industrial", titulo "Ingeniero Industrial", incumbencias "Optimización de procesos" y planes de estudio "PCAR1-2025"
    Cuando se registra una nueva carrera con codigo "ING-IND", nombre "Ingeniería Industrial Renovada", titulo "Ingeniero Industrial", incumbencias "Gestión de operaciones" y planes de estudio "PCAR2-2025"
    Entonces no se registra la carrera exitosamente

  Escenario: Dar de alta carrera con plan de estudios inexistente fracasa
    Cuando se registra una nueva carrera con codigo "ARQ", nombre "Arquitectura", titulo "Arquitecto", incumbencias "Diseño y construcción" y planes de estudio "P-INEXISTENTE"
    Entonces no se registra la carrera exitosamente