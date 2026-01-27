#!/bin/bash

# Script de Inicialización de Datos de Prueba

set -e  # Salir en caso de error

# Configuración
BASE_URL="${1:-http://localhost:8080}"
API_URL="$BASE_URL/api"

echo "Inicializando Datos de Prueba"
echo "Usando URL de API: $API_URL"
echo "=================================================="

# Códigos de color para la salida
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # Sin Color

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

echo -e "${BLUE}Paso 1: Creando usuarios del sistema...${NC}"

# Crear Usuario de Gestión Académica
echo "Creando usuario de Gestión Académica..."
respuesta_academica=$(hacer_peticion "POST" "/auth/register" '{
    "username": "gestion_academica",
    "password": "password123",
    "roles": ["ROLE_GESTION_ACADEMICA"]
}')

# Crear Usuario de Gestión de Planificación  
echo "Creando usuario de Gestión de Planificación..."
respuesta_planificacion=$(hacer_peticion "POST" "/auth/register" '{
    "username": "gestor_planificacion", 
    "password": "password123",
    "roles": ["ROLE_GESTOR_DE_PLANIFICACION"]
}')

# Crear Usuario de Gestión Estudiantil
echo "Creando usuario de Gestión Estudiantil..."
respuesta_estudiantil=$(hacer_peticion "POST" "/auth/register" '{
    "username": "gestion_estudiantil",
    "password": "password123",
    "roles": ["ROLE_GESTION_ESTUDIANTIL"]
}')

# Crear Usuario Docente
echo "Creando usuario Docente..."
respuesta_docente=$(hacer_peticion "POST" "/auth/register" '{
    "username": "docente",
    "password": "password123",
    "roles": ["ROLE_DOCENTE"]
}')

echo -e "${GREEN}Usuarios del sistema creados correctamente${NC}"

echo -e "${BLUE}Paso 2: Iniciando sesión para obtener tokens de autenticación...${NC}"

# Login como usuario de Gestión Académica
login_academica=$(hacer_peticion "POST" "/auth/login" '{
    "username": "gestion_academica",
    "password": "password123"
}')
TOKEN_ACADEMICA=$(extraer_token "$login_academica")

# Login como usuario de Gestión de Planificación
login_planificacion=$(hacer_peticion "POST" "/auth/login" '{
    "username": "gestor_planificacion", 
    "password": "password123"
}')
TOKEN_PLANIFICACION=$(extraer_token "$login_planificacion")

# Login como usuario de Gestión Estudiantil
login_estudiantil=$(hacer_peticion "POST" "/auth/login" '{
    "username": "gestion_estudiantil",
    "password": "password123"
}')
TOKEN_ESTUDIANTIL=$(extraer_token "$login_estudiantil")

echo -e "${GREEN}Tokens de autenticación obtenidos${NC}"

echo -e "${BLUE}Paso 3: Creando materias de prueba...${NC}"

# Array de materias a crear
materias=(
    '{"codigoDeMateria":"997-MA","nombre":"Analisis I","contenidos":"Contenido de Analisis I","creditosQueOtorga":4,"creditosNecesarios":0,"tipo":"OBLIGATORIA","cuatrimestre":1,"codigoPlanDeEstudios":null,"codigosCorrelativas":[]}'
    '{"codigoDeMateria":"998-MA","nombre":"Algebra I","contenidos":"Contenido de Algebra I","creditosQueOtorga":4,"creditosNecesarios":0,"tipo":"OBLIGATORIA","cuatrimestre":1,"codigoPlanDeEstudios":null,"codigosCorrelativas":[]}'
    '{"codigoDeMateria":"999-MA","nombre":"Algoritmos y Programacion I","contenidos":"Contenido de Algoritmos y Programacion I","creditosQueOtorga":6,"creditosNecesarios":0,"tipo":"OBLIGATORIA","cuatrimestre":1,"codigoPlanDeEstudios":null,"codigosCorrelativas":[]}'
    '{"codigoDeMateria":"CORRELATIVA","nombre":"Algebra II","contenidos":"Contenido de Algebra II","creditosQueOtorga":4,"creditosNecesarios":0,"tipo":"OBLIGATORIA","cuatrimestre":2,"codigoPlanDeEstudios":null,"codigosCorrelativas":[]}'
    '{"codigoDeMateria":"MateriaQueNecesitaCredito","nombre":"Algebra Lineal","contenidos":"Contenido de Algebra Lineal","creditosQueOtorga":4,"creditosNecesarios":1,"tipo":"OBLIGATORIA","cuatrimestre":3,"codigoPlanDeEstudios":null,"codigosCorrelativas":[]}'
    '{"codigoDeMateria":"MateriaDeElMillonDeCreditos","nombre":"Algebra Imposible","contenidos":"Contenido de Algebra Imposible","creditosQueOtorga":4,"creditosNecesarios":1000000,"tipo":"OBLIGATORIA","cuatrimestre":8,"codigoPlanDeEstudios":null,"codigosCorrelativas":[]}'
)

