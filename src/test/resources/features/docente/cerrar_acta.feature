#language: es
Característica: Cerrar acta

#  Hacer tabla con valores Cursada y Final
  Escenario: : Cerrar acta de Cursada/Final es exitoso
    Dado que existe un docente con nombre "Luis", apellido "Gomez" y DNI "23456789"
    Y tiene un curso a su nombre
    Y hay un acta de "Cursada/Final" abierta para el curso en el cuatrimiestre
    Cuando cierra un acta de "Cursada/Final" para el curso en el cuatrimestre
    Entonces el acta queda en estado "Cerrada"
