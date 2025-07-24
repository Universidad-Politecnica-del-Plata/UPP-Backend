#language: es
@todo
Característica: Consultar cursos a su nombre

  Escenario: : Consultar cursos a su nombre es exitoso
    Dado que existe un docente con nombre "Luis", apellido "Gomez" y DNI "23456789"
    Y tiene cursos a su nombre
    Cuando consulta los cursos a su nombre
    Entonces se informa el "código de curso", "materia","código de materia", "cuatrimestre", "horarios", "modalidad", "cantidad máxima de alumnos" y "aulas" de cada curso
