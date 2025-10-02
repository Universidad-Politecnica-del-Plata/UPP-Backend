#language: es
Característica: Dar de baja alumno
  Antecedentes:
    Dado que hay un gestor estudiantil logueado

  Escenario: Dar de baja alumno exitosamente
    Dado existe un alumno con matrícula 100001
    Cuando se da de baja el alumno con matrícula 100001
    Entonces se da de baja el alumno exitosamente
    Y el alumno con matrícula 100001 está inhabilitado

  Escenario: Error al dar de baja alumno que no existe
    Cuando se intenta dar de baja el alumno con matrícula 999999
    Entonces no se puede dar de baja el alumno

  Escenario: Verificar que la baja es lógica (no elimina físicamente)
    Y existe un alumno con matrícula 100001
    Cuando se da de baja el alumno con matrícula 100001
    Entonces el alumno con matrícula 100001 sigue existiendo en el sistema pero inhabilitado