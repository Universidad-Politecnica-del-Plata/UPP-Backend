#language: es
@todo
Característica: Dar de alta cuota
#Como responsable de Administración y Cobranzas quiero registrar nuevas cuotas para reflejar el costo de la cursada
  Escenario: : Dar de alta cuota es exitoso
    Dado que existen cursadas activas
    Y no existe una cuota con "año" y "mes"
    Cuando se registra una cuota con "año", "mes" e "importe"
    Entonces se registra una nueva cuota en el sistema

  Escenario: : Dar de alta cuota con año y mes ya existente falla
    Dado que existe una cuota con "2024" y "03"
    Cuando se registra una cuota con "2024", "03" e "importe"
    Entonces no se registra la cuota porque ya existe

  Escenario: : Dar de alta cuota con importe inválido falla
    Dado que existen cursadas activas
    Y no existe una cuota con "2024" y "04"
    Cuando se registra una cuota con "2024", "04" e importe negativo
    Entonces no se registra la cuota por importe inválido

