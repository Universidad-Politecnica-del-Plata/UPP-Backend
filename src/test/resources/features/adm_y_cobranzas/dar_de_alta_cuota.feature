#language: es
@todo
Característica: Dar de alta cuota

  Escenario: : Dar de alta cuota es exitoso
    Dado que existe un responsable de Administración y Cobranzas
    Cuando se registra una cuota con "año", "mes" e "importe"
    Entonces se registra una nueva cuota en el sistema

