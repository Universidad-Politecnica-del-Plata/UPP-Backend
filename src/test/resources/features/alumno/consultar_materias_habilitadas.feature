#language: es
@todo
Característica: Consultar materias habilitadas


  Escenario: : Consultar materias habilitadas es exitoso
    Dado que existe un alumno con nombre "Juan", apellido "Perez" y DNI "12345678"
    Y esta inscripto en la carrera "Ing. Informatica"
    Y cumple con los requerimientos para cursar materias
    Cuando consulta las materias habilitadas
    Entonces se le informa el nombre "Analisis Matematico II", los creditos 8, el codigo 71.10
    Y se le informa el nombre "Fisica II", los creditos 6, el codigo 75.00
