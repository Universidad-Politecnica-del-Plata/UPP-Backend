#language: es
@todo
Característica: Abrir acta

#  Hacer tabla con valores Cursada y Final
  Escenario: : Abrir acta de Cursada/Final es exitoso
    Dado que existe un docente con nombre "Luis", apellido "Gomez" y DNI "23456789"
    Y tiene un curso a su nombre
    Y no hay un acta de "Cursada/Final" abierta para el curso en el cuatrimiestre
    Cuando abre un acta de "Cursada/Final" para el curso en el cuatrimestre
    Entonces el acta queda en estado "Abierta"


