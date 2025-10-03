#language: es
Característica: Modificar datos de un curso
  Antecedentes:
    Dado que hay un gestor academico logueado
    Y que existe una materia con el código de materia "125-M" y nombre "Cálculo I"
    Y que existe una materia con el código de materia "126-M" y nombre "Física I"
    Y que hay un gestor de planificacion logueado
    Y se registra un nuevo curso con código "CURSO-MOD-01", máximo de alumnos 25 y materia "125-M"
    Y se registra un nuevo curso con código "CURSO-MOD-02", máximo de alumnos 30 y materia "126-M"


  Escenario: Modificar máximo de alumnos de un curso es exitoso
    Cuando se modifica el curso con código "CURSO-MOD-01", máximo de alumnos 35 y materia "125-M"
    Entonces se actualiza la información del curso "CURSO-MOD-01" exitosamente
    Y el curso "CURSO-MOD-01" tiene máximo de alumnos 35 y materia "125-M"

  Escenario: Modificar materia de un curso es exitoso
    Cuando se modifica el curso con código "CURSO-MOD-02", máximo de alumnos 30 y materia "126-M"
    Entonces se actualiza la información del curso "CURSO-MOD-02" exitosamente
    Y el curso "CURSO-MOD-02" tiene máximo de alumnos 30 y materia "126-M"

  Escenario: Modificar todos los datos de un curso es exitoso
    Cuando se modifica el curso con código "CURSO-MOD-01", máximo de alumnos 40 y materia "126-M"
    Entonces se actualiza la información del curso "CURSO-MOD-01" exitosamente
    Y el curso "CURSO-MOD-01" tiene máximo de alumnos 40 y materia "126-M"

  Escenario: Modificar curso inexistente fracasa
    Cuando se modifica el curso con código "CURSO-INEXISTENTE", máximo de alumnos 20 y materia "125-M"
    Entonces no se actualiza la información del curso exitosamente

  Escenario: Modificar curso con materia inexistente fracasa
    Cuando se modifica el curso con código "CURSO-MOD-01", máximo de alumnos 25 y materia "MATERIA-INEXISTENTE"
    Entonces no se actualiza la información del curso exitosamente
