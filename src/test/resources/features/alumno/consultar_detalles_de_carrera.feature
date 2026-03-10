#language: es
Característica: Consultar detalles de carrera

  #Como alumno quiero consultar los detalles de mi carrera para planear mejor mi trayectoria académica

  Escenario: Consultar detalles de carrera es exitoso
    Dado que existe un alumno con nombre "Juan", apellido "Perez" y DNI "12345678"
    Y esta inscripto en la carrera "Ing. Informatica"
    Cuando consulta la información de su carrera "ING-INFORMATICA"
    Entonces se le informa nombre "Ing. Informatica", titulo "Título de Ing. Informatica" e incumbencias "Incumbencias de Ing. Informatica"

  Escenario: Consultar detalles de carrera inexistente falla
    Dado que existe un alumno con nombre "Juan", apellido "Perez" y DNI "12345678"
    Cuando consulta la información de su carrera "CARRERA-INEXISTENTE"
    Entonces se le informa que la carrera no existe
