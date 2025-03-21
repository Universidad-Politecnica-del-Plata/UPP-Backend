#language: es
Característica: Registrar alumno

  Escenario: : Registrar alumno es exitoso
    Dado que existe un responsable de Gestión Estudiantil
    Cuando registra un nuevo alumno con "número de matrícula", "DNI", "apellido", "nombre", "dirección", "teléfonos", "email", "fecha de nacimiento", "fecha de ingreso", "fecha de egreso", "carreras que cursa" y "planes de estudio de la carrera"
    Entonces se registra el alumno

