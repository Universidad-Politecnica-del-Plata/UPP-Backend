#language: es
@todo
Característica: Dar de baja docente

  Escenario: : Dar de baja docente es exitoso
    Dado que existe un responsable de Gestión Docente
    Y existe un docente docente con "número de matrícula", "DNI", "apellido", "nombre", "dirección", "teléfonos", "email", "fecha de nacimiento", "fecha de ingreso", "fecha de egreso", "categoria" y "titulos"
    Cuando se da de baja el docente
    Entonces se elimina el registro del docente

