#language: es
@todo
Característica: Dar de alta cuota
#Como responsable de Administración y Cobranzas quiero registrar nuevas cuotas para reflejar el costo de la cursada
  Escenario: : Dar de alta cuota es exitoso
    Cuando se registra una cuota con "año", "mes" e "importe"
    Entonces se registra una nueva cuota en el sistema

