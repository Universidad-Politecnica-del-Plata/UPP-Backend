#language: es
Característica: Dar de baja cuatrimestre
  Antecedentes:
    Dado que hay un gestor academico logueado
    Y que existe una materia con el código de materia "125-M" y nombre "Cálculo I"
    Y que existe una materia con el código de materia "126-M" y nombre "Física I"
    Y que hay un gestor de planificacion logueado

  Escenario: Dar de baja cuatrimestre sin cursos es exitoso
    Dado se registra un nuevo cuatrimestre con código "2024-DELETE-1", fecha de inicio de clases "2024-03-01", fecha de fin de clases "2024-07-15", fecha de inicio de inscripción "2024-02-01", fecha de fin de inscripción "2024-02-28", fecha de inicio de integradores "2024-07-16" y fecha de fin de integradores "2024-07-31"
    Cuando se da de baja el cuatrimestre con código "2024-DELETE-1"
    Entonces no existe el cuatrimestre "2024-DELETE-1" en el registro

  Escenario: Dar de baja cuatrimestre con cursos es exitoso
    Dado se registra un nuevo cuatrimestre con código "2024-DELETE-2", fecha de inicio de clases "2024-08-01", fecha de fin de clases "2024-12-15", fecha de inicio de inscripción "2024-07-01", fecha de fin de inscripción "2024-07-31", fecha de inicio de integradores "2024-12-16" y fecha de fin de integradores "2024-12-31"
    Y se registra un nuevo curso con código "CURSO-CUATR-01", máximo de alumnos 25, materia "125-M" y cuatrimestres "2024-DELETE-2"
    Y se registra un nuevo curso con código "CURSO-CUATR-02", máximo de alumnos 30, materia "126-M" y cuatrimestres "2024-DELETE-2"
    Cuando se da de baja el cuatrimestre con código "2024-DELETE-2"
    Entonces no existe el cuatrimestre "2024-DELETE-2" en el registro
    Y los cursos "CURSO-CUATR-01,CURSO-CUATR-02" ya no tienen el cuatrimestre "2024-DELETE-2" asignado

  Escenario: Dar de baja cuatrimestre que no existe lanza error
    Dado se registra un nuevo cuatrimestre con código "2024-DELETE-3", fecha de inicio de clases "2024-03-01", fecha de fin de clases "2024-07-15", fecha de inicio de inscripción "2024-02-01", fecha de fin de inscripción "2024-02-28", fecha de inicio de integradores "2024-07-16" y fecha de fin de integradores "2024-07-31"
    Cuando se da de baja el cuatrimestre con código "CUATRIMESTRE-INEXISTENTE"
    Entonces no se elimina el cuatrimestre y se lanza error