# Usar Maven para compilar la aplicacion
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar pom.xml y descargar dependencias
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiar el resto del codigo fuente de la aplicacion
COPY src/ src/

# crear el archivo JAR
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk
WORKDIR /app

# Instalar curl para inicializacion
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

COPY --from=build /app/target/*.jar app.jar

# Copiar scripts de inicializacion
COPY checkear-si-hay-datos.sh /app/checkear-si-hay-datos.sh
COPY inicializar-datos.sh /app/inicializar-datos.sh
RUN chmod +x /app/checkear-si-hay-datos.sh /app/inicializar-datos.sh

# Exponer el puerto de la aplicacion
EXPOSE 8080

# Ejecutar la aplicacion via script de carga de datos
ENTRYPOINT ["/app/checkear-si-hay-datos.sh"]
