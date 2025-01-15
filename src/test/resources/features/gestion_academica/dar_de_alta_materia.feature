#language: es
Característica: Dar de alta materia

  Escenario: : Dar de alta materia es exitoso
    Dado que existe un responsable de Gestión Académica
    Y existe un plan de estudios
    Cuando se registra una materia con "código de materia", "plan de estudios correspondiente", "nombre", "contenidos", "tipo de materia", "cantidad de créditos y correlativas"
    Entonces se registra la materia

