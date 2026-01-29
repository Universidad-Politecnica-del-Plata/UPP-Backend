#!/bin/bash

set -e

echo "Iniciando aplicacion UPP Backend..."

# Puerto de la aplicacion (Render asigna via $PORT, default 8080 para local)
APP_PORT=${PORT:-8080}
echo "Usando puerto: $APP_PORT"

# Funcion que verifica e inicializa datos en background
inicializar_en_background() {
    echo "[Background] Esperando a que la aplicacion este lista..."

    # Esperar a que la aplicacion este lista (maximo 120 segundos)
    MAX_WAIT=120
    WAIT_COUNT=0
    until curl -sf http://localhost:${APP_PORT}/api/ping > /dev/null 2>&1; do
        WAIT_COUNT=$((WAIT_COUNT + 2))
        if [ $WAIT_COUNT -ge $MAX_WAIT ]; then
            echo "[Background] La aplicacion no respondio en ${MAX_WAIT} segundos, abortando inicializacion"
            return 1
        fi
        sleep 2
    done

    echo "[Background] La aplicacion esta lista!"

    # Verificar si la base de datos necesita inicializacion
    echo "[Background] Verificando si la base de datos necesita inicializacion..."

    LOGIN_RESPONSE=$(curl -sf -X POST \
        -H "Content-Type: application/json" \
        -d '{"username": "gestion_academica", "password": "password123"}' \
        "http://localhost:${APP_PORT}/api/auth/login" 2>/dev/null || echo "")

    if echo "$LOGIN_RESPONSE" | grep -q '"token"'; then
        echo "[Background] La base de datos ya tiene datos. Saltando inicializacion."
    else
        echo "[Background] La base de datos esta vacia. Ejecutando script de inicializacion..."
        if [ -f /app/inicializar-datos.sh ]; then
            chmod +x /app/inicializar-datos.sh
            /app/inicializar-datos.sh http://localhost:${APP_PORT} || {
                echo "[Background] Advertencia: El script de inicializacion fallo"
            }
            echo "[Background] Inicializacion de base de datos completada!"
        else
            echo "[Background] Advertencia: Script de inicializacion no encontrado"
        fi
    fi
}

# Ejecutar la inicializacion en background
inicializar_en_background &

# Ejecutar Java como proceso principal (reemplaza este script)
# Esto permite que Render detecte correctamente el proceso y el puerto
echo "Ejecutando aplicacion Java como proceso principal..."
exec java -jar app.jar