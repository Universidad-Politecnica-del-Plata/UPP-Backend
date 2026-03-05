#!/bin/bash

set -e

APP_PORT=${PORT:-8080}
BASE_URL="${1:-http://localhost:${APP_PORT}}"
API_URL="$BASE_URL/api"

echo "Inicializando datos en: $API_URL"

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

echo "Creando usuarios del sistema..."
hacer_peticion "POST" "/auth/register" '{
    "username": "gestion_academica",
    "password": "password123",
    "roles": ["ROLE_GESTION_ACADEMICA"]
}' > /dev/null

hacer_peticion "POST" "/auth/register" '{
    "username": "gestor_planificacion",
    "password": "password123",
    "roles": ["ROLE_GESTOR_DE_PLANIFICACION"]
}' > /dev/null

hacer_peticion "POST" "/auth/register" '{
    "username": "gestion_estudiantil",
    "password": "password123",
    "roles": ["ROLE_GESTION_ESTUDIANTIL"]
}' > /dev/null

hacer_peticion "POST" "/auth/register" '{
    "username": "docente",
    "password": "password123",
    "roles": ["ROLE_DOCENTE"]
}' > /dev/null

echo "Obteniendo tokens de autenticación..."
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

echo "Creando materias de Ingeniería en Sistemas..."
materias_sistemas=(
    '{"codigoDeMateria":"SIS-AM1","nombre":"Análisis Matemático I","contenidos":"Límites y continuidad. Derivadas y aplicaciones. Integrales definidas e indefinidas. Series numéricas.","creditosQueOtorga":8,"creditosNecesarios":0,"tipo":"OBLIGATORIA","cuatrimestre":1,"codigoPlanDeEstudios":null,"codigosCorrelativas":[]}'
    '{"codigoDeMateria":"SIS-ALG","nombre":"Álgebra y Geometría Analítica","contenidos":"Vectores y matrices. Sistemas de ecuaciones lineales. Espacios vectoriales. Transformaciones lineales.","creditosQueOtorga":8,"creditosNecesarios":0,"tipo":"OBLIGATORIA","cuatrimestre":1,"codigoPlanDeEstudios":null,"codigosCorrelativas":[]}'
    '{"codigoDeMateria":"SIS-FIS1","nombre":"Física I","contenidos":"Mecánica del punto material. Cinemática y dinámica. Trabajo y energía. Oscilaciones.","creditosQueOtorga":6,"creditosNecesarios":0,"tipo":"OBLIGATORIA","cuatrimestre":1,"codigoPlanDeEstudios":null,"codigosCorrelativas":[]}'
    '{"codigoDeMateria":"SIS-QUI","nombre":"Química General","contenidos":"Estructura atómica. Enlaces químicos. Estequiometría. Termodinámica química básica.","creditosQueOtorga":4,"creditosNecesarios":0,"tipo":"OBLIGATORIA","cuatrimestre":1,"codigoPlanDeEstudios":null,"codigosCorrelativas":[]}'
    '{"codigoDeMateria":"SIS-SYO","nombre":"Sistemas y Organizaciones","contenidos":"Teoría de sistemas. Estructuras organizacionales. Gestión de procesos. Calidad.","creditosQueOtorga":4,"creditosNecesarios":0,"tipo":"OBLIGATORIA","cuatrimestre":2,"codigoPlanDeEstudios":null,"codigosCorrelativas":[]}'
    '{"codigoDeMateria":"SIS-AED","nombre":"Algoritmos y Estructuras de Datos","contenidos":"Estructuras de datos lineales y jerárquicas. Algoritmos de ordenamiento y búsqueda. Complejidad computacional.","creditosQueOtorga":6,"creditosNecesarios":0,"tipo":"OBLIGATORIA","cuatrimestre":3,"codigoPlanDeEstudios":null,"codigosCorrelativas":[]}'
)

for materia in "${materias_sistemas[@]}"; do
    hacer_peticion "POST" "/materias" "$materia" "$TOKEN_ACADEMICA" > /dev/null
done

