#language: es
Característica: Consultar plan de estudios

  Escenario: : Consultar plan de estudios es exitoso
    Dado que existe un alumno con nombre "Juan", apellido "Perez" y DNI "12345678"
    Y esta inscripto en la carrera "Ing. Informatica"
    Cuando consulta su plan de estudios
    Entonces  se le informa el nombre "Analisis Matematico II", los creditos 8, el codigo 71.10, correlativas ""
    Y se le informa el nombre "Fisica II", los creditos 6, el codigo 75.00, correlativas ""
    Y se le informa el nombre "Fisica III", los creditos 6, el codigo 75.01, correlativas "Fisica II, Analisis Matematico II"