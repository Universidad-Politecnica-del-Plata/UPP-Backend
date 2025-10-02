#language: es
Característica: Modificar datos de una carrera
  Antecedentes:
    Dado que hay un gestor academico logueado
    Y que existe una materia con el código de materia "CAR997-M" y nombre "Analisis I"
    Y que existe una materia con el código de materia "CAR998-M" y nombre "Algebra I"
    Y que existe una materia con el código de materia "CAR999-M" y nombre "Algoritmos y Programacion I"
    Y que existe un plan de estudios con codigo "PCAR1-2025", fecha de entrada en vigencia "01-01-2025", fecha de vencimiento "31-12-2035", materias en el plan "CAR997-M", "CAR998-M" y "CAR999-M" y total de créditos optativos 20
    Y que existe un plan de estudios con codigo "PCAR2-2025", fecha de entrada en vigencia "01-01-2025", fecha de vencimiento "31-12-2035", materias en el plan "CAR997-M", "CAR998-M" y "CAR999-M" y total de créditos optativos 15
    Y se registra una nueva carrera con codigo "ING-SIS", nombre "Ingeniería en Sistemas", titulo "Ingeniero en Sistemas", incumbencias "Desarrollo de software, análisis de sistemas" y planes de estudio "PCAR1-2025"
    Y se registra una nueva carrera con codigo "MED", nombre "Medicina", titulo "Médico", incumbencias "Atención médica integral" y sin planes de estudio

  Escenario: Modificar datos básicos de una carrera es exitoso
    Cuando se modifica la carrera con codigo "ING-SIS", nombre "Ingeniería en Sistemas de Información", titulo "Ingeniero en Sistemas de Información", incumbencias "Desarrollo de software, gestión de datos, análisis de sistemas"
    Entonces se actualiza la información de la carrera "ING-SIS" exitosamente

  Escenario: Modificar carrera agregando planes de estudio es exitoso
    Cuando se modifica la carrera con codigo "MED", nombre "Medicina", titulo "Médico", incumbencias "Atención médica integral, investigación clínica" y planes de estudio "PCAR1-2025" y "PCAR2-2025"
    Entonces se actualiza la información de la carrera "MED" exitosamente

  Escenario: Modificar carrera cambiando planes de estudio es exitoso
    Cuando se modifica la carrera con codigo "ING-SIS", nombre "Ingeniería en Sistemas", titulo "Ingeniero en Sistemas", incumbencias "Desarrollo de software, análisis de sistemas" y planes de estudio "PCAR2-2025"
    Entonces se actualiza la información de la carrera "ING-SIS" exitosamente

  Escenario: Modificar carrera removiendo planes de estudio es exitoso
    Cuando se modifica la carrera con codigo "ING-SIS", nombre "Ingeniería en Sistemas", titulo "Ingeniero en Sistemas", incumbencias "Desarrollo de software, análisis de sistemas" y sin planes de estudio
    Entonces se actualiza la información de la carrera "ING-SIS" exitosamente

  Escenario: Modificar carrera inexistente fracasa
    Cuando se modifica la carrera con codigo "ING-INEXISTENTE", nombre "Carrera Inexistente", titulo "Título", incumbencias "Incumbencias"
    Entonces no se actualiza la información de la carrera exitosamente

  Escenario: Modificar carrera con plan de estudios inexistente fracasa
    Cuando se modifica la carrera con codigo "ING-SIS", nombre "Ingeniería en Sistemas", titulo "Ingeniero en Sistemas", incumbencias "Desarrollo de software" y planes de estudio "P-INEXISTENTE"
    Entonces no se actualiza la información de la carrera exitosamente

