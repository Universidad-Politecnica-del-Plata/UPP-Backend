#!/bin/bash

set -e

APP_PORT=${PORT:-8080}
BASE_URL="${1:-http://localhost:${APP_PORT}}"
API_URL="$BASE_URL/api"

echo "init: $API_URL"

# Función auxiliar para hacer llamadas a la API
hacer_peticion() {
    local metodo=$1
    local endpoint=$2
    local datos=$3
    local header_auth=$4
    
    if [ -n "$header_auth" ]; then
        curl -s -X "$metodo" \
             -H "Content-Type: application/json" \
             -H "Authorization: Bearer $header_auth" \
             -d "$datos" \
             "$API_URL$endpoint"
    else
        curl -s -X "$metodo" \
             -H "Content-Type: application/json" \
             -d "$datos" \
             "$API_URL$endpoint"
    fi
}

# Función auxiliar para extraer token de la respuesta de login
extraer_token() {
    echo "$1" | grep -o '"token":"[^"]*"' | cut -d'"' -f4
}

echo "creando usuarios..."
respuesta_academica=$(hacer_peticion "POST" "/auth/register" '{
    "username": "gestion_academica",
    "password": "password123",
    "roles": ["ROLE_GESTION_ACADEMICA"]
}')

respuesta_planificacion=$(hacer_peticion "POST" "/auth/register" '{
    "username": "gestor_planificacion", 
    "password": "password123",
    "roles": ["ROLE_GESTOR_DE_PLANIFICACION"]
}')

respuesta_estudiantil=$(hacer_peticion "POST" "/auth/register" '{
    "username": "gestion_estudiantil",
    "password": "password123",
    "roles": ["ROLE_GESTION_ESTUDIANTIL"]
}')

respuesta_docente=$(hacer_peticion "POST" "/auth/register" '{
    "username": "docente",
    "password": "password123",
    "roles": ["ROLE_DOCENTE"]
}')

echo "obteniendo tokens..."
login_academica=$(hacer_peticion "POST" "/auth/login" '{
    "username": "gestion_academica",
    "password": "password123"
}')
TOKEN_ACADEMICA=$(extraer_token "$login_academica")

login_planificacion=$(hacer_peticion "POST" "/auth/login" '{
    "username": "gestor_planificacion", 
    "password": "password123"
}')
TOKEN_PLANIFICACION=$(extraer_token "$login_planificacion")

login_estudiantil=$(hacer_peticion "POST" "/auth/login" '{
    "username": "gestion_estudiantil",
    "password": "password123"
}')
TOKEN_ESTUDIANTIL=$(extraer_token "$login_estudiantil")

echo "creando materias..."
materias=(
    '{"codigoDeMateria":"997-MA","nombre":"Analisis I","contenidos":"Contenido de Analisis I","creditosQueOtorga":4,"creditosNecesarios":0,"tipo":"OBLIGATORIA","cuatrimestre":1,"codigoPlanDeEstudios":null,"codigosCorrelativas":[]}'
    '{"codigoDeMateria":"998-MA","nombre":"Algebra I","contenidos":"Contenido de Algebra I","creditosQueOtorga":4,"creditosNecesarios":0,"tipo":"OBLIGATORIA","cuatrimestre":1,"codigoPlanDeEstudios":null,"codigosCorrelativas":[]}'
    '{"codigoDeMateria":"999-MA","nombre":"Algoritmos y Programacion I","contenidos":"Contenido de Algoritmos y Programacion I","creditosQueOtorga":6,"creditosNecesarios":0,"tipo":"OBLIGATORIA","cuatrimestre":1,"codigoPlanDeEstudios":null,"codigosCorrelativas":[]}'
    '{"codigoDeMateria":"CORRELATIVA","nombre":"Algebra II","contenidos":"Contenido de Algebra II","creditosQueOtorga":4,"creditosNecesarios":0,"tipo":"OBLIGATORIA","cuatrimestre":2,"codigoPlanDeEstudios":null,"codigosCorrelativas":[]}'
    '{"codigoDeMateria":"MateriaQueNecesitaCredito","nombre":"Algebra Lineal","contenidos":"Contenido de Algebra Lineal","creditosQueOtorga":4,"creditosNecesarios":1,"tipo":"OBLIGATORIA","cuatrimestre":3,"codigoPlanDeEstudios":null,"codigosCorrelativas":[]}'
    '{"codigoDeMateria":"MateriaDeElMillonDeCreditos","nombre":"Algebra Imposible","contenidos":"Contenido de Algebra Imposible","creditosQueOtorga":4,"creditosNecesarios":1000000,"tipo":"OBLIGATORIA","cuatrimestre":8,"codigoPlanDeEstudios":null,"codigosCorrelativas":[]}'
)

for i in "${!materias[@]}"; do
    hacer_peticion "POST" "/materias" "${materias[$i]}" "$TOKEN_ACADEMICA" > /dev/null
done
hacer_peticion "POST" "/materias" '{
    "codigoDeMateria": "NECESITA-CORRELATIVA",
    "nombre": "Algebra III", 
    "contenidos": "Contenido de Algebra III",
    "creditosQueOtorga": 4,
    "creditosNecesarios": 0,
    "tipo": "OBLIGATORIA",
    "cuatrimestre": 3,
    "codigoPlanDeEstudios": null,
    "codigosCorrelativas": ["CORRELATIVA"]
}' "$TOKEN_ACADEMICA" > /dev/null

