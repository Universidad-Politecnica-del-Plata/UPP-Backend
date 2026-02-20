#language: es
@todo
Característica: Consultar cursos a su nombre
#Como docente quiero consultar todos los cursos a mi nombre para planificar mi trabajo en el cuatrimestre
  Escenario: : Consultar cursos a su nombre es exitoso
    Dado que existe un docente con nombre "Luis", apellido "Gomez" y DNI "23456789"
    Y tiene cursos a su nombre
    Cuando consulta los cursos a su nombre
    Entonces se informa el "código de curso", "materia","código de materia", "cuatrimestre", "horarios", "modalidad", "cantidad máxima de alumnos" y "aulas" de cada curso
