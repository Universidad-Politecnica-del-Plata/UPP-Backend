#language: es
Característica: Dar de baja carrera
  Antecedentes:
    Dado que hay un gestor academico logueado
    Y que existe una materia con el código de materia "CAR997-M" y nombre "Analisis I"
    Y que existe una materia con el código de materia "CAR998-M" y nombre "Algebra I"
    Y que existe una materia con el código de materia "CAR999-M" y nombre "Algoritmos y Programacion I"
    Y que existe un plan de estudios con codigo "PCAR1-2025", fecha de entrada en vigencia "01-01-2025", fecha de vencimiento "31-12-2035", materias en el plan "CAR997-M", "CAR998-M" y "CAR999-M" y total de créditos optativos 20
    Y se registra una nueva carrera con codigo "ING-SIS", nombre "Ingeniería en Sistemas", titulo "Ingeniero en Sistemas", incumbencias "Desarrollo de software, análisis de sistemas" y planes de estudio "PCAR1-2025"

  Escenario: Dar de baja carrera con planes de estudio fracasa por validación
    Cuando se da de baja la carrera con codigo "ING-SIS"
    Entonces no se elimina el registro de la carrera por tener planes asociados

  Escenario: Dar de baja carrera inexistente fracasa
    Cuando se da de baja la carrera con codigo "ING-INEXISTENTE"
    Entonces no se elimina el registro de la carrera exitosamente

  Escenario: Dar de baja carrera sin planes de estudio es exitoso
    Cuando se registra una nueva carrera con codigo "ING-VACIA", nombre "Ingeniería Vacía", titulo "Ingeniero Vacío", incumbencias "Testing" y sin planes de estudio
    Y se da de baja la carrera con codigo "ING-VACIA"
    Entonces se elimina el registro de la carrera "ING-VACIA" exitosamente
