# Biblioteca Escolar – Control de préstamos

Proyecto académico de **Lenguaje de Programación II (PROG04691)**.

Sistema web para administrar libros, lectores y préstamos de una biblioteca escolar.

## Funciones

- Registrar, editar y eliminar libros.
- Registrar, editar y eliminar lectores.
- Registrar préstamos y devoluciones.
- Control automático de ejemplares disponibles.
- Préstamos con plazo de 7 días.
- Validaciones para evitar datos repetidos y préstamos sin stock.
- Generación de reporte PDF de préstamos pendientes y vencidos.

## Tecnologías

- Java 21
- Spring Boot 3.2.5
- Thymeleaf
- Spring Data JPA / Hibernate
- MySQL 8
- Maven
- JasperReports 6.21.3

## Estructura

```text
biblioteca-prestamos/
├── database/
│   └── biblioteca_escolar.sql
├── pom.xml
└── src/main
    ├── java/com/biblioteca
    │   ├── model/
    │   ├── repository/
    │   ├── service/
    │   └── controller/
    └── resources
        ├── templates/
        ├── static/
        └── reportes/
```

## Requisitos

- JDK 21
- Maven 3.9+
- MySQL 8
- MySQL Workbench

## Base de datos

Ejecutar el archivo:

```text
database/biblioteca_escolar.sql
```

Este archivo crea la base de datos `biblioteca_escolar`, sus tablas y algunos datos iniciales.

## Configuración

La conexión está configurada para:

```text
Servidor: localhost:3306
Base de datos: biblioteca_escolar
Usuario: root
Contraseña: 1234
```

Si tus datos de MySQL son diferentes, puedes configurarlos en:

```text
application-local.properties
```

## Ejecutar el proyecto

Desde la carpeta del proyecto:

```bash
mvn spring-boot:run
```

También se puede ejecutar `BibliotecaApplication.java` desde Eclipse.

Después entrar a:

```text
http://localhost:8080
```

## Reporte PDF

El reporte se puede abrir desde el menú **Reporte PDF** o directamente en:

```text
http://localhost:8080/reportes/prestamos-pendientes
```

El diseño del reporte está en:

```text
src/main/resources/reportes/prestamos_pendientes.jrxml
```

## Pruebas

Se pueden probar las funciones principales:

- Registrar y devolver préstamos.
- Aumentar y disminuir el stock.
- Crear, editar y eliminar libros.
- Crear, editar y eliminar lectores.
- Validación de códigos y correos repetidos.
- Evitar préstamos cuando no hay ejemplares disponibles.
- Generar el reporte PDF.


## Git

```bash
git add .
git commit -m "Actualización del sistema"
git push
```