# Materias de Sistemas con correlativas
hacer_peticion "POST" "/materias" '{
    "codigoDeMateria": "SIS-AM2",
    "nombre": "Análisis Matemático II",
    "contenidos": "Funciones de varias variables. Integrales múltiples. Ecuaciones diferenciales ordinarias.",
    "creditosQueOtorga": 8,
    "creditosNecesarios": 0,
    "tipo": "OBLIGATORIA",
    "cuatrimestre": 2,
    "codigoPlanDeEstudios": null,
    "codigosCorrelativas": ["SIS-AM1"]
}' "$TOKEN_ACADEMICA" > /dev/null

hacer_peticion "POST" "/materias" '{
    "codigoDeMateria": "SIS-FIS2",
    "nombre": "Física II",
    "contenidos": "Electrostática y magnetostática. Circuitos eléctricos. Ondas electromagnéticas. Óptica.",
    "creditosQueOtorga": 6,
    "creditosNecesarios": 0,
    "tipo": "OBLIGATORIA",
    "cuatrimestre": 2,
    "codigoPlanDeEstudios": null,
    "codigosCorrelativas": ["SIS-FIS1", "SIS-AM1"]
}' "$TOKEN_ACADEMICA" > /dev/null

hacer_peticion "POST" "/materias" '{
    "codigoDeMateria": "SIS-PROB",
    "nombre": "Probabilidad y Estadística",
    "contenidos": "Variables aleatorias. Distribuciones de probabilidad. Inferencia estadística. Regresión.",
    "creditosQueOtorga": 4,
    "creditosNecesarios": 0,
    "tipo": "OBLIGATORIA",
    "cuatrimestre": 2,
    "codigoPlanDeEstudios": null,
    "codigosCorrelativas": ["SIS-AM1"]
}' "$TOKEN_ACADEMICA" > /dev/null

hacer_peticion "POST" "/materias" '{
    "codigoDeMateria": "SIS-AEDII",
    "nombre": "Algoritmos y Estructuras de Datos II",
    "contenidos": "Grafos y árboles avanzados. Programación dinámica. Algoritmos de optimización.",
    "creditosQueOtorga": 6,
    "creditosNecesarios": 0,
    "tipo": "OBLIGATORIA",
    "cuatrimestre": 4,
    "codigoPlanDeEstudios": null,
    "codigosCorrelativas": ["SIS-AED"]
}' "$TOKEN_ACADEMICA" > /dev/null

hacer_peticion "POST" "/materias" '{
    "codigoDeMateria": "SIS-BD",
    "nombre": "Bases de Datos",
    "contenidos": "Modelo relacional. SQL. Normalización. Transacciones. Diseño de bases de datos.",
    "creditosQueOtorga": 6,
    "creditosNecesarios": 0,
    "tipo": "OBLIGATORIA",
    "cuatrimestre": 4,
    "codigoPlanDeEstudios": null,
    "codigosCorrelativas": ["SIS-AED"]
}' "$TOKEN_ACADEMICA" > /dev/null

hacer_peticion "POST" "/materias" '{
    "codigoDeMateria": "SIS-SSL",
    "nombre": "Sintaxis y Semántica de Lenguajes",
    "contenidos": "Gramáticas formales. Autómatas. Compiladores. Análisis léxico y sintáctico.",
    "creditosQueOtorga": 4,
    "creditosNecesarios": 0,
    "tipo": "OBLIGATORIA",
    "cuatrimestre": 4,
    "codigoPlanDeEstudios": null,
    "codigosCorrelativas": ["SIS-AED"]
}' "$TOKEN_ACADEMICA" > /dev/null

hacer_peticion "POST" "/materias" '{
    "codigoDeMateria": "SIS-SISOP",
    "nombre": "Sistemas Operativos",
    "contenidos": "Procesos y threads. Gestión de memoria. Sistemas de archivos. Concurrencia.",
    "creditosQueOtorga": 6,
    "creditosNecesarios": 0,
    "tipo": "OBLIGATORIA",
    "cuatrimestre": 5,
    "codigoPlanDeEstudios": null,
    "codigosCorrelativas": ["SIS-AEDII"]
}' "$TOKEN_ACADEMICA" > /dev/null

