#language: es
Característica: Modificar datos de un cuatrimestre
  Antecedentes:
    Dado que hay un gestor academico logueado
    Y que existe una materia con el código de materia "125-M" y nombre "Cálculo I"
    Y que existe una materia con el código de materia "126-M" y nombre "Física I"
    Y que hay un gestor de planificacion logueado
    Y que existe un curso con código "125-C-1" para la materia "125-M"
    Y que existe un curso con código "125-C-2" para la materia "125-M"
    Y que existe un curso con código "126-C-1" para la materia "126-M"
    Y se registra un nuevo cuatrimestre con código "2024-MOD-1", fecha de inicio de clases "2024-03-01", fecha de fin de clases "2024-07-15", fecha de inicio de inscripción "2024-02-01", fecha de fin de inscripción "2024-02-28", fecha de inicio de integradores "2024-07-16" y fecha de fin de integradores "2024-07-31"
    Y se registra un nuevo cuatrimestre con código "2024-MOD-2", fecha de inicio de clases "2024-08-01", fecha de fin de clases "2024-12-15", fecha de inicio de inscripción "2024-07-01", fecha de fin de inscripción "2024-07-31", fecha de inicio de integradores "2024-12-16" y fecha de fin de integradores "2024-12-31"

  Escenario: Modificar fechas de inicio y fin de clases es exitoso
    Cuando se modifica el cuatrimestre con código "2024-MOD-1", fecha de inicio de clases "2024-03-15", fecha de fin de clases "2024-07-30", fecha de inicio de inscripción "2024-02-01", fecha de fin de inscripción "2024-02-28", fecha de inicio de integradores "2024-07-31" y fecha de fin de integradores "2024-08-15"
    Entonces se actualiza la información del cuatrimestre "2024-MOD-1" exitosamente
    Y el cuatrimestre "2024-MOD-1" tiene fecha de inicio "2024-03-15" y fecha de fin "2024-07-30"

  Escenario: Modificar período de inscripción es exitoso
    Cuando se modifica el cuatrimestre con código "2024-MOD-2", fecha de inicio de clases "2024-08-01", fecha de fin de clases "2024-12-15", fecha de inicio de inscripción "2024-06-15", fecha de fin de inscripción "2024-07-15", fecha de inicio de integradores "2024-12-16" y fecha de fin de integradores "2024-12-31"
    Entonces se actualiza la información del cuatrimestre "2024-MOD-2" exitosamente
    Y el cuatrimestre "2024-MOD-2" tiene período de inscripción desde "2024-06-15" hasta "2024-07-15"

  Escenario: Modificar período de integradores es exitoso
    Cuando se modifica el cuatrimestre con código "2024-MOD-1", fecha de inicio de clases "2024-03-01", fecha de fin de clases "2024-07-15", fecha de inicio de inscripción "2024-02-01", fecha de fin de inscripción "2024-02-28", fecha de inicio de integradores "2024-08-01" y fecha de fin de integradores "2024-08-31"
    Entonces se actualiza la información del cuatrimestre "2024-MOD-1" exitosamente
    Y el cuatrimestre "2024-MOD-1" tiene período de integradores desde "2024-08-01" hasta "2024-08-31"

  Escenario: Modificar todas las fechas es exitoso
    Cuando se modifica el cuatrimestre con código "2024-MOD-2", fecha de inicio de clases "2024-09-01", fecha de fin de clases "2025-01-15", fecha de inicio de inscripción "2024-08-01", fecha de fin de inscripción "2024-08-31", fecha de inicio de integradores "2025-01-16" y fecha de fin de integradores "2025-01-31"
    Entonces se actualiza la información del cuatrimestre "2024-MOD-2" exitosamente
    Y el cuatrimestre "2024-MOD-2" tiene fecha de inicio "2024-09-01" y fecha de fin "2025-01-15"
    Y el cuatrimestre "2024-MOD-2" tiene período de inscripción desde "2024-08-01" hasta "2024-08-31"
    Y el cuatrimestre "2024-MOD-2" tiene período de integradores desde "2025-01-16" hasta "2025-01-31"

  Escenario: Modificar cuatrimestre inexistente fracasa
    Cuando se modifica el cuatrimestre con código "CUATRIMESTRE-INEXISTENTE", fecha de inicio de clases "2024-03-01", fecha de fin de clases "2024-07-15", fecha de inicio de inscripción "2024-02-01", fecha de fin de inscripción "2024-02-28", fecha de inicio de integradores "2024-07-16" y fecha de fin de integradores "2024-07-31"
    Entonces no se actualiza la información del cuatrimestre exitosamente

  Escenario: Modificar cuatrimestre con fechas inválidas fracasa
    Cuando se modifica el cuatrimestre con código "2024-MOD-1", fecha de inicio de clases "2024-07-15", fecha de fin de clases "2024-03-01", fecha de inicio de inscripción "2024-02-01", fecha de fin de inscripción "2024-02-28", fecha de inicio de integradores "2024-07-16" y fecha de fin de integradores "2024-07-31"
    Entonces no se actualiza la información del cuatrimestre exitosamente

  Escenario: Modificar cuatrimestre agregando cursos es exitoso
    Cuando se modifica el cuatrimestre con código "2024-MOD-1", fecha de inicio de clases "2024-03-01", fecha de fin de clases "2024-07-15", fecha de inicio de inscripción "2024-02-01", fecha de fin de inscripción "2024-02-28", fecha de inicio de integradores "2024-07-16", fecha de fin de integradores "2024-07-31" y los cursos "125-C-1,126-C-1"
    Entonces se actualiza la información del cuatrimestre "2024-MOD-1" exitosamente
    Y el cuatrimestre "2024-MOD-1" tiene los cursos "125-C-1,126-C-1"

  Escenario: Modificar cuatrimestre cambiando cursos es exitoso
    Dado que el cuatrimestre "2024-MOD-2" tiene asociados los cursos "125-C-1"
    Cuando se modifica el cuatrimestre con código "2024-MOD-2", fecha de inicio de clases "2024-08-01", fecha de fin de clases "2024-12-15", fecha de inicio de inscripción "2024-07-01", fecha de fin de inscripción "2024-07-31", fecha de inicio de integradores "2024-12-16", fecha de fin de integradores "2024-12-31" y los cursos "125-C-2,126-C-1"
    Entonces se actualiza la información del cuatrimestre "2024-MOD-2" exitosamente
    Y el cuatrimestre "2024-MOD-2" tiene los cursos "125-C-2,126-C-1"
    Y el cuatrimestre "2024-MOD-2" no tiene el curso "125-C-1"

  Escenario: Modificar cuatrimestre eliminando todos los cursos es exitoso
    Dado que el cuatrimestre "2024-MOD-1" tiene asociados los cursos "125-C-1,126-C-1"
    Cuando se modifica el cuatrimestre con código "2024-MOD-1", fecha de inicio de clases "2024-03-01", fecha de fin de clases "2024-07-15", fecha de inicio de inscripción "2024-02-01", fecha de fin de inscripción "2024-02-28", fecha de inicio de integradores "2024-07-16", fecha de fin de integradores "2024-07-31" y sin cursos
    Entonces se actualiza la información del cuatrimestre "2024-MOD-1" exitosamente
    Y el cuatrimestre "2024-MOD-1" no tiene cursos asociados

  Escenario: Modificar cuatrimestre con cursos inexistentes fracasa
    Cuando se modifica el cuatrimestre con código "2024-MOD-1", fecha de inicio de clases "2024-03-01", fecha de fin de clases "2024-07-15", fecha de inicio de inscripción "2024-02-01", fecha de fin de inscripción "2024-02-28", fecha de inicio de integradores "2024-07-16", fecha de fin de integradores "2024-07-31" y los cursos "CURSO-INEXISTENTE"
    Entonces se actualiza la información del cuatrimestre "2024-MOD-1" exitosamente
    Y el cuatrimestre "2024-MOD-1" no tiene cursos asociados