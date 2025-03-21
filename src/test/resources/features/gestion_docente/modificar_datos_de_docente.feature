#language: es
Característica: Modificar datos del docente

  Escenario: : Modificar datos del docente exitoso
    Dado que existe un responsable de Gestión Docente
    Y existe un docente
    Cuando modifica un docente con "número de matrícula", "DNI", "apellido", "nombre", "dirección", "teléfonos", "email", "fecha de nacimiento", "fecha de ingreso", "fecha de egreso", "categoria" y "titulos"
    Entonces se actualiza la información del docente

