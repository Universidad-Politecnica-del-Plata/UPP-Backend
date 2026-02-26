#language: es
@todo
Característica: Modificar datos de cuota
#  Como responsable de Administración y Cobranzas quiero modificar la información de una cuota para reflejar las actualizaciones de aranceles
  Escenario: : Modificar cuota es exitoso
    Dado existe una cuota con "año", "mes" e "importe"
    Cuando se modifica la cuota con "año", "mes" e "importe"
    Entonces se actualiza la informacion del cuota en el sistema

  Escenario: : Modificar cuota inexistente falla
    Dado que no existe una cuota con "2099" y "12"
    Cuando se intenta modificar la cuota con "2099", "12" e "importe"
    Entonces no se puede modificar la cuota porque no existe

  Escenario: : Modificar cuota con importe inválido falla
    Dado existe una cuota con "2024", "03" e "50000"
    Cuando se intenta modificar la cuota con "2024", "03" e importe negativo
    Entonces no se puede modificar la cuota por importe inválido

