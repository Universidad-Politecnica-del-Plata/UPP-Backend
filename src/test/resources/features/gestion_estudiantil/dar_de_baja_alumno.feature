#language: es
Característica: Dar de baja alumno

  Escenario: : Dar de baja alumno es exitoso
    Dado que existe un responsable de Gestión Estudiantil
    Y existe un alumno con "número de matrícula", "DNI", "apellido", "nombre", "dirección", "teléfonos", "fecha de nacimiento", "fecha de ingreso", "fecha de egreso", "carreras que cursa" y "planes de estudio de la carrera"
    Cuando se da de baja el alumno
    Entonces se elimina el registro del alumno

