#language: es
@todo
Característica: Modificar datos del docente
  # Como responsable de Gestión Docente quiero modificar los datos de un docente para garantizar la exactitud de la información administrativa publicada.

  Escenario: : Modificar datos del docente exitoso
    Dado que existe un docente docente con "número de matrícula", "DNI", "apellido", "nombre", "dirección", "teléfonos", "email", "fecha de nacimiento", "fecha de ingreso", "fecha de egreso", "categoria" y "titulos"
    Cuando modifica un docente con "número de matrícula", "DNI", "apellido", "nombre", "dirección", "teléfonos", "email", "fecha de nacimiento", "fecha de ingreso", "fecha de egreso", "categoria" y "titulos"
    Entonces se actualiza la información del docente

  Escenario: : Modificar datos de docente inexistente falla
    Dado que no existe un docente con matrícula 99999
    Cuando se intenta modificar los datos del docente con matrícula 99999
    Entonces no se puede modificar el docente porque no existe

  Escenario: : Modificar docente con email duplicado falla
    Dado que existe un docente docente con email "docente1@universidad.edu"
    Y existe otro docente con email "docente2@universidad.edu"
    Cuando se intenta modificar el primer docente con el email "docente2@universidad.edu"
    Entonces no se puede modificar el docente porque el email ya está en uso

