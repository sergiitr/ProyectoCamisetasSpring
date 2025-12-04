# PROYECTO CAMISETAS: Panel de Administración (Spring Boot + JPA)

## Descripción del Proyecto
**ProyectoCamisetasSpring** es el panel de gestión administrativo (backend) para una tienda de comercio electrónico de camisetas. El objetivo principal es implementar y asegurar la funcionalidad **CRUD** para el inventario de **Camisetas** y el catálogo de **Categorías**, manteniendo la **integridad referencial** en la base de datos.

---

## Tecnología Utilizada
- **Backend Framework:** Spring Boot  
- **Lenguaje:** Java 17
- **Base de Datos:** MySQL  
- **Vistas (Admin UI):** Thymeleaf  
- **Contenedores:** Docker Compose (para la BBDD)  
- **Utilidades:** Lombok  

---

## Funcionalidades Clave

### I. Gestión de Camisetas (`/admin/camiseta`)
🔹 CRUD Completo: Listar ➜ Crear ➜ Editar ➜ Borrar  
🔹 Borrado Robusto:  
El método `deleteCamisetaData` utiliza **EntityManager** y `@Transactional`, asegurando que se gestionen correctamente las dependencias y no se generen errores de llave foránea (FK).  
🔹 Actualización Segura:  
Los formularios de edición incluyen un input oculto para enviar correctamente el ID, evitando registros duplicados e imponiendo una **UPDATE** en vez de un **INSERT**.  
🔹 Vista Maestro-Detalle: Visualización de camisetas agrupadas por categoría.  

### II. Gestión de Categorías (`/admin/categoria`)
🔹 CRUD Completo para el catálogo de categorías  
🔹 Vista Detalle: Permite mostrar qué camisetas están asociadas a cada categoría

### III. Interfaz de Usuario (Thymeleaf)
🔹 Navegación modular mediante **fragmentos** (ej: `navbar.html`)  
🔹 Menús desplegables mediante CSS `:hover` para acceso rápido a operaciones CRUD  

---

## Configuración e Instalación Local

### 1 Requisitos Previos
Debes tener instalado:
- Java JDK 17+
- Apache Maven
- Docker y Docker Compose

---

### 2 Base de Datos con Docker
Asegúrate de tener el script SQL actualizado con la estructura final.

Ejecuta en el directorio donde se encuentre `docker-compose.yml`:

```bash
sudo docker compose up -d
```

---

### 3 Ejecución de la Aplicación

Configura las credenciales y puerto MySQL en application.properties
(por defecto MySQL en localhost:3306)

Ejecuta el proyecto:

``` bash
mvn clean spring-boot:run
```