hacer_peticion "POST" "/materias" '{
    "codigoDeMateria": "SIS-REDES",
    "nombre": "Redes de Computadoras",
    "contenidos": "Modelo OSI/TCP-IP. Protocolos de red. Enrutamiento. Seguridad en redes.",
    "creditosQueOtorga": 6,
    "creditosNecesarios": 0,
    "tipo": "OBLIGATORIA",
    "cuatrimestre": 5,
    "codigoPlanDeEstudios": null,
    "codigosCorrelativas": ["SIS-AEDII"]
}' "$TOKEN_ACADEMICA" > /dev/null

echo "Creando materias de Ingeniería Electrónica..."
materias_electronica=(
    '{"codigoDeMateria":"ELE-AM1","nombre":"Análisis Matemático I","contenidos":"Límites y continuidad. Derivadas y aplicaciones. Integrales definidas e indefinidas. Series numéricas.","creditosQueOtorga":8,"creditosNecesarios":0,"tipo":"OBLIGATORIA","cuatrimestre":1,"codigoPlanDeEstudios":null,"codigosCorrelativas":[]}'
    '{"codigoDeMateria":"ELE-ALG","nombre":"Álgebra y Geometría Analítica","contenidos":"Vectores y matrices. Sistemas de ecuaciones lineales. Espacios vectoriales. Transformaciones lineales.","creditosQueOtorga":8,"creditosNecesarios":0,"tipo":"OBLIGATORIA","cuatrimestre":1,"codigoPlanDeEstudios":null,"codigosCorrelativas":[]}'
    '{"codigoDeMateria":"ELE-FIS1","nombre":"Física I","contenidos":"Mecánica del punto material. Cinemática y dinámica. Trabajo y energía. Oscilaciones.","creditosQueOtorga":6,"creditosNecesarios":0,"tipo":"OBLIGATORIA","cuatrimestre":1,"codigoPlanDeEstudios":null,"codigosCorrelativas":[]}'
    '{"codigoDeMateria":"ELE-QUI","nombre":"Química General","contenidos":"Estructura atómica. Enlaces químicos. Estequiometría. Termodinámica química básica.","creditosQueOtorga":4,"creditosNecesarios":0,"tipo":"OBLIGATORIA","cuatrimestre":1,"codigoPlanDeEstudios":null,"codigosCorrelativas":[]}'
    '{"codigoDeMateria":"ELE-SYO","nombre":"Sistemas y Organizaciones","contenidos":"Teoría de sistemas. Estructuras organizacionales. Gestión de procesos. Calidad.","creditosQueOtorga":4,"creditosNecesarios":0,"tipo":"OBLIGATORIA","cuatrimestre":2,"codigoPlanDeEstudios":null,"codigosCorrelativas":[]}'
)

for materia in "${materias_electronica[@]}"; do
    hacer_peticion "POST" "/materias" "$materia" "$TOKEN_ACADEMICA" > /dev/null
done

# Materias de Electrónica con correlativas
hacer_peticion "POST" "/materias" '{
    "codigoDeMateria": "ELE-AM2",
    "nombre": "Análisis Matemático II",
    "contenidos": "Funciones de varias variables. Integrales múltiples. Ecuaciones diferenciales ordinarias.",
    "creditosQueOtorga": 8,
    "creditosNecesarios": 0,
    "tipo": "OBLIGATORIA",
    "cuatrimestre": 2,
    "codigoPlanDeEstudios": null,
    "codigosCorrelativas": ["ELE-AM1"]
}' "$TOKEN_ACADEMICA" > /dev/null

