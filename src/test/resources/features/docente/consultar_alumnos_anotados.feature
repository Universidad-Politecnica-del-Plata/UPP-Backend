#language: es
Característica: Consultar alumnos anotados a un curso

  Escenario: Consultar alumnos anotados a un curso exitoso
    Dado que existe un docente con nombre "Luis", apellido "Gomez" y DNI "23456789"
    Y existen cursos a su nombre
    Y existen alumnos inscriptos a los cursos
    Cuando consulta los alumnos anotados a uno de sus cursos
    Entonces se informa "nombre", "apellido" y "matrícula" de cada alumno
