#language: es
Característica: Modificar datos de una materia

  Escenario: : Modificar todos datos de una materia es exitoso
    Dado se registra una materia con código de materia "123-M", nombre "Literatura I", contenidos "Literatura Neoclasica", tipo de materia "Optativa", cantidad de créditos que otorga 4 y créditos necesarios 0
    Y que existe una materia con el código de materia "123-A" y nombre "Analisis I"
    Cuando a la materia con código de materia "123-M" se modifica nombre "Literatura II", contenidos "Literatura Griega", tipo de materia "Optativa", con correlativa "Analisis I", cantidad de créditos que otorga 6 y créditos necesarios 4
    Entonces se actualiza la información de la materia exitosamente
    Entonces la materia "123-M" tiene nombre "Literatura II", contenidos "Literatura Griega", cantidad de créditos que otorga 6 y créditos necesarios 4

  Escenario: : Modificar datos de una materia que no existe
    Cuando a la materia con código de materia "123-M" se modifica nombre "Literatura II", contenidos "Literatura Griega", tipo de materia "Optativa", con correlativa "Analisis I", cantidad de créditos que otorga 6 y créditos necesarios 4
    Entonces no existe la materia "123-M" en el registro