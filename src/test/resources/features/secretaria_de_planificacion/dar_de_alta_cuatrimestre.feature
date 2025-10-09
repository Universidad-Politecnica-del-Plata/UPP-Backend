#language: es
Característica: Dar de alta cuatrimestre
  Antecedentes:
    Dado que hay un gestor de planificacion logueado

  Escenario: Dar de alta cuatrimestre es exitoso
    Cuando se registra un nuevo cuatrimestre con código "2024-1", fecha de inicio de clases "2024-03-01", fecha de fin de clases "2024-07-15", fecha de inicio de inscripción "2024-02-01", fecha de fin de inscripción "2024-02-28", fecha de inicio de integradores "2024-07-16" y fecha de fin de integradores "2024-07-31"
    Entonces se registra el cuatrimestre "2024-1" exitosamente

  Escenario: Dar de alta cuatrimestre con código ya en uso fracasa
    Dado se registra un nuevo cuatrimestre con código "2024-1", fecha de inicio de clases "2024-03-01", fecha de fin de clases "2024-07-15", fecha de inicio de inscripción "2024-02-01", fecha de fin de inscripción "2024-02-28", fecha de inicio de integradores "2024-07-16" y fecha de fin de integradores "2024-07-31"
    Cuando se registra un nuevo cuatrimestre con código "2024-1", fecha de inicio de clases "2024-08-01", fecha de fin de clases "2024-12-15", fecha de inicio de inscripción "2024-07-01", fecha de fin de inscripción "2024-07-31", fecha de inicio de integradores "2024-12-16" y fecha de fin de integradores "2024-12-31"
    Entonces no se registra el cuatrimestre exitosamente

  Escenario: Dar de alta cuatrimestre con fechas inválidas fracasa
    Cuando se registra un nuevo cuatrimestre con código "2024-2", fecha de inicio de clases "2024-07-15", fecha de fin de clases "2024-03-01", fecha de inicio de inscripción "2024-02-01", fecha de fin de inscripción "2024-02-28", fecha de inicio de integradores "2024-07-16" y fecha de fin de integradores "2024-07-31"
    Entonces no se registra el cuatrimestre exitosamente

