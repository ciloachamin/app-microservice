# Usa una imagen base de Java
FROM openjdk:17-jdk-slim

# Establece el directorio de trabajo en el contenedor
WORKDIR /app

# Copia el archivo JAR en el contenedor
COPY target/msvc-usuarios-0.0.1-SNAPSHOT.jar msvc-usuarios.jar

# Expone el puerto en el que la aplicación estará escuchando
EXPOSE 8001

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "msvc-usuarios.jar"]
