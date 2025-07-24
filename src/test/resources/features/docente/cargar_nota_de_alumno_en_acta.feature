#language: es
@todo
Característica: Cargar nota de alumno a un acta

  Escenario: Cargar nota aprobatoria de alumno exitoso
    Dado que existe un docente con nombre "Luis", apellido "Gomez" y DNI "23456789"
    Y existe un curso a su nombre
    Y el curso tiene un acta en estado Abierta
    Cuando carga la nota de un alumno con "nombre", "matrícula" y "nota"
    Y la nota es "aprobatoria"
    Entonces se guarda la información en el acta

  Escenario: Cargar nota desaprobatoria de alumno exitoso
    Dado que existe un docente con nombre "Luis", apellido "Gomez" y DNI "23456789"
    Y existe un curso a su nombre
    Y el curso tiene un acta en estado Abierta
    Cuando carga la nota de un alumno con "nombre", "matrícula" y "nota"
    Y la nota es "desaprobatoria"
    Entonces se no guarda la información en el acta