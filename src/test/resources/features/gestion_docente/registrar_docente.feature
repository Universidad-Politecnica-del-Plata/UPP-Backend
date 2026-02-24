#language: es
@todo
Característica: Registrar docente
  # Como responsable de Gestión Docente quiero registrar un docente para oficializar el ingreso

  Escenario: : Registrar docente es exitoso
    Dado que no existe un docente con "nro de matricula" y "dni"
    Cuando registra un nuevo docente con "número de matrícula", "DNI", "apellido", "nombre", "dirección", "teléfonos", "email", "fecha de nacimiento", "fecha de ingreso", "fecha de egreso", "categoria" y "titulos"
    Entonces se registra el docente

  Escenario: : Registrar docente con DNI duplicado falla
    Dado que existe un docente con DNI "12345678"
    Cuando se intenta registrar un nuevo docente con DNI "12345678"
    Entonces no se registra el docente porque el DNI ya existe

  Escenario: : Registrar docente con email duplicado falla
    Dado que existe un docente con email "docente@universidad.edu"
    Cuando se intenta registrar un nuevo docente con email "docente@universidad.edu"
    Entonces no se registra el docente porque el email ya existe

