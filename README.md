
# 📝 Gestor de Biblioteca CLI (Command Line Interface)

Aplicación en Java utlizando Spring Boot para gestionar una biblioteca a través de la línea de comandos




## Funcionalidades

- Registrar libros
- Buscar libro por ISBN
- Marcar libro como prestado
- Ver cuándo fue prestado un libro
- Ver qué libros no han sido devueltos
- Ver qué libros fueron prestados entre dos fechas proporcionadas
## Tecnologías

Java 17, Spring Boot 3.5.6, H2 DataBase, Hibernate, Lombok

## Requisitos del Sistema
* **Java Development Kit (JDK):** Versión **17**
* **Apache Maven:** Versión 3.6.0 o superior (para usar los comandos `mvn`).

## Estado del Proyecto

🚧 En desarrollo 
## Arquitectura y Estructura

El proyecto sigue una arquitectura en capas (Controller, Service y Repository) promoviendo la separación de responsabilidades y la reutilización de código
## Ejecución

1. **Verificar Versión de Java:**
   Asegurate de que tu terminal está usando la versión correcta:
   ```bash
   java -version
   # La salida debe mostrar 'openjdk version "17.x.x"' o 'java version 17.x.x"'
   ```
2. **Compilar y Empaquetar:**
   ```bash
   mvn clean compile package
   ```
3. **Ejectuar el Aplicativo:**
   ```bash
   java -jar target/gestion-biblioteca-cli-1.0.0.jar
   ```   
