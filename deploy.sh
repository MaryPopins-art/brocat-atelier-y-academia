#!/bin/bash

# Script de despliegue para Academia BROCAT
# Ejecutar desde el directorio raíz del proyecto

echo "🚀 Iniciando despliegue de Academia BROCAT..."

# 1. Limpiar y compilar
echo "📦 Compilando proyecto..."
mvn clean package -DskipTests -Pprod

# 2. Verificar que el JAR se creó correctamente
if [ ! -f "target/academia-horarios-1.0-SNAPSHOT.jar" ]; then
    echo "❌ Error: No se pudo crear el JAR"
    exit 1
fi

echo "✅ JAR creado exitosamente"

# 3. Crear directorio de producción
echo "📁 Creando estructura de directorios..."
mkdir -p /opt/academia-brocat/logs
mkdir -p /opt/academia-brocat/backup

# 4. Copiar JAR a directorio de producción
echo "📋 Copiando archivos..."
cp target/academia-horarios-1.0-SNAPSHOT.jar /opt/academia-brocat/academia-brocat.jar

# 5. Crear archivo de configuración del sistema
cat > /etc/systemd/system/academia-brocat.service << EOF
[Unit]
Description=Academia BROCAT Application
After=network.target

[Service]
Type=simple
User=www-data
WorkingDirectory=/opt/academia-brocat
ExecStart=/usr/bin/java -Xmx512m -Xms256m -jar academia-brocat.jar --spring.profiles.active=prod
Restart=always
RestartSec=10

Environment=DB_USERNAME=academia_user
Environment=DB_PASSWORD=your_secure_password_here
Environment=JAVA_OPTS=-Xmx512m -Xms256m

[Install]
WantedBy=multi-user.target
EOF

# 6. Habilitar y iniciar el servicio
echo "🔧 Configurando servicio del sistema..."
systemctl daemon-reload
systemctl enable academia-brocat
systemctl start academia-brocat

# 7. Verificar estado
echo "🔍 Verificando estado del servicio..."
systemctl status academia-brocat

echo "🎉 Despliegue completado!"
echo "📱 La aplicación estará disponible en: http://tu-servidor:8080"
echo "📋 Para ver logs: journalctl -u academia-brocat -f"