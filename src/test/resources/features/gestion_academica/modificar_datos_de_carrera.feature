#language: es
@todo
Característica: Modificar datos de una carrera

  Escenario: : Modificar datos de una carrera exitoso
    Dado que existe un responsable de Gestión Académica
    Y existe una carrera con "código de carrera", "nombre", "título" oficial que otorga e "incumbencias"
    Cuando se modifica la informacion de la carrera con "código de carrera", "nombre", "título" oficial que otorga e "incumbencias"
    Entonces se actualiza la información de la carrera

