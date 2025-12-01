# Etapa 1: Construcción
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
# Compilar saltando tests para ir más rápido
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
# Ajusta el nombre del jar si es diferente en tu pom.xml, por defecto suele ser:
COPY --from=build /app/target/fonda-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]