hacer_peticion "POST" "/materias" '{
    "codigoDeMateria": "ELE-FIS2",
    "nombre": "Física II",
    "contenidos": "Electrostática y magnetostática. Circuitos eléctricos. Ondas electromagnéticas. Óptica.",
    "creditosQueOtorga": 6,
    "creditosNecesarios": 0,
    "tipo": "OBLIGATORIA",
    "cuatrimestre": 2,
    "codigoPlanDeEstudios": null,
    "codigosCorrelativas": ["ELE-FIS1", "ELE-AM1"]
}' "$TOKEN_ACADEMICA" > /dev/null

hacer_peticion "POST" "/materias" '{
    "codigoDeMateria": "ELE-PROB",
    "nombre": "Probabilidad y Estadística",
    "contenidos": "Variables aleatorias. Distribuciones de probabilidad. Inferencia estadística. Regresión.",
    "creditosQueOtorga": 4,
    "creditosNecesarios": 0,
    "tipo": "OBLIGATORIA",
    "cuatrimestre": 2,
    "codigoPlanDeEstudios": null,
    "codigosCorrelativas": ["ELE-AM1"]
}' "$TOKEN_ACADEMICA" > /dev/null

hacer_peticion "POST" "/materias" '{
    "codigoDeMateria": "ELE-CIRC",
    "nombre": "Análisis de Circuitos",
    "contenidos": "Análisis de circuitos en DC y AC. Teoremas de redes. Respuesta transitoria.",
    "creditosQueOtorga": 6,
    "creditosNecesarios": 0,
    "tipo": "OBLIGATORIA",
    "cuatrimestre": 3,
    "codigoPlanDeEstudios": null,
    "codigosCorrelativas": ["ELE-FIS2"]
}' "$TOKEN_ACADEMICA" > /dev/null

hacer_peticion "POST" "/materias" '{
    "codigoDeMateria": "ELE-SEN",
    "nombre": "Señales y Sistemas",
    "contenidos": "Señales continuas y discretas. Transformada de Fourier y Laplace. Filtros.",
    "creditosQueOtorga": 6,
    "creditosNecesarios": 0,
    "tipo": "OBLIGATORIA",
    "cuatrimestre": 4,
    "codigoPlanDeEstudios": null,
    "codigosCorrelativas": ["ELE-AM2"]
}' "$TOKEN_ACADEMICA" > /dev/null

hacer_peticion "POST" "/materias" '{
    "codigoDeMateria": "ELE-ELEC1",
    "nombre": "Electrónica I",
    "contenidos": "Semiconductores. Diodos y transistores. Amplificadores operacionales.",
    "creditosQueOtorga": 6,
    "creditosNecesarios": 0,
    "tipo": "OBLIGATORIA",
    "cuatrimestre": 4,
    "codigoPlanDeEstudios": null,
    "codigosCorrelativas": ["ELE-CIRC"]
}' "$TOKEN_ACADEMICA" > /dev/null

hacer_peticion "POST" "/materias" '{
    "codigoDeMateria": "ELE-CTRL",
    "nombre": "Sistemas de Control",
    "contenidos": "Modelado de sistemas. Estabilidad. Controladores PID. Diseño de sistemas de control.",
    "creditosQueOtorga": 6,
    "creditosNecesarios": 0,
    "tipo": "OBLIGATORIA",
    "cuatrimestre": 5,
    "codigoPlanDeEstudios": null,
    "codigosCorrelativas": ["ELE-ELEC1", "ELE-AM2"]
}' "$TOKEN_ACADEMICA" > /dev/null

echo "Creando planes de estudio..."
hacer_peticion "POST" "/planDeEstudios" '{
    "codigoDePlanDeEstudios": "PLAN-SIS-2024",
    "creditosElectivos": 12,
    "fechaEntradaEnVigencia": "2024-01-01",
    "fechaVencimiento": "2034-12-31",
    "codigosMaterias": ["SIS-AM1", "SIS-ALG", "SIS-FIS1", "SIS-QUI", "SIS-AM2", "SIS-FIS2", "SIS-PROB", "SIS-SYO", "SIS-AED", "SIS-AEDII", "SIS-BD", "SIS-SSL", "SIS-SISOP", "SIS-REDES"]
}' "$TOKEN_ACADEMICA" > /dev/null

