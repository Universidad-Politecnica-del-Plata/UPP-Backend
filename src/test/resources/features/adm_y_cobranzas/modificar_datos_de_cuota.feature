#language: es
Característica: Modificar datos de cuota

  Escenario: : Modificar cuota es exitoso
    Dado que existe un responsable de Administración y Cobranzas
    Y existe una cuota con "año", "mes" e "importe"
    Cuando se modifica la cuota con "año", "mes" e "importe"
    Entonces se actualiza la informacion del cuota en el sistema

