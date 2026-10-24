# Biblioteca Escolar – Control de préstamos

Proyecto académico del curso **Lenguaje de Programación II (PROG04691)**.

## Objetivo

Sistema web pequeño para que el encargado de una biblioteca escolar registre libros y lectores, controle los préstamos y la devolución de ejemplares, y emita un reporte en PDF de los préstamos pendientes.

**Problema que resuelve:** evitar el control en cuadernos, donde no se sabe cuántos ejemplares quedan ni qué préstamos están vencidos.

**Usuario:** un único rol (el bibliotecario). Por simplicidad no hay inicio de sesión, por lo que no existen credenciales de acceso.

## Funciones principales

- CRUD de **libros** (código, título, autor, cantidad de ejemplares).
- CRUD de **lectores** (nombre y correo).
- **Préstamos:** registrar (plazo fijo de 7 días), listar y registrar devolución. El stock disponible se actualiza solo.
- Reglas: no se prestan libros sin ejemplares disponibles, no se repiten códigos ni correos, y no se elimina un libro o lector con préstamos registrados.
- **Reporte PDF** (JasperReports) de préstamos pendientes, marcando cuáles están vencidos.

## Tecnologías utilizadas

| Tecnología | Uso en el proyecto |
|---|---|
| Java 17 | Lenguaje del sistema |
| Maven | Dependencias y construcción (`pom.xml`) |
| Spring Boot 3.2.5 | Spring MVC, inyección de dependencias, validación y Thymeleaf para las vistas |
| JPA (Spring Data JPA + Hibernate) | Entidades `Libro`, `Lector`, `Prestamo` y repositorios |
| MySQL 8 | Base de datos `biblioteca_escolar` |
| JasperReports 6.21.3 | Plantilla `prestamos_pendientes.jrxml`, que consulta MySQL y exporta PDF |
| Git y GitHub | Control de versiones (instrucciones más abajo) |

## Estructura

```
biblioteca-prestamos/
├── database/biblioteca_escolar.sql        Script de creación y datos de prueba
├── application-local.properties.example   Plantilla de configuración local
├── pom.xml
└── src/main
    ├── java/com/biblioteca
    │   ├── model/        Entidades JPA
    │   ├── repository/   Acceso a datos
    │   ├── service/      Reglas de negocio y generación del reporte
    │   └── controller/   Controladores web
    └── resources
        ├── templates/    Vistas Thymeleaf
        ├── static/css/   Estilos
        ├── reportes/     Diseño JasperReports (.jrxml)
        └── application.properties
```

## Requisitos previos

- **JDK 21** (debe ser JDK, no solo JRE: JasperReports compila la plantilla al generar el reporte).
- **Maven 3.9+** (o el Maven integrado de IntelliJ IDEA, Eclipse, NetBeans o VS Code).
- **MySQL 8.0.16 o superior** y MySQL Workbench (el script usa restricciones `CHECK`).
- Conexión a internet la primera vez, para que Maven descargue las dependencias.

## Configurar MySQL y ejecutar el script SQL

1. Verifique que el servidor MySQL esté en ejecución.
2. Abra MySQL Workbench y conéctese con su usuario (por ejemplo `root`).
3. Menú **File → Open SQL Script…** y seleccione `database/biblioteca_escolar.sql`.
4. Ejecute todo el script con el icono del rayo (**Execute**).
5. Compruebe en el panel *Schemas* (botón de actualizar) que existe `biblioteca_escolar` con las tablas `lector`, `libro` y `prestamo`.

El script elimina y vuelve a crear la base `biblioteca_escolar`, por lo que puede ejecutarse de nuevo para restaurar los datos de prueba. Las fechas de los préstamos de ejemplo se calculan respecto a la fecha actual, de modo que siempre hay préstamos vencidos y en plazo.

## Configurar la conexión

Por defecto el sistema usa `localhost:3306`, usuario `root` y contraseña vacía. Para usar su contraseña sin modificar archivos del repositorio:

1. Copie `application-local.properties.example` como `application-local.properties` en la raíz del proyecto (junto al `pom.xml`).
2. Edite usuario, contraseña y, si hace falta, el puerto o la URL.

El archivo `application-local.properties` está en `.gitignore`, así que la contraseña no se sube a GitHub.

## Importar y ejecutar en Eclipse (Windows)

