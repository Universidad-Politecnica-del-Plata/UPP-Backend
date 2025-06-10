#language: es
@todo
Característica: Dar de alta plan de estudios

  Escenario: : Dar de alta plan de estudios es exitoso
    Dado que existe un responsable de Gestión Académica
    Y existe un plan de estudios
    Cuando se registra un nuevo plan de estudios en el sistema con "código de plan de estudios", "carrera correspondiente", "fecha de entrada en vigencia", "fecha de vencimiento", "total de créditos académicos obligatorios" y "total de créditos académicos optativos"
    Entonces se registra el plan de estudios

