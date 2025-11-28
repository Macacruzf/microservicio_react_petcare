# 🚀 Guía de Inicio - Microservicios Spring Boot

Esta guía te ayudará a iniciar los 4 microservicios de Spring Boot necesarios para el proyecto PetCare.

---

## 📋 Pre-requisitos

Antes de iniciar los microservicios, asegúrate de tener:

- ✅ **Java JDK 17 o superior** instalado
- ✅ **Maven** instalado (o usar el Maven Wrapper incluido `./mvnw`)
- ✅ **MySQL** instalado y corriendo
- ✅ Las siguientes **bases de datos creadas** en MySQL:
  - `react_usuario`
  - `react_producto`
  - `react_carrito`
  - `react_pedido`

---

## 🗄️ Paso 1: Crear Bases de Datos MySQL

Abre MySQL y ejecuta los siguientes comandos:

```sql
CREATE DATABASE react_usuario;
CREATE DATABASE react_producto;
CREATE DATABASE react_carrito;
CREATE DATABASE react_pedido;
```

**Verificar que se crearon:**
```sql
SHOW DATABASES;
```

---

## ⚙️ Paso 2: Verificar Configuración

Cada microservicio tiene su archivo de configuración en `src/main/resources/application.properties`.

**No es necesario modificar nada** si tu MySQL tiene:
- Usuario: `root`
- Contraseña: (vacía o la que configuraste)
- Puerto: `3306`

Si tu configuración es diferente, edita cada `application.properties`:

```properties
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_CONTRASEÑA
```

---

## 🚀 Paso 3: Iniciar los Microservicios

### Opción A: Con Maven Wrapper (Recomendado)

Abre **4 terminales diferentes** (una para cada microservicio):

#### Terminal 1 - Usuario Service (Puerto 8081)
```bash
cd microservicio_react_petcare/usuario/usuario
./mvnw spring-boot:run
```

#### Terminal 2 - Productos Service (Puerto 8082)
```bash
cd microservicio_react_petcare/productos/productos
./mvnw spring-boot:run
```

#### Terminal 3 - Carrito Service (Puerto 8083)
```bash
cd microservicio_react_petcare/carrito/carrito
./mvnw spring-boot:run
```

#### Terminal 4 - Pedidos Service (Puerto 8084)
```bash
cd microservicio_react_petcare/pedidos/pedidos
./mvnw spring-boot:run
```

---

### Opción B: Con Maven instalado globalmente

Si tienes Maven instalado en tu sistema:

```bash
# Terminal 1
cd microservicio_react_petcare/usuario/usuario
mvn spring-boot:run

# Terminal 2
cd microservicio_react_petcare/productos/productos
mvn spring-boot:run

# Terminal 3
cd microservicio_react_petcare/carrito/carrito
mvn spring-boot:run

# Terminal 4
cd microservicio_react_petcare/pedidos/pedidos
mvn spring-boot:run
```

---

### Opción C: Desde tu IDE (IntelliJ IDEA / Eclipse)

1. Abre cada carpeta de microservicio como proyecto
2. Localiza la clase principal (con anotación `@SpringBootApplication`)
3. Click derecho → Run

---

## ✅ Paso 4: Verificar que están Corriendo

Una vez iniciados todos los microservios, verifica que funcionan:

### Swagger UI (Documentación de API):

Abre en tu navegador:

- **Usuario**: http://localhost:8081/swagger-ui/
- **Productos**: http://localhost:8082/swagger-ui/
- **Carrito**: http://localhost:8083/swagger-ui/
- **Pedidos**: http://localhost:8084/swagger-ui/

### Endpoints de Prueba:

```bash
# Usuario Service
curl http://localhost:8081/actuator/health

# Productos Service
curl http://localhost:8082/actuator/health

# Carrito Service
curl http://localhost:8083/actuator/health

# Pedidos Service
curl http://localhost:8084/actuator/health
```

---

## 📊 Resumen de Puertos

| Microservicio | Puerto | Base de Datos | Swagger |
|---------------|--------|---------------|---------|
| Usuario | 8081 | react_usuario | http://localhost:8081/swagger-ui/ |
| Productos | 8082 | react_producto | http://localhost:8082/swagger-ui/ |
| Carrito | 8083 | react_carrito | http://localhost:8083/swagger-ui/ |
| Pedidos | 8084 | react_pedido | http://localhost:8084/swagger-ui/ |

---

## 🐛 Solución de Problemas Comunes

### Error: "Port 8081 is already in use"
**Solución**: Otro proceso está usando ese puerto. Ciérralo o cambia el puerto en `application.properties`:
```properties
server.port=8085
```

### Error: "Access denied for user 'root'@'localhost'"
**Solución**: Usuario o contraseña incorrecta. Edita `application.properties`:
```properties
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_CONTRASEÑA
```

### Error: "Unknown database 'react_usuario'"
**Solución**: La base de datos no existe. Créala con:
```sql
CREATE DATABASE react_usuario;
```

### Error: "Table 'usuario' doesn't exist"
**Solución**: JPA creará las tablas automáticamente al iniciar. Si no, verifica:
```properties
spring.jpa.hibernate.ddl-auto=update
```

### Maven no descarga dependencias
**Solución**: 
```bash
./mvnw clean install
```

---

## 🔄 Orden Recomendado de Inicio

Aunque pueden iniciarse en cualquier orden, se recomienda:

1. **Usuario** (8081) - Primero, porque maneja autenticación
2. **Productos** (8082) - Segundo, base del catálogo
3. **Carrito** (8083) - Tercero, depende de Productos
4. **Pedidos** (8084) - Último, depende de Carrito y Productos

---

## 🛑 Detener los Microservicios

En cada terminal donde esté corriendo un microservicio:

- **Windows**: `Ctrl + C`
- **Mac/Linux**: `Ctrl + C` o `Cmd + C`

---

## 📝 Logs Importantes

Cuando inicia correctamente, verás:

```
Started UsuarioApplication in X.XXX seconds
Tomcat started on port(s): 8081 (http)
```

Si ves errores, revisa:
1. Configuración de MySQL
2. Puertos disponibles
3. Java y Maven instalados correctamente

---

## 🎯 Próximo Paso

Una vez que todos los microservicios estén corriendo, ve a la carpeta raíz del proyecto React y sigue las instrucciones en:

📄 **`GUIA_INICIO_PROYECTO_WEB.md`**

---

## 🆘 Ayuda Adicional

- **Documentación Spring Boot**: https://spring.io/projects/spring-boot
- **Maven**: https://maven.apache.org/
- **MySQL**: https://dev.mysql.com/doc/

---

**✨ ¡Listo! Tus microservicios deberían estar corriendo en los puertos 8081-8084.**
