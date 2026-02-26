#language: es
@todo
Característica: Dar de baja docente
  # Como responsable de Gestión Docente quiero dar de baja a un docente para reflejar la finalización de su vínculo con la facultad.

  Escenario: Dar de baja docente es exitoso
    Dado que existe un docente docente con "número de matrícula", "DNI", "apellido", "nombre", "dirección", "teléfonos", "email", "fecha de nacimiento", "fecha de ingreso", "fecha de egreso", "categoria" y "titulos"
    Cuando se da de baja el docente
    Entonces se elimina el registro del docente

  Escenario: Dar de baja docente inexistente falla
    Dado que no existe un docente con matrícula 99999
    Cuando se intenta dar de baja el docente con matrícula 99999
    Entonces no se puede dar de baja el docente porque no existe

  Escenario: Dar de baja docente con cursos activos asignados falla
    Dado que existe un docente con cursos activos asignados
    Cuando se intenta dar de baja el docente
    Entonces no se puede dar de baja el docente porque tiene cursos activos