nombres_materias=("Analisis I" "Algebra I" "Algoritmos y Programacion I" "Algebra II" "Algebra Lineal" "Algebra Imposible")

for i in "${!materias[@]}"; do
    echo "Creando materia: ${nombres_materias[$i]}..."
    hacer_peticion "POST" "/materias" "${materias[$i]}" "$TOKEN_ACADEMICA" > /dev/null
done

# Crear materias con correlativas
echo "Creando materia: Algebra III (requiere Algebra II)..."
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

echo "Creando materia: Algebra 4D (requiere Algebra II + Algebra I)..."
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

echo -e "${GREEN}Todas las materias creadas${NC}"

echo -e "${BLUE}Paso 4: Creando plan de estudios...${NC}"

# Crear plan de estudios con todas las materias
echo "Creando plan de estudios con todas las materias..."
hacer_peticion "POST" "/planDeEstudios" '{
    "codigoDePlanDeEstudios": "PLAN-2023",
    "creditosElectivos": 10,
    "fechaEntradaEnVigencia": "2023-01-01", 
    "fechaVencimiento": "2030-12-31",
    "codigosMaterias": ["997-MA", "998-MA", "999-MA", "CORRELATIVA", "MateriaQueNecesitaCredito", "MateriaDeElMillonDeCreditos", "NECESITA CORRELATIVA", "NECESITA 2 CORRELATIVAS"]
}' "$TOKEN_ACADEMICA" > /dev/null

echo "Creando plan de estudios con todas las materias..."
hacer_peticion "POST" "/planDeEstudios" '{
    "codigoDePlanDeEstudios": "PLAN-2025",
    "creditosElectivos": 10,
    "fechaEntradaEnVigencia": "2025-01-01",
    "fechaVencimiento": "2030-12-31",
    "codigosMaterias": ["997-MA", "998-MA", "999-MA"]
}' "$TOKEN_ACADEMICA" > /dev/null


echo -e "${GREEN}Plan de estudios creado${NC}"

echo -e "${BLUE}Paso 5: Creando carrera...${NC}"

# Crear carrera
echo "Creando carrera..."
hacer_peticion "POST" "/carreras" '{
    "codigoDeCarrera": "ING-SIS",
    "nombre": "Ingenieria en Sistemas",
    "titulo": "Ingeniero en Sistemas",
    "incumbencias": "Desarrollo de software y sistemas",
    "codigosPlanesDeEstudio": ["PLAN-2023","PLAN-2025"]
}' "$TOKEN_ACADEMICA" > /dev/null

echo -e "${GREEN}Carrera creada${NC}"

echo -e "${BLUE}Paso 6: Creando cuatrimestre de prueba...${NC}"

# Crear cuatrimestre con período de inscripción abierto
echo "Creando cuatrimestre 2025-2..."
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

echo -e "${GREEN}Cuatrimestre creado${NC}"

echo -e "${BLUE}Paso 7: Creando cursos de prueba...${NC}"