hacer_peticion "POST" "/planDeEstudios" '{
    "codigoDePlanDeEstudios": "PLAN-ELE-2024",
    "creditosElectivos": 12,
    "fechaEntradaEnVigencia": "2024-01-01",
    "fechaVencimiento": "2034-12-31",
    "codigosMaterias": ["ELE-AM1", "ELE-ALG", "ELE-FIS1", "ELE-QUI", "ELE-AM2", "ELE-FIS2", "ELE-PROB", "ELE-SYO", "ELE-CIRC", "ELE-SEN", "ELE-ELEC1", "ELE-CTRL"]
}' "$TOKEN_ACADEMICA" > /dev/null

echo "Creando carreras..."
hacer_peticion "POST" "/carreras" '{
    "codigoDeCarrera": "ING-SIS",
    "nombre": "Ingeniería en Sistemas de Información",
    "titulo": "Ingeniero/a en Sistemas de Información",
    "incumbencias": "Diseño, desarrollo e implementación de sistemas informáticos. Administración de proyectos de software. Gestión de infraestructura tecnológica. Auditoría de sistemas.",
    "codigosPlanesDeEstudio": ["PLAN-SIS-2024"]
}' "$TOKEN_ACADEMICA" > /dev/null

hacer_peticion "POST" "/carreras" '{
    "codigoDeCarrera": "ING-ELE",
    "nombre": "Ingeniería Electrónica",
    "titulo": "Ingeniero/a en Electrónica",
    "incumbencias": "Diseño de circuitos y sistemas electrónicos. Sistemas de control y automatización. Telecomunicaciones. Procesamiento digital de señales.",
    "codigosPlanesDeEstudio": ["PLAN-ELE-2024"]
}' "$TOKEN_ACADEMICA" > /dev/null

echo "Creando cuatrimestres..."
hacer_peticion "POST" "/cuatrimestres" '{
    "codigo": "2025-1C",
    "fechaDeInicioClases": "9999-02-01",
    "fechaDeFinClases": "9999-07-15",
    "fechaInicioPeriodoDeInscripcion": "2025-01-01",
    "fechaFinPeriodoDeInscripcion": "9999-01-31",
    "fechaInicioPeriodoIntegradores": "9999-07-16",
    "fechaFinPeriodoIntegradores": "9999-07-31",
    "codigosCursos": []
}' "$TOKEN_PLANIFICACION" > /dev/null

hacer_peticion "POST" "/cuatrimestres" '{
    "codigo": "2025-2C",
    "fechaDeInicioClases": "9999-08-01",
    "fechaDeFinClases": "9999-12-15",
    "fechaInicioPeriodoDeInscripcion": "2025-06-01",
    "fechaFinPeriodoDeInscripcion": "9999-07-31",
    "fechaInicioPeriodoIntegradores": "9999-12-16",
    "fechaFinPeriodoIntegradores": "9999-12-31",
    "codigosCursos": []
}' "$TOKEN_PLANIFICACION" > /dev/null

