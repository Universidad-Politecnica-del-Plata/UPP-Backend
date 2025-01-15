#language: es
Característica: Modificar datos de una carrera

  Escenario: : Modificar datos de una carrera exitoso
    Dado que existe un responsable de Gestión Académica
    Y existe un plan de estudios con "código de plan de estudios", "carrera correspondiente", "fecha de entrada en vigencia", "fecha de vencimiento", "total de créditos académicos obligatorios" y "total de créditos académicos optativos"
    Cuando se modifica el plan de estudios en el sistema con "código de plan de estudios", "carrera correspondiente", "fecha de entrada en vigencia", "fecha de vencimiento", "total de créditos académicos obligatorios" y "total de créditos académicos optativos"
    Entonces se actualiza la información del plan de estudios