# Array de cursos a crear
cursos=(
    '{"codigo":"CURSO-001","maximoDeAlumnos":25,"codigoMateria":"997-MA","codigosCuatrimestres":["2025-2"]}'
    '{"codigo":"CURSO-002","maximoDeAlumnos":25,"codigoMateria":"998-MA","codigosCuatrimestres":["2025-2"]}'
    '{"codigo":"CURSO-Correlativa-inicial","maximoDeAlumnos":25,"codigoMateria":"CORRELATIVA","codigosCuatrimestres":["2025-2"]}'
    '{"codigo":"CURSO-Correlativas","maximoDeAlumnos":25,"codigoMateria":"NECESITA CORRELATIVA","codigosCuatrimestres":["2025-2"]}'
    '{"codigo":"CURSO-que-necesita-creditos","maximoDeAlumnos":25,"codigoMateria":"MateriaQueNecesitaCredito","codigosCuatrimestres":["2025-2"]}'
    '{"codigo":"CURSO-que-necesita-un-millon-creditos","maximoDeAlumnos":25,"codigoMateria":"MateriaDeElMillonDeCreditos","codigosCuatrimestres":["2025-2"]}'
    '{"codigo":"CURSO-NECESITA-2-CORRELATIVAS","maximoDeAlumnos":25,"codigoMateria":"NECESITA 2 CORRELATIVAS","codigosCuatrimestres":["2025-2"]}'
)

nombres_cursos=("CURSO-001 (Analisis I)" "CURSO-002 (Algebra I)" "CURSO-Correlativa inicial (Algebra II)" "CURSO-Correlativas (Algebra III)" "CURSO-que-necesita-creditos (Algebra Lineal)" "CURSO-que-necesita-un-millon-creditos (Algebra Imposible)" "CURSO-NECESITA 2 CORRELATIVAS (Algebra 4D)")

for i in "${!cursos[@]}"; do
    echo "Creando curso: ${nombres_cursos[$i]}..."
    hacer_peticion "POST" "/cursos" "${cursos[$i]}" "$TOKEN_PLANIFICACION" > /dev/null
done

echo -e "${GREEN}Todos los cursos creados${NC}"

echo -e "${BLUE}Paso 8: Creando alumno de prueba...${NC}"

# Crear perfil del alumno de prueba
echo "Creando perfil del alumno..."
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

echo -e "${GREEN}Alumno de prueba creado${NC}"

echo ""
echo -e "${GREEN}INICIALIZACIÓN DE DATOS DE PRUEBA COMPLETADA${NC}"
echo "=================================================="
echo ""
echo -e "${YELLOW}RESUMEN DE DATOS DE PRUEBA:${NC}"
echo "================================"
echo ""
echo -e "${BLUE}Credenciales del Alumno de Prueba:${NC}"
echo "   Usuario: 12345"
echo "   Contraseña: 12345"
echo "   DNI: 12345"
echo ""
echo -e "${BLUE}Cursos Disponibles para Pruebas:${NC}"
echo "   - CURSO-001 (Analisis I) - Inscripción básica"
echo "   - CURSO-002 (Algebra I) - Inscripción básica" 
echo "   - CURSO-Correlativa inicial (Algebra II) - Configuración de correlativas"
echo "   - CURSO-Correlativas (Algebra III) - Requiere Algebra II"
echo "   - CURSO-que-necesita-creditos (Algebra Lineal) - Requiere 1 crédito"
echo "   - CURSO-que-necesita-un-millon-creditos (Algebra Imposible) - Requiere 1M créditos"
echo "   - CURSO-NECESITA 2 CORRELATIVAS (Algebra 4D) - Requiere 2 correlativas"
echo ""
echo -e "${BLUE}Cuatrimestre Activo:${NC} 2025-2 (período de inscripción abierto)"
echo ""
echo -e "${BLUE}Usuarios del Sistema Creados:${NC}"
echo "   - gestion_academica (contraseña: password123) - Gestión académica"
echo "   - gestor_planificacion (contraseña: password123) - Gestión de planificación"
echo "   - gestion_estudiantil (contraseña: password123) - Gestión estudiantil"
echo "   - docente (contraseña: password123) - Docente"
echo ""
echo -e "${BLUE}FIN${NC}"
