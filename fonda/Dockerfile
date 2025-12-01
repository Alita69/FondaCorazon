# Usamos una imagen ligera de JDK 17 (o la versión que uses, ej. 21)
FROM openjdk:17-jdk-slim
# Copiamos el archivo jar generado (asegurate que el nombre coincida con tu pom.xml)
COPY target/fonda-0.0.1-SNAPSHOT.jar app.jar
# Exponemos el puerto
EXPOSE 8080
# Ejecutamos
ENTRYPOINT ["java","-jar","/app.jar"]