#language: es
Característica: Registrar alumno

  Escenario:  Registrar alumno es exitoso
    Cuando registra un nuevo alumno con DNI 12345678, apellido "Perez", nombre "Juan", direccion "Calle Falsa 123", telefono 1234, email "juan@mail.com", fecha de nacimiento "01-01-1990" y fecha de ingreso "01-03-2015"
    Entonces se registra el alumno exitosamente

  Escenario:  Registrar alumno con mismo DNI no es exitoso
    Dado un alumno registrado con DNI 12345678
    Cuando registra un nuevo alumno con "número de matrícula", DNI 12345678, "apellido", "nombre", "dirección", "teléfonos", "email", "fecha de nacimiento", "fecha de ingreso", "fecha de egreso", "carreras que cursa" y "planes de estudio de la carrera"
    Entonces no se registra el alumno

  Escenario:  Registrar alumno con mismo email no es exitoso
    Dado un alumno registrado con email alumno@mail.com
    Cuando registra un nuevo alumno con "número de matrícula", DNI 12345678, "apellido", "nombre", "dirección", "teléfonos", email alumno@mail.com, "fecha de nacimiento", "fecha de ingreso", "fecha de egreso", "carreras que cursa" y "planes de estudio de la carrera"
    Entonces no se registra el alumno





