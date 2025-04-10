#language: es
Característica: Registrar alumno

  Escenario:  Registrar alumno es exitoso
    Cuando registra un nuevo alumno con DNI 12345678, apellido "Perez", nombre "Juan", direccion "Calle Falsa 123", telefono "1234", email "juan@mail.com", fecha de nacimiento "01-01-1990" y fecha de ingreso "01-03-2015"
    Entonces se registra el alumno exitosamente

  Escenario:  Registrar alumno con mismo DNI no es exitoso
    Dado un alumno registrado con DNI 12345678
    Cuando registra un nuevo alumno con DNI 12345678, apellido "Perez", nombre "Juan", direccion "Calle Falsa 123", telefono "1234", email "juan@mail.com", fecha de nacimiento "01-01-1990" y fecha de ingreso "01-03-2015"
    Entonces no se registra el alumno

  Escenario:  Registrar alumno con mismo email no es exitoso
    Dado un alumno registrado con email "juan@mail.com"
    Cuando registra un nuevo alumno con DNI 12345678, apellido "Perez", nombre "Juan", direccion "Calle Falsa 123", telefono "1234", email "juan@mail.com", fecha de nacimiento "01-01-1990" y fecha de ingreso "01-03-2015"
    Entonces no se registra el alumno





