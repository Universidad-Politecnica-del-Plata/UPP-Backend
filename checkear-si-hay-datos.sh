#!/bin/bash

set -e

APP_PORT=${PORT:-8080}
echo "iniciando en puerto $APP_PORT"

inicializar_en_background() {
    MAX_WAIT=300
    WAIT_COUNT=0
    until curl -sf http://localhost:${APP_PORT}/api/ping > /dev/null 2>&1; do
        WAIT_COUNT=$((WAIT_COUNT + 2))
        if [ $WAIT_COUNT -ge $MAX_WAIT ]; then
            echo "timeout esperando app"
            return 1
        fi
        sleep 2
    done

    echo "app lista, verificando db..."

    LOGIN_RESPONSE=$(curl -sf -X POST \
        -H "Content-Type: application/json" \
        -d '{"username": "gestion_academica", "password": "password123"}' \
        "http://localhost:${APP_PORT}/api/auth/login" 2>/dev/null || echo "")

    if echo "$LOGIN_RESPONSE" | grep -q '"token"'; then
        echo "db con datos, omitiendo init"
    else
        echo "db vacia, ejecutando init..."
        if [ -f /app/inicializar-datos.sh ]; then
            chmod +x /app/inicializar-datos.sh
            /app/inicializar-datos.sh http://localhost:${APP_PORT} || echo "init fallo"
        else
            echo "script init no encontrado"
        fi
    fi
}

inicializar_en_background &

exec java -jar app.jar