#language: es
@todo
Característica: Dar de alta cuota
#Como responsable de Administración y Cobranzas quiero registrar nuevas cuotas para reflejar el costo de la cursada
  Escenario: : Dar de alta cuota es exitoso
    Dado que existen cursadas activas
    Y no existe una cuota con "año" y "mes"
    Cuando se registra una cuota con "año", "mes" e "importe"
    Entonces se registra una nueva cuota en el sistema

