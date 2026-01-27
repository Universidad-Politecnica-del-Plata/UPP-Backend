# Usar Maven para compilar la aplicacion
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar pom.xml y descargar dependencias
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiar el resto del codigo fuente de la aplicacion
COPY src/ src/

# Empaquetar la aplicacion (crea el archivo JAR)
RUN mvn clean package -DskipTests

# Usar un JDK ligero para la imagen final
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copiar solo el archivo JAR de la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto de la aplicacion
EXPOSE 8080

# Ejecutar la aplicacion directamente
ENTRYPOINT ["java", "-jar", "app.jar"]
