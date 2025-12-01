# --- ETAPA 1: Construir el JAR (Usamos Maven) ---
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
# Compilamos el proyecto y saltamos los tests para ahorrar tiempo
RUN mvn clean package -DskipTests

# --- ETAPA 2: Ejecutar la App (Usamos una imagen ligera y moderna) ---
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
# Copiamos el JAR generado en la etapa anterior
# Asegúrate de que el nombre 'fonda-0.0.1-SNAPSHOT.jar' coincida con tu pom.xml
COPY --from=build /app/target/fonda-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]