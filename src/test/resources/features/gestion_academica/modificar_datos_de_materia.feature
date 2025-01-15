#language: es
Característica: Modificar datos de una materia

  Escenario: : Modificar datos de una materia exitoso
    Dado que existe un responsable de Gestión Académica
    Y existe una materia con "código de materia", "plan de estudios correspondiente", "nombre", "contenidos", "tipo de materia", "cantidad de créditos y correlativas"
    Cuando se modifica la materia en el sistema con "código de materia", "plan de estudios correspondiente", "nombre", "contenidos", "tipo de materia", "cantidad de créditos y correlativas"
    Entonces se actualiza la información de la materia

