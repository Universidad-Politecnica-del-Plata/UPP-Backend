#language: es
Característica: Dar de alta plan de estudios
  Antecedentes:
    Dado que hay un gestor academico logueado
    Y que existe una materia con el código de materia "997-M" y nombre "Analisis I"
    Y que existe una materia con el código de materia "998-M" y nombre "Algebra I"
    Y que existe una materia con el código de materia "999-M" y nombre "Algoritmos y Programacion I"

  Escenario: Dar de alta plan de estudios es exitoso

    Cuando se registra un nuevo plan de estudios con codigo "P1-2025", fecha de entrada en vigencia "01-01-2025", fecha de vencimiento "31-12-2035", materias en el plan "997-M", "998-M" y "999-M" y total de créditos optativos 20
    Entonces se registra el plan de estudios "P1-2025" exitosamente

