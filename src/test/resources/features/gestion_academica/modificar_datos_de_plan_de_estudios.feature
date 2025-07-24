#language: es
Característica: Modificar datos de un plan de estudios

  Antecedentes:
    Dado que hay un gestor academico logueado
    Y que existe una materia con el código de materia "1234-M" y nombre "Analisis I"
    Y que existe una materia con el código de materia "1235-M" y nombre "Algebra I"
    Y que existe una materia con el código de materia "1236-M" y nombre "Algoritmos y Programacion I"
    Y que existe una materia con el código de materia "1237-M" y nombre "Algoritmos y Programacion II"
    Y se registra un nuevo plan de estudios con codigo "P1-2025", fecha de entrada en vigencia "01-01-2025", fecha de vencimiento "31-12-2035", materias en el plan "1234-M", "1235-M" y "1236-M" y total de créditos optativos 20

  Escenario: Modificar datos de plan de estudios es exitoso
    Cuando al plan de estudios con código "P1-2025" se modifica fecha de entrada en vigencia "31-01-2025", fecha de vencimiento "31-12-2035", materias en el plan "1234-M" y "1235-M" y total de créditos optativos 25
    Entonces se actualiza la información del plan de estudios exitosamente
    Y el plan de estudios "P1-2025" tiene fecha de entrada en vigencia "31-01-2025", fecha de vencimiento "31-12-2035", materias en el plan "1234-M" y "1235-M" y total de créditos optativos 25

  Escenario: Modificar materias del plan de estudios
    Cuando al plan de estudios con código "P1-2025" se modifica fecha de entrada en vigencia "01-01-2025", fecha de vencimiento "31-12-2035", materias en el plan "1236-M" y "1237-M" y total de créditos optativos 30
    Entonces se actualiza la información del plan de estudios exitosamente
    Y el plan de estudios "P1-2025" tiene fecha de entrada en vigencia "01-01-2025", fecha de vencimiento "31-12-2035", materias en el plan "1236-M" y "1237-M" y total de créditos optativos 30

  Escenario: Modificar plan de estudios que no existe
    Cuando al plan de estudios con código "P999-2025" se modifica fecha de entrada en vigencia "31-01-2025", fecha de vencimiento "31-12-2035", materias en el plan "1234-M" y "1235-M" y total de créditos optativos 20
    Entonces no se puede modificar el plan de estudios inexistente

  Escenario: Modificar plan de estudios con materia inexistente
    Cuando al plan de estudios con código "P1-2025" se modifica fecha de entrada en vigencia "31-01-2025", fecha de vencimiento "31-12-2035", materias en el plan "1234-M" y "INEXISTENTE-M" y total de créditos optativos 20
    Entonces no se puede modificar el plan de estudios por materia inexistente
