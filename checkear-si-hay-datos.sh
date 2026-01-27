#!/bin/bash

set -e

echo "Iniciando aplicacion UPP Backend..."

# Iniciar la aplicacion Java en segundo plano
java -jar app.jar &
APP_PID=$!

echo "Esperando a que la aplicacion este lista..."

# Puerto de la aplicacion (Render asigna via $PORT, default 8080 para local)
APP_PORT=${PORT:-8080}
echo "Usando puerto: $APP_PORT"

# Esperar a que la aplicacion este lista (maximo 120 segundos)
MAX_WAIT=120
WAIT_COUNT=0
until curl -sf http://localhost:${APP_PORT}/api/ping > /dev/null 2>&1; do
    WAIT_COUNT=$((WAIT_COUNT + 2))
    if [ $WAIT_COUNT -ge $MAX_WAIT ]; then
        echo "La aplicacion no pudo iniciar en ${MAX_WAIT} segundos"
        exit 1
    fi
    echo "Esperando a la aplicacion... (${WAIT_COUNT}s)"
    sleep 2
done

echo "La aplicacion esta lista!"

# Verificar si la base de datos necesita inicializacion intentando login con un usuario conocido
echo "Verificando si la base de datos necesita inicializacion..."

# Intentar login con el usuario gestion_academica (creado por el script de inicializacion)
LOGIN_RESPONSE=$(curl -sf -X POST \
    -H "Content-Type: application/json" \
    -d '{"username": "gestion_academica", "password": "password123"}' \
    "http://localhost:${APP_PORT}/api/auth/login" 2>/dev/null || echo "")

# Si el login devuelve un token, los datos ya existen
if echo "$LOGIN_RESPONSE" | grep -q '"token"'; then
    VERIFICACION_DATOS="tiene_datos"
else
    VERIFICACION_DATOS=""
fi

if [ -z "$VERIFICACION_DATOS" ]; then
    echo "La base de datos esta vacia. Ejecutando script de inicializacion..."
    if [ -f /app/inicializar-datos.sh ]; then
        chmod +x /app/inicializar-datos.sh
        /app/inicializar-datos.sh http://localhost:${APP_PORT} || {
            echo "Advertencia: El script de inicializacion fallo, pero continuando..."
        }
        echo "Inicializacion de base de datos completada!"
    else
        echo "Advertencia: Script de inicializacion no encontrado en /app/inicializar-datos.sh"
    fi
else
    echo "La base de datos ya tiene datos. Saltando inicializacion."
fi

echo "Inicio de la aplicacion completo. Ejecutando con PID: $APP_PID"

# Esperar al proceso Java para mantener el contenedor corriendo
wait $APP_PID