1. Compruebe que Java funciona en una terminal con `java -version`. Este proyecto está configurado para JDK 21.
2. En Eclipse abra **File → Import → Maven → Existing Maven Projects** y seleccione la carpeta que contiene `pom.xml`.
3. Marque el proyecto y pulse **Finish**. Espere a que termine la descarga de dependencias.
4. Si Eclipse pregunta por el JRE, seleccione un **JDK 21** en **Window → Preferences → Java → Installed JREs**. Si no aparece, pulse **Add → Standard VM** y seleccione la carpeta raíz del JDK, no `bin` ni `java.exe`.
5. En **Project → Properties → Java Build Path → Libraries**, confirme que aparece **JavaSE-21**. En **Project → Clean**, limpie y reconstruya el proyecto.
6. Ejecute `BibliotecaApplication.java` con **Run As → Java Application**. También se incluye `BibliotecaApplication.launch` como configuración de ejecución de Eclipse.
7. Abra <http://localhost:8080>.

### Solución al error `CreateProcess error=193`

Si Eclipse intenta ejecutar una ruta como `...\jdk-21.0.12.1\bin\java` y muestra `%1 no es una aplicación Win32 válida`, la configuración de ejecución está apuntando a un ejecutable Java incorrecto o a una ruta incompleta. No es un error de la base de datos.

- No escriba manualmente la ruta terminada en `\bin\java`. Seleccione el JDK desde **Installed JREs**; Eclipse resolverá el ejecutable correcto (`java.exe`).
- La ruta de instalación debe contener `bin\java.exe`. Si ese archivo no existe, vuelva a instalar un JDK de Windows x64 y seleccione su carpeta raíz.
- Abra **Run → Run Configurations → Java Application**, elija `BibliotecaApplication` y en la pestaña **JRE** seleccione **Workspace default JRE** o el JDK 21 correcto. Elimine cualquier ruta antigua de `Downloads`.
- Después use **Project → Clean** y vuelva a ejecutar.

Como alternativa, si Maven está instalado y `mvn -version` funciona, ejecute `ejecutar.bat` desde la carpeta del proyecto o `mvn spring-boot:run` desde una terminal. El archivo `.bat` no configura Eclipse; sirve para comprobar Java/Maven y arrancar desde terminal.

## Cómo probar las funciones

1. **Préstamos** (pantalla inicial): con los datos de prueba aparecen 7 préstamos; 5 siguen pendientes y 2 de ellos están vencidos.
2. **Registrar préstamo:** elija un libro disponible y un lector. El préstamo aparece en la lista y el libro pierde un ejemplar disponible.
3. **Devolución:** pulse *Registrar devolución*; el ejemplar vuelve al stock.
4. **Libros y Lectores:** cree, edite y elimine registros. Pruebe dejar campos vacíos, repetir un código o un correo, o eliminar un libro con préstamos para ver los mensajes de validación.
5. **Libro agotado:** *Conversación en La Catedral* tiene 0 ejemplares disponibles y no aparece al registrar un préstamo.

## Generar el reporte

Pulse **Reporte PDF** en el menú, o abra directamente <http://localhost:8080/reportes/prestamos-pendientes>. Se muestra en el navegador un PDF con lector, libro, fechas, estado y totales; desde el visor puede descargarlo. La consulta SQL está dentro de `src/main/resources/reportes/prestamos_pendientes.jrxml`.

## Uso de Git y GitHub

Desde la carpeta del proyecto:

```bash
git init
git add .
git commit -m "Versión inicial del sistema de préstamos"
git branch -M main
```

Para publicarlo:

1. En GitHub cree un repositorio nuevo y vacío (sin README ni `.gitignore`).
2. Copie su URL y ejecute (reemplace `<usuario>` y `<repositorio>` por los suyos):

```bash
git remote add origin https://github.com/<usuario>/<repositorio>.git
git push -u origin main
```

Para registrar cambios posteriores:

```bash
git status
git add .
git commit -m "Descripción breve del cambio"
git push
```

Si GitHub solicita autenticación, use un *Personal Access Token* en lugar de la contraseña de la cuenta.

## Alcance y limitaciones

- No incluye inicio de sesión ni roles: está pensado para un solo usuario.
- El plazo de préstamo (7 días) es una constante en `PrestamoService`.
- Los préstamos no se editan ni se eliminan; solo se registran y se devuelven, para conservar el historial.
- El esquema de la base de datos se crea únicamente con el script SQL; Hibernate solo lo valida (`ddl-auto=validate`).
