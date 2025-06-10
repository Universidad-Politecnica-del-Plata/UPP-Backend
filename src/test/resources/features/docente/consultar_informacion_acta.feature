#language: es
@todo
Característica: Consultar informacion de acta

  Escenario: Consultar informacion de acta exitoso
    Dado que existe un docente con nombre "Luis", apellido "Gomez" y DNI "23456789"
    Y existe un curso a su nombre
    Y el curso tiene un acta
    Cuando consulta el acta del curso en el cuatrimestre
    Entonces se informa el "nombre" del "alumno", "matrícula" y "nota" de los alumnos en el acta
