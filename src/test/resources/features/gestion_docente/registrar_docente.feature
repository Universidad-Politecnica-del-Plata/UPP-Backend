#language: es
Característica: Registrar docente

  Escenario: : Registrar docente es exitoso
    Dado que existe un responsable de Gestión Docente
    Cuando registra un nuevo docente con "número de matrícula", "DNI", "apellido", "nombre", "dirección", "teléfonos", "fecha de nacimiento", "fecha de ingreso", "fecha de egreso", "categoria" y "titulos"
    Entonces se registra el docente

