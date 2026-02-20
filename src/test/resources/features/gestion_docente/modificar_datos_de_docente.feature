#language: es
@todo
Característica: Modificar datos del docente
  # Como responsable de Gestión Docente quiero modificar los datos de un docente para tener su informacion al dia.

  Escenario: : Modificar datos del docente exitoso
    Dado que existe un docente docente con "número de matrícula", "DNI", "apellido", "nombre", "dirección", "teléfonos", "email", "fecha de nacimiento", "fecha de ingreso", "fecha de egreso", "categoria" y "titulos"
    Cuando modifica un docente con "número de matrícula", "DNI", "apellido", "nombre", "dirección", "teléfonos", "email", "fecha de nacimiento", "fecha de ingreso", "fecha de egreso", "categoria" y "titulos"
    Entonces se actualiza la información del docente

