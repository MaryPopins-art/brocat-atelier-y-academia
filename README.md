# Academia Horarios

Este proyecto es una aplicación web para gestionar los horarios de los alumnos de una academia. La aplicación permite a los administradores gestionar la información de los estudiantes y a los alumnos inscribirse o darse de baja en las clases.

## Estructura del Proyecto

El proyecto está organizado de la siguiente manera:

# 🏫 Academia BROCAT - Sistema de Gestión Integral

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.5.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-11-blue.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 📋 Descripción

**Academia BROCAT** es un sistema integral de gestión que combina dos aplicaciones independientes:

### 🎓 **Academia BROCAT** - Sistema de Gestión Educativa
- ✅ Gestión completa de alumnos, profesores y cursos
- ✅ Sistema inteligente de ausencias con recuperaciones automáticas
- ✅ Calendarios mensuales con visualización de clases perdidas
- ✅ Límite de 30 días naturales para recuperaciones
- ✅ Panel administrativo completo

### ✂️ **Atelier BROCAT** - Sistema de Citas Previas
- ✅ Reserva de citas con confirmación automática
- ✅ Selección dinámica de horas disponibles en tiempo real
- ✅ Gestión independiente de horarios (10:00-13:30 y 17:00-20:30)
- ✅ Consulta de citas por nombre y teléfono
- ✅ Sistema completamente independiente de la Academia

## 🚀 Características Principales

### **Sistemas Independientes**
- 🔒 **Separación total** entre Academia y Atelier
- 🕐 **Horarios independientes** sin interferencias
- 💾 **Bases de datos separadas** para cada contexto
- 🎯 **APIs REST específicas** para cada sistema

### **Funcionalidades Avanzadas**
- 📱 **Responsive Design** con Bootstrap 5
- ⚡ **Confirmación automática** de citas
- 🔄 **Actualización en tiempo real** de disponibilidad
- 📊 **Dashboard administrativo** completo
- 🗓️ **Calendario interactivo** con ausencias marcadas

## 🛠️ Tecnologías Utilizadas

### Backend
- **Spring Boot 2.5.4** - Framework principal
- **Spring Data JPA** - Persistencia de datos
- **Hibernate** - ORM
- **H2 Database** - Base de datos en memoria (desarrollo)
- **PostgreSQL** - Base de datos para producción

### Frontend
- **Thymeleaf** - Motor de plantillas
- **Bootstrap 5** - Framework CSS
- **JavaScript ES6** - Interactividad
- **Bootstrap Icons** - Iconografía

### Herramientas
- **Maven** - Gestión de dependencias
- **Git** - Control de versiones
- **Docker** - Contenedorización
- **Nginx** - Servidor web y proxy reverso

## 📦 Instalación y Configuración

### Prerrequisitos
- ☕ Java 11 o superior
- 📦 Maven 3.6+
- 🐳 Docker (opcional)

### 🔧 Instalación Local

1. **Clonar el repositorio**
```bash
git clone https://github.com/tu-usuario/academia-brocat.git
cd academia-brocat
```

2. **Compilar el proyecto**
```bash
mvn clean compile
```

3. **Ejecutar la aplicación**
```bash
mvn spring-boot:run
```

4. **Acceder a la aplicación**
- 🌐 **Aplicación principal:** http://localhost:8080
- 🗄️ **Consola H2:** http://localhost:8080/h2-console

### 🐳 Instalación con Docker

```bash
# Construir y ejecutar con Docker Compose
docker-compose up -d

# Ver logs
docker-compose logs -f app
```

## 📱 Uso de la Aplicación

### **Academia BROCAT**
1. Accede al **Panel de Administración**
2. Gestiona alumnos, profesores y cursos
3. Marca ausencias desde el calendario
4. Revisa las recuperaciones generadas automáticamente

