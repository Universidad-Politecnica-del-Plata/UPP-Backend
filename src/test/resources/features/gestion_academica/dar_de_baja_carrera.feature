#language: es
@todo
Característica: Dar de baja carrera

  Escenario: : Dar de baja carrera es exitoso
    Dado que existe un responsable de Gestión Académica
    Y existe una carrera con "código de carrera", "nombre", "título" oficial que otorga e "incumbencias"
    Cuando se da de baja la carrera
    Entonces se elimina el registro de la carrera
