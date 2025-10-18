# Dockerfile para Academia BROCAT
FROM openjdk:11-jre-slim

# Información del mantenedor
LABEL maintainer="Academia BROCAT"
LABEL description="Sistema de gestión Academia BROCAT y Atelier"

# Crear directorio de trabajo
WORKDIR /app

# Copiar el JAR
COPY target/academia-horarios-1.0-SNAPSHOT.jar app.jar

# Crear directorio para logs
RUN mkdir -p /app/logs

# Exponer puerto
EXPOSE 8080

# Variables de entorno
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Comando de inicio
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]