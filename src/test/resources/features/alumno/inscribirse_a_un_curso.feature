#language: es
@todo
Característica: Inscribirse a un curso

  Escenario: : Inscribirse a un curso exitoso
    Dado que existe un alumno con nombre "Juan", apellido "Perez" y DNI "12345678"
    Y existen cursos para una materia obligatoria
    Y cumple con los requerimientos de horarios
    Y cumple con los requerimientos de correlativas
    Cuando se inscribe a un curso de una materia obligatoria
    Entonces se registra la inscripcion en el curso

  Escenario: : Inscribirse a un curso exitoso
    Dado que existe un alumno con nombre "Juan", apellido "Perez" y DNI "12345678"
    Y existen cursos para una materia optativa
    Y cumple con los requerimientos de horarios
    Y cumple con los requerimientos de correlativas
    Y cumplo con los requerimientos de créditos
    Cuando se inscribe a un curso de una materia optativa
    Entonces se registra la inscripcion en el curso