#language: es
@todo
Característica: Consultar alumnos anotados a un curso
#Como docente quiero consultar los alumnos anotados a un curso para hacer un seguimiento de los alumnos
  Escenario: Consultar alumnos anotados a un curso exitoso
    Dado que existe un docente con nombre "Luis", apellido "Gomez" y DNI "23456789"
    Y existen cursos a su nombre
    Y existen alumnos inscriptos a los cursos
    Cuando consulta los alumnos anotados a uno de sus cursos
    Entonces se informa "nombre", "apellido" y "matrícula" de cada alumno

  Escenario: Consultar alumnos anotados a un curso sin alumnos inscriptos
    Dado que existe un docente con nombre "Luis", apellido "Gomez" y DNI "23456789"
    Y existen cursos a su nombre
    Y no existen alumnos inscriptos al curso
    Cuando consulta los alumnos anotados a uno de sus cursos
    Entonces se le informa que no hay alumnos inscriptos al curso

  Escenario: Consultar alumnos anotados a un curso que no es suyo falla
    Dado que existe un docente con nombre "Luis", apellido "Gomez" y DNI "23456789"
    Y existe un curso que no está a su nombre
    Cuando intenta consultar los alumnos anotados a ese curso
    Entonces no se le permite ver los alumnos del curso
