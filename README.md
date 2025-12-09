# Sistema de Facturación

Sistema de facturación con Spring Boot, PostgreSQL y Swagger.

## Requisitos

- Java 17+
- Docker y Docker Compose
- Maven

## Recursos del Proyecto

### 📦 JAR Ejecutable
El proyecto puede compilarse como JAR ejecutable standalone.

### 📮 Colección Postman
Incluye archivo `Sistema-Facturacion.postman_collection.json` con todos los endpoints de prueba.

### 🗄️ Script de Base de Datos
Archivo `schema.sql` con la estructura completa de tablas, índices y datos iniciales.

### 🔗 Repositorio GitHub
**URL del proyecto:** `https://GitHub.com/[tu-usuario]/sistema-facturacion`
> ⚠️ **Nota:** Reemplazar con la URL real de tu repositorio GitHub

## Inicio Rápido

### 1. Levantar la base de datos

```bash
docker-compose up -d
```

Esto iniciará:
- PostgreSQL en `localhost:5432`
    - Base de datos: `facturacion_db`
    - Usuario: `admin`
    - Contraseña: `admin123`
- pgAdmin en `http://localhost:5050`
    - Email: `admin@admin.com`
    - Contraseña: `admin123`

### 2. Opción A: Ejecutar con Maven

```bash
mvn spring-boot:run
```

### 2. Opción B: Generar y ejecutar JAR

```bash
# Compilar el proyecto y generar JAR
mvn clean package

# El JAR se genera en: target/facturacion-alex-1.0.0.jar

# Ejecutar el JAR
java -jar target/facturacion-alex-1.0.0.jar
```

El JAR incluye todas las dependencias necesarias (fat JAR) y puede ejecutarse de forma independiente en cualquier servidor con Java 17+.

La aplicación estará disponible en `http://localhost:8080`

### 3. Acceder a la documentación API

Swagger UI: `http://localhost:8080/swagger-ui.html`

## Configuración de pgAdmin

1. Abrir `http://localhost:5050`
2. Iniciar sesión con las credenciales
3. Agregar nuevo servidor:
    - Name: `Facturacion`
    - Host: `postgres`
    - Port: `5432`
    - Database: `facturacion_db`
    - Username: `admin`
    - Password: `admin123`

### Ejecutar Script de Creación de Tablas

Si necesitas crear las tablas manualmente o en otra base de datos:

```bash
# Desde pgAdmin:
# 1. Conectar al servidor PostgreSQL
# 2. Abrir Query Tool
# 3. Cargar y ejecutar el archivo schema.sql

# O desde línea de comandos:
psql -h localhost -U admin -d facturacion_db -f schema.sql
```

## Pruebas con Postman

### Importar Colección

1. Abrir Postman
2. Click en "Import"
3. Seleccionar el archivo `Sistema-Facturacion.postman_collection.json`
4. La colección incluye:
    - ✅ Todos los endpoints de Clientes
    - ✅ Todos los endpoints de Productos
    - ✅ Todos los endpoints de Facturas/Comprobantes
    - ✅ Casos de prueba exitosos y con errores
    - ✅ Variable de entorno `base_url` preconfigurada

### Variable de Entorno

La colección usa `{{base_url}}` con valor por defecto: `http://localhost:8080`

Para cambiar el servidor, editar la variable de entorno en Postman.

## Endpoints Principales

### Crear Comprobante (POST /api/facturas)

```json
{
  "cliente": {
    "clienteid": 1
  },
  "lineas": [
    {
      "cantidad": 2,
      "producto": {
        "productoid": 1
      }
    },
    {
      "cantidad": 1,
      "producto": {
        "productoid": 3
      }
    }
  ]
}
```

### Validaciones Implementadas

- Cliente debe existir y estar activo
- Productos deben existir y estar activos
- Stock debe ser suficiente para cada producto
- Stock se reduce automáticamente
- Fecha se obtiene desde WorldClock API (fallback a fecha local)
- Precios históricos se preservan (cambios futuros no afectan facturas anteriores)

## Arquitectura del Proyecto

```
Capa de Presentación (Controllers)
        ↓
Capa de Negocio (Services)
        ↓
Capa de Acceso a Datos (Repositories)
        ↓
Base de Datos (PostgreSQL)
```

### Tecnologías Utilizadas

- **Spring Boot 3.1.5** - Framework principal
- **Spring Data JPA** - ORM y persistencia
- **PostgreSQL 15** - Base de datos relacional
- **SpringDoc OpenAPI 3** - Documentación automática (Swagger)
- **Bean Validation** - Validaciones en DTOs
- **Docker & Docker Compose** - Contenedores para BD
- **Maven** - Gestión de dependencias

## Detener Servicios

```bash
# Detener contenedores
docker-compose down

# Detener y eliminar volúmenes
docker-compose down -v

# Ver logs de los contenedores
docker-compose logs -f
```

## Troubleshooting

### Error de conexión a PostgreSQL

Si el servicio no puede conectarse a la base de datos:

```bash
# Verificar que los contenedores estén corriendo
docker-compose ps

# Ver logs de PostgreSQL
docker-compose logs postgres

# Reiniciar servicios
docker-compose restart
```

### Puerto 8080 en uso

Cambiar el puerto en `application.properties`:

```properties
server.port=8081
```

### Problemas con el JAR

Si el JAR no ejecuta correctamente:

```bash
# Verificar versión de Java
java -version

# Debe ser Java 17 o superior
# Limpiar y recompilar
mvn clean package -DskipTests
```

## Contacto

Equipo de Desarrollo - contacto@facturacion.com

**Repositorio GitHub:** https://github.com/[tu-usuario]/sistema-facturacion