### **Atelier BROCAT**
1. Selecciona **"Atelier BROCAT CITA PREVIA"**
2. Completa el formulario de solicitud
3. Elige fecha y hora disponible
4. ¡Cita confirmada automáticamente!

## 🏗️ Arquitectura del Sistema

```
📁 academia-horarios/
├── 📁 src/main/java/com/academia/
│   ├── 📁 controller/          # Controladores REST
│   ├── 📁 service/             # Lógica de negocio
│   ├── 📁 repository/          # Acceso a datos
│   └── 📄 *.java              # Entidades y modelos
├── 📁 src/main/resources/
│   ├── 📁 templates/           # Plantillas Thymeleaf
│   ├── 📁 static/              # Archivos estáticos
│   └── 📄 application.properties
├── 📁 docker/                  # Configuración Docker
├── 📄 pom.xml                  # Configuración Maven
└── 📄 README.md               # Este archivo
```

## 🌐 Despliegue en Producción

### Variables de Entorno
```bash
# Base de datos
DB_USERNAME=academia_user
DB_PASSWORD=your_secure_password
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/academia_db

# Perfiles
SPRING_PROFILES_ACTIVE=prod
```

### Servicios Recomendados
- 🔥 **DigitalOcean** - VPS desde $5/mes
- ☁️ **Heroku** - Despliegue automático
- 🐳 **Docker Hub** - Contenedores
- 🌍 **Cloudflare** - CDN y SSL

## 📊 Endpoints API

### Academia BROCAT
- `GET /alumno/api/horarios-disponibles-academia` - Horarios disponibles
- `GET /alumno/api/plazas-disponibles-academia` - Plazas para recuperación
- `GET /alumno/api/contar-plazas-academia` - Contador de plazas

### Atelier BROCAT
- `GET /cita-previa/api/horas-disponibles-atelier` - Horas disponibles
- `GET /cita-previa/api/info-dia-atelier` - Información del día
- `GET /cita-previa/api/estadisticas-atelier` - Estadísticas de ocupación

## 🤝 Contribución

1. Haz fork del proyecto
2. Crea una rama para tu feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit tus cambios (`git commit -am 'Añadir nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Crea un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para detalles.

## 👥 Autores

- **Tu Nombre** - *Desarrollo principal* - [tu-github](https://github.com/tu-usuario)

## 🙏 Agradecimientos

- Spring Boot team por el excelente framework
- Bootstrap team por el framework CSS
- Thymeleaf team por el motor de plantillas

## 📞 Soporte

¿Tienes preguntas? ¡Contacta con nosotros!

- 📧 **Email:** soporte@academia-brocat.com
- 🐛 **Issues:** [GitHub Issues](https://github.com/tu-usuario/academia-brocat/issues)
- 📖 **Documentación:** [Wiki del proyecto](https://github.com/tu-usuario/academia-brocat/wiki)

---

⭐ **¡Si este proyecto te ha sido útil, no olvides darle una estrella!** ⭐

## Requisitos

Para desarrollar este proyecto, necesitarás tener instalados los siguientes programas:

- **Java**: Asegúrate de tener el JDK instalado.
- **Maven**: Para gestionar las dependencias del proyecto.
- **Servidor de aplicaciones**: Puedes usar Apache Tomcat o Spring Boot para simplificar el proceso de despliegue.
- **Node.js y npm**: Si decides incluir bibliotecas front-end como React o Vue.js.

## Instrucciones de Configuración

1. Clona este repositorio en tu máquina local.
2. Navega al directorio del proyecto.
3. Ejecuta `mvn clean install` para compilar el proyecto y descargar las dependencias.
4. Si usas Spring Boot, puedes ejecutar la aplicación con `mvn spring-boot:run`.
5. Accede a la aplicación en tu navegador en `http://localhost:8080`.

## Contribuciones

Las contribuciones son bienvenidas. Si deseas contribuir, por favor abre un issue o envía un pull request.

## Licencia

Este proyecto está bajo la Licencia MIT.