echo "Creando cursos..."
cursos=(
    '{"codigo":"SIS-AM1-2025-1C-A","maximoDeAlumnos":40,"codigoMateria":"SIS-AM1","codigosCuatrimestres":["2025-1C"]}'
    '{"codigo":"SIS-AM1-2025-1C-B","maximoDeAlumnos":40,"codigoMateria":"SIS-AM1","codigosCuatrimestres":["2025-1C"]}'
    '{"codigo":"SIS-ALG-2025-1C-A","maximoDeAlumnos":40,"codigoMateria":"SIS-ALG","codigosCuatrimestres":["2025-1C"]}'
    '{"codigo":"SIS-FIS1-2025-1C-A","maximoDeAlumnos":35,"codigoMateria":"SIS-FIS1","codigosCuatrimestres":["2025-1C"]}'
    '{"codigo":"SIS-QUI-2025-1C-A","maximoDeAlumnos":35,"codigoMateria":"SIS-QUI","codigosCuatrimestres":["2025-1C"]}'
    '{"codigo":"SIS-AED-2025-1C-A","maximoDeAlumnos":35,"codigoMateria":"SIS-AED","codigosCuatrimestres":["2025-1C"]}'
    '{"codigo":"ELE-AM1-2025-1C-A","maximoDeAlumnos":40,"codigoMateria":"ELE-AM1","codigosCuatrimestres":["2025-1C"]}'
    '{"codigo":"ELE-ALG-2025-1C-A","maximoDeAlumnos":40,"codigoMateria":"ELE-ALG","codigosCuatrimestres":["2025-1C"]}'
    '{"codigo":"ELE-FIS1-2025-1C-A","maximoDeAlumnos":35,"codigoMateria":"ELE-FIS1","codigosCuatrimestres":["2025-1C"]}'
    '{"codigo":"ELE-QUI-2025-1C-A","maximoDeAlumnos":35,"codigoMateria":"ELE-QUI","codigosCuatrimestres":["2025-1C"]}'
    '{"codigo":"ELE-CIRC-2025-1C-A","maximoDeAlumnos":30,"codigoMateria":"ELE-CIRC","codigosCuatrimestres":["2025-1C"]}'
)

for curso in "${cursos[@]}"; do
    hacer_peticion "POST" "/cursos" "$curso" "$TOKEN_PLANIFICACION" > /dev/null
done

echo "Creando alumnos..."
# Lucía Fernández - Solo Ing. en Sistemas
hacer_peticion "POST" "/alumnos" '{
    "matricula": 12345,
    "nombre": "Lucía",
    "apellido": "Fernández",
    "dni": 12345,
    "email": "lucia.fernandez@upp.edu.ar",
    "direccion": "Av. Libertador 1250, CABA",
    "fechaNacimiento": "2001-03-15",
    "fechaIngreso": "2024-03-01",
    "fechaEgreso": null,
    "telefonos": ["1145678901"],
    "codigosCarreras": ["ING-SIS"],
    "codigosPlanesDeEstudio": ["PLAN-SIS-2024"]
}' "$TOKEN_ESTUDIANTIL" > /dev/null

# Martín González - Ambas carreras (Sistemas y Electrónica)
hacer_peticion "POST" "/alumnos" '{
    "matricula": 123456,
    "nombre": "Martín",
    "apellido": "González",
    "dni": 123456,
    "email": "martin.gonzalez@upp.edu.ar",
    "direccion": "Calle San Martín 456, La Plata",
    "fechaNacimiento": "2002-08-22",
    "fechaIngreso": "2024-03-01",
    "fechaEgreso": null,
    "telefonos": ["1198765432"],
    "codigosCarreras": ["ING-SIS", "ING-ELE"],
    "codigosPlanesDeEstudio": ["PLAN-SIS-2024", "PLAN-ELE-2024"]
}' "$TOKEN_ESTUDIANTIL" > /dev/null

# Valentina López - Solo Ing. Electrónica
hacer_peticion "POST" "/alumnos" '{
    "matricula": 1234567,
    "nombre": "Valentina",
    "apellido": "López",
    "dni": 1234567,
    "email": "valentina.lopez@upp.edu.ar",
    "direccion": "Av. Corrientes 3500, CABA",
    "fechaNacimiento": "2000-11-10",
    "fechaIngreso": "2023-03-01",
    "fechaEgreso": null,
    "telefonos": ["1156781234"],
    "codigosCarreras": ["ING-ELE"],
    "codigosPlanesDeEstudio": ["PLAN-ELE-2024"]
}' "$TOKEN_ESTUDIANTIL" > /dev/null

echo "Inicialización completada exitosamente."
echo ""
echo "Resumen de datos creados:"
echo "  - 4 usuarios del sistema"
echo "  - 14 materias de Ing. en Sistemas"
echo "  - 12 materias de Ing. Electrónica"
echo "  - 2 planes de estudio"
echo "  - 2 carreras"
echo "  - 2 cuatrimestres"
echo "  - 11 cursos"
echo "  - 3 alumnos (1 inscripto en ambas carreras)"
