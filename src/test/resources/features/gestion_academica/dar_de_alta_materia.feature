#language: es
Característica: Dar de alta materia

  Escenario:  Dar de alta materia sin correlativa es exitoso
    Cuando se registra una materia con código de materia "123-M", nombre "Literatura I", contenidos "Literatura Neoclasica", tipo de materia "Optativa", cantidad de créditos que otorga 4 y créditos necesarios 0
    Entonces se registra la materia exitosamente

  Escenario:  Dar de alta materia con correlativa es exitoso
    Dado que existe una materia con el código de materia "123-M" y nombre "Analisis I"
    Cuando se registra una materia con código de materia "456-A", nombre "Analisis II", contenidos "Funciones, Derivadas e Integrales", tipo de materia "Obligatoria", con correlativa "Analisis I", cantidad de créditos que otorga 6 y créditos necesarios 0
    Entonces se registra la materia exitosamente


