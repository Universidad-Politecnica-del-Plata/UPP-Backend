#language: es

Característica: Dar de baja plan de estudios
  Antecedentes:
    Dado que hay un gestor academico logueado
    Y que existe una materia con el código de materia "1997-M" y nombre "Analisis I"
    Y que existe una materia con el código de materia "1998-M" y nombre "Algebra I"
    Y que existe una materia con el código de materia "1999-M" y nombre "Algoritmos y Programacion I"

  Escenario: : Dar de baja plan de estudios es exitoso
    Dado se registra un nuevo plan de estudios con codigo "P2-2025", fecha de entrada en vigencia "01-01-2025", fecha de vencimiento "31-12-2035", materias en el plan "1997-M", "1998-M" y "1999-M" y total de créditos optativos 20
    Cuando se da de baja el plan de estudios con codigo "P2-2025"
    Entonces no existe el plan de estudios "P2-2025" en el registro

  Escenario: : Dar de baja plan de estudios que no existe lanza error
    Dado se registra un nuevo plan de estudios con codigo "P2-2026", fecha de entrada en vigencia "01-01-2025", fecha de vencimiento "31-12-2035", materias en el plan "1997-M", "1998-M" y "1999-M" y total de créditos optativos 20
    Cuando se da de baja el plan de estudios con codigo "P2-2027"
    Entonces no se elimina el plan de estudios y se lanza error