hacer_peticion "POST" "/materias" '{
    "codigoDeMateria": "NECESITA-2-CORRELATIVAS",
    "nombre": "Algebra 4D",
    "contenidos": "Contenido de Algebra 4D", 
    "creditosQueOtorga": 4,
    "creditosNecesarios": 0,
    "tipo": "OBLIGATORIA",
    "cuatrimestre": 4,
    "codigoPlanDeEstudios": null, 
    "codigosCorrelativas": ["CORRELATIVA", "998-MA"]
}' "$TOKEN_ACADEMICA" > /dev/null

echo "creando planes de estudio..."
hacer_peticion "POST" "/planDeEstudios" '{
    "codigoDePlanDeEstudios": "PLAN-2023",
    "creditosElectivos": 10,
    "fechaEntradaEnVigencia": "2023-01-01", 
    "fechaVencimiento": "2030-12-31",
    "codigosMaterias": ["997-MA", "998-MA", "999-MA", "CORRELATIVA", "MateriaQueNecesitaCredito", "MateriaDeElMillonDeCreditos", "NECESITA-CORRELATIVA", "NECESITA-2-CORRELATIVAS"]
}' "$TOKEN_ACADEMICA" > /dev/null

hacer_peticion "POST" "/planDeEstudios" '{
    "codigoDePlanDeEstudios": "PLAN-2025",
    "creditosElectivos": 10,
    "fechaEntradaEnVigencia": "2025-01-01",
    "fechaVencimiento": "2030-12-31",
    "codigosMaterias": ["997-MA", "998-MA", "999-MA"]
}' "$TOKEN_ACADEMICA" > /dev/null


echo "creando carrera..."
hacer_peticion "POST" "/carreras" '{
    "codigoDeCarrera": "ING-SIS",
    "nombre": "Ingenieria en Sistemas",
    "titulo": "Ingeniero en Sistemas",
    "incumbencias": "Desarrollo de software y sistemas",
    "codigosPlanesDeEstudio": ["PLAN-2023","PLAN-2025"]
}' "$TOKEN_ACADEMICA" > /dev/null

echo "creando cuatrimestre..."
hacer_peticion "POST" "/cuatrimestres" '{
    "codigo": "2025-2",
    "fechaDeInicioClases": "9999-02-01",
    "fechaDeFinClases": "9999-07-15", 
    "fechaInicioPeriodoDeInscripcion": "2025-01-01",
    "fechaFinPeriodoDeInscripcion": "9999-01-31",
    "fechaInicioPeriodoIntegradores": "9999-07-16",
    "fechaFinPeriodoIntegradores": "9999-07-31",
    "codigosCursos": []
}' "$TOKEN_PLANIFICACION" > /dev/null

echo "creando cursos..."
cursos=(
    '{"codigo":"CURSO-001","maximoDeAlumnos":25,"codigoMateria":"997-MA","codigosCuatrimestres":["2025-2"]}'
    '{"codigo":"CURSO-002","maximoDeAlumnos":25,"codigoMateria":"998-MA","codigosCuatrimestres":["2025-2"]}'
    '{"codigo":"CURSO-Correlativa-inicial","maximoDeAlumnos":25,"codigoMateria":"CORRELATIVA","codigosCuatrimestres":["2025-2"]}'
    '{"codigo":"CURSO-Correlativas","maximoDeAlumnos":25,"codigoMateria":"NECESITA-CORRELATIVA","codigosCuatrimestres":["2025-2"]}'
    '{"codigo":"CURSO-que-necesita-creditos","maximoDeAlumnos":25,"codigoMateria":"MateriaQueNecesitaCredito","codigosCuatrimestres":["2025-2"]}'
    '{"codigo":"CURSO-que-necesita-un-millon-creditos","maximoDeAlumnos":25,"codigoMateria":"MateriaDeElMillonDeCreditos","codigosCuatrimestres":["2025-2"]}'
    '{"codigo":"CURSO-NECESITA-2-CORRELATIVAS","maximoDeAlumnos":25,"codigoMateria":"NECESITA-2-CORRELATIVAS","codigosCuatrimestres":["2025-2"]}'
)

for i in "${!cursos[@]}"; do
    hacer_peticion "POST" "/cursos" "${cursos[$i]}" "$TOKEN_PLANIFICACION" > /dev/null
done

echo "creando alumno..."
hacer_peticion "POST" "/alumnos" '{
    "matricula": 12345,
    "nombre": "Juan",
    "apellido": "Perez",
    "dni": 12345,
    "email": "juan@mail.com",
    "direccion": "Calle Falsa 123",
    "fechaNacimiento": "1990-01-01",
    "fechaIngreso": "2015-03-01",
    "fechaEgreso": null,
    "telefonos": ["1234"],
    "codigosCarreras": ["ING-SIS"],
    "codigosPlanesDeEstudio": ["PLAN-2023", "PLAN-2025"]
}' "$TOKEN_ESTUDIANTIL" > /dev/null

echo "fin"
