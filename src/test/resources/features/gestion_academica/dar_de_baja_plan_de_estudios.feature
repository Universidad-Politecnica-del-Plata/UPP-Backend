#language: es
Característica: Dar de baja plan de estudios

  Escenario: : Dar de baja plan de estudios es exitoso
    Dado que existe un responsable de Gestión Académica
    Y existe un plan de estudios con "código de plan de estudios", "carrera correspondiente", "fecha de entrada en vigencia", "fecha de vencimiento", "total de créditos académicos obligatorios" y "total de créditos académicos optativos"
    Cuando se da de baja el plan de estudios
    Entonces se elimina el registro del plan de estudios
