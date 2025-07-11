#language: es
@todo
Característica: Modificar datos de un plan de estudios

  Antecedentes:
    Dado que hay un gestor academico logueado
    Y que existe una materia con el código de materia "1234-M" y nombre "Analisis I"
    Y que existe una materia con el código de materia "1235-M" y nombre "Algebra I"
    Y que existe una materia con el código de materia "1236-M" y nombre "Algoritmos y Programacion I"
    Y que existe una materia con el código de materia "1237-M" y nombre "Algoritmos y Programacion II"
    Y se registra un nuevo plan de estudios con codigo "P1-2025", fecha de entrada en vigencia "01-01-2025", fecha de vencimiento "31-12-2035", materias en el plan "1234-M", "1235-M" y "1236-M" y total de créditos optativos 20

  Escenario: Modificar datos de plan de estudios es exitoso

    Cuando al plan de estudios con código "P1-2025" se modifica fecha de entrada en vigencia "31-01-2025", fecha de vencimiento "31-12-2035", materias en el plan "1234-M", "1235-M" y "1236-M" y total de créditos optativos 20
    Entonces se actualiza la información de la materia exitosamente
    Entonces la materia "123-M" tiene nombre "Literatura II", contenidos "Literatura Griega", cantidad de créditos que otorga 6 y créditos necesarios 4


  Escenario: : Modificar datos de una carrera exitoso
    Dado que existe un responsable de Gestión Académica
    Y existe un plan de estudios con "código de plan de estudios", "carrera correspondiente", "fecha de entrada en vigencia", "fecha de vencimiento", "total de créditos académicos obligatorios" y "total de créditos académicos optativos"
    Cuando se modifica el plan de estudios en el sistema con "código de plan de estudios", "carrera correspondiente", "fecha de entrada en vigencia", "fecha de vencimiento", "total de créditos académicos obligatorios" y "total de créditos académicos optativos"
    Entonces se actualiza la información del plan de estudios

