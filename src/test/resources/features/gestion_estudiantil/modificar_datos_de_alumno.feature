#language: es
@todo
Característica: Modificar datos del alumno

  Escenario: : Modificar datos del alumno exitoso
    Dado que existe un responsable de Gestión Estudiantil
    Y existe un alumno
    Cuando modifica la información del alumno con "número de matrícula", "DNI", "apellido", "nombre", "dirección", "teléfonos", "email", "fecha de nacimiento", "fecha de ingreso", "fecha de egreso", "carreras que cursa" y "planes de estudio de la carrera"
    Entonces se actualiza la información del alumno

