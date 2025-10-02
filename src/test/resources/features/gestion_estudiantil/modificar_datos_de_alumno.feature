#language: es
Característica: Modificar datos del alumno
  Antecedentes:
    Dado que hay un gestor estudiantil logueado

  Escenario: Modificar datos del alumno exitosamente
    Y existe un alumno con matrícula 100001
    Cuando se modifican los datos del alumno con matrícula 100001 con nombre "Carlos", apellido "Rodriguez", dni 87654321, email "carlos.rodriguez@example.com", dirección "Av. Libertador 1234", teléfonos "987654321", fecha de nacimiento "1995-05-15", fecha de ingreso "2023-03-01" y fecha de egreso "2027-12-15"
    Entonces se actualizan los datos del alumno exitosamente
    Y el alumno con matrícula 100001 tiene los nuevos datos

  Escenario: Error al modificar datos de alumno que no existe
    Cuando se intenta modificar los datos del alumno con matrícula 999999 con nombre "Carlos", apellido "Rodriguez", dni 87654321, email "carlos.rodriguez@example.com", dirección "Av. Libertador 1234", teléfonos "987654321", fecha de nacimiento "1995-05-15", fecha de ingreso "2023-03-01" y fecha de egreso "2027-12-15"
    Entonces se obtiene el error "No existe un alumno con esa matrícula."

  Escenario: Error al intentar modificar la matrícula (campo inmutable)
    Y existe un alumno con matrícula 100001
    Cuando se modifican los datos del alumno con matrícula 100001 con nombre "Carlos", apellido "Rodriguez", dni 87654321, email "carlos.rodriguez@example.com", dirección "Av. Libertador 1234", teléfonos "987654321", fecha de nacimiento "1995-05-15", fecha de ingreso "2023-03-01" y fecha de egreso "2027-12-15"
    Entonces la matrícula del alumno permanece como 100001