# Biblioteca Escolar – Control de préstamos

Proyecto académico desarrollado para el curso **Lenguaje de Programación II**.

## Sobre el proyecto

**Biblioteca Escolar** es una aplicación web desarrollada para llevar el control de libros, lectores y préstamos de una biblioteca escolar.

La idea del sistema es sencilla: reemplazar el registro manual en cuadernos por una aplicación que permita saber qué libros están disponibles, quién tiene cada ejemplar y qué préstamos ya vencieron.

El sistema está pensado para ser utilizado por un único bibliotecario, por lo que no cuenta con inicio de sesión ni manejo de usuarios.

## Funcionalidades

- Registro, consulta, edición y eliminación de **libros**.
- Registro, consulta, edición y eliminación de **lectores**.
- Registro de **préstamos** con un plazo de 7 días.
- Registro de **devoluciones** y actualización automática del stock.
- Validaciones para evitar:
  - prestar un libro sin ejemplares disponibles;
  - registrar códigos de libros repetidos;
  - registrar correos de lectores repetidos;
  - eliminar libros o lectores que tengan préstamos registrados.
- Generación de un **reporte PDF** de los préstamos pendientes mediante JasperReports, indicando cuáles se encuentran vencidos.

## Tecnologías

| Tecnología | Uso |
|---|---|
| Java 17 | Lenguaje principal |
| Maven | Gestión de dependencias y construcción del proyecto |
| Spring Boot 3.2.5 | Base de la aplicación web |
| Spring MVC | Controladores y flujo de las peticiones |
| Thymeleaf | Vistas de la aplicación |
| Spring Data JPA + Hibernate | Persistencia y acceso a datos |
| MySQL 8 | Base de datos |
| JasperReports 6.21.3 | Generación del reporte PDF |
| Git + GitHub | Control de versiones |

## Estructura del proyecto

```text
biblioteca-prestamos/
├── database/
│   └── biblioteca_escolar.sql
├── application-local.properties.example
├── pom.xml
└── src/
    └── main/
        ├── java/com/biblioteca/
        │   ├── model/        # Entidades JPA
        │   ├── repository/   # Acceso a la base de datos
        │   ├── service/      # Lógica del sistema
        │   └── controller/   # Controladores web
        │
        └── resources/
            ├── templates/     # Vistas Thymeleaf
            ├── static/css/    # Estilos
            ├── reportes/      # Plantillas JasperReports
            └── application.properties
```

## Requisitos

Antes de ejecutar el proyecto necesitas tener instalado:

- **JDK 21**
- **Maven 3.9 o superior**
- **MySQL 8.0.16 o superior**
- **MySQL Workbench** (recomendado para ejecutar el script)
- Conexión a Internet durante la primera compilación para descargar las dependencias de Maven.

> Aunque el proyecto utiliza Java 17 como versión declarada, para ejecutar y generar los reportes se utiliza **JDK 21**.

## 1. Preparar la base de datos

Primero asegúrate de que MySQL esté ejecutándose.

Abre **MySQL Workbench**, conéctate con tu usuario y abre:

```text
database/biblioteca_escolar.sql
```

Ejecuta el script completo.

Al terminar debería aparecer la base de datos:

```text
biblioteca_escolar
```

con las tablas:

```text
lector
libro
prestamo
```

El script elimina y vuelve a crear la base de datos, por lo que también puede utilizarse para volver a cargar los datos de prueba.

Los préstamos incluidos en los datos de prueba utilizan fechas calculadas respecto a la fecha actual, por lo que siempre se pueden encontrar préstamos pendientes y vencidos para probar el sistema.

## 2. Configurar la conexión a MySQL

La configuración de ejemplo utiliza:

```text
Host: localhost
Puerto: 3306
Usuario: root
Contraseña: vacía
```

Si tu instalación de MySQL utiliza una contraseña, no es necesario modificar el archivo principal del proyecto.

Copia:

```text
application-local.properties.example
```

como:

```text
application-local.properties
```

y configura ahí tus datos de conexión.

Este archivo está incluido en `.gitignore`, por lo que las credenciales locales no deberían terminar en el repositorio.

## 3. Ejecutar el proyecto en Eclipse

1. Comprueba que Java está disponible:

```bash
java -version
```

2. En Eclipse ve a:

**File → Import → Maven → Existing Maven Projects**

3. Selecciona la carpeta donde se encuentra `pom.xml`.

4. Espera a que Maven descargue las dependencias.

5. Si Eclipse solicita un JRE, selecciona un **JDK 21** desde:

**Window → Preferences → Java → Installed JREs**

Si no aparece, agrégalo con **Add → Standard VM** y selecciona la carpeta principal del JDK.

6. En:

**Project → Properties → Java Build Path → Libraries**

verifica que esté seleccionado **JavaSE-21**.

7. Si es necesario, ejecuta:

**Project → Clean**

8. Ejecuta:

```text
BibliotecaApplication.java
```

con **Run As → Java Application**.

También se incluye una configuración de ejecución llamada:

```text
BibliotecaApplication.launch
```

9. Una vez iniciada la aplicación, entra desde:

```text
http://localhost:8080
```

## Si aparece `CreateProcess error=193`

Este error normalmente aparece cuando Eclipse está intentando utilizar una ruta incorrecta para Java.

Por ejemplo, si intenta ejecutar algo parecido a:

```text
...\jdk-21.0.12.1\bin\java
```

verifica que Eclipse tenga configurado el JDK y no una ruta incompleta.

La instalación correcta debe contener:

```text
bin\java.exe
```

En Eclipse:

**Run → Run Configurations → Java Application**

Selecciona `BibliotecaApplication` y revisa la pestaña **JRE**.

Puedes utilizar:

```text
Workspace default JRE
```

si el JDK 21 está configurado correctamente.

Después de corregirlo:

**Project → Clean**

y vuelve a ejecutar la aplicación.

### Otra opción

Si Maven funciona correctamente desde la terminal:

```bash
mvn -version
```

puedes iniciar el proyecto con:

```bash
mvn spring-boot:run
```

También puedes utilizar el archivo:

```text
ejecutar.bat
```

para iniciarlo desde Windows.

## Probar el sistema

Una vez iniciada la aplicación, estas son algunas pruebas rápidas para comprobar las funciones principales.

### Préstamos

En la pantalla inicial aparecen los préstamos de prueba. Hay **7 préstamos**, de los cuales **5 permanecen pendientes** y **2 están vencidos**.

### Registrar un préstamo

Selecciona un libro disponible y un lector.

Al registrar el préstamo:

- se guarda el nuevo préstamo;
- disminuye la cantidad disponible del libro;
- el préstamo aparece en el listado.

### Registrar una devolución

Selecciona un préstamo pendiente y utiliza **Registrar devolución**.

El ejemplar vuelve a estar disponible en el stock.

### Libros y lectores

Desde los módulos correspondientes puedes crear, editar y eliminar registros.

También puedes probar las validaciones utilizando:

- campos vacíos;
- códigos de libro repetidos;
- correos repetidos;
- eliminación de un libro con préstamos registrados;
- eliminación de un lector con préstamos registrados.

### Libro sin stock

El libro:

```text
Conversación en La Catedral
```

tiene actualmente **0 ejemplares disponibles**, por lo que no puede seleccionarse para registrar un nuevo préstamo.

## Reporte de préstamos

El sistema genera un PDF con los préstamos pendientes utilizando **JasperReports**.

Puedes acceder desde el menú **Reporte PDF** o directamente desde:

```text
http://localhost:8080/reportes/prestamos-pendientes
```

El reporte muestra información como:

- lector;
- libro;
- fecha del préstamo;
- fecha límite;
- estado;
- totales.

La plantilla utilizada se encuentra en:

```text
src/main/resources/reportes/prestamos_pendientes.jrxml
```

## Git y GitHub

Para iniciar el repositorio local:

```bash
git init
git add .
git commit -m "Versión inicial del sistema de préstamos"
git branch -M main
```

Después crea un repositorio vacío en GitHub y vincúlalo con:

```bash
git remote add origin https://github.com/<usuario>/<repositorio>.git
git push -u origin main
```

Para subir cambios posteriormente:

```bash
git status
git add .
git commit -m "Descripción breve del cambio"
git push
```

Si GitHub solicita autenticación mediante HTTPS, utiliza un **Personal Access Token** en lugar de la contraseña de la cuenta.

## Alcance actual

El proyecto está enfocado en el control básico de préstamos de una biblioteca escolar.

Actualmente:

- no hay inicio de sesión ni roles;
- está pensado para un único bibliotecario;
- el plazo de cada préstamo es de **7 días**;
- los préstamos no se editan ni eliminan, únicamente se registran y posteriormente se devuelven;
- el historial se conserva mediante los registros de préstamos;
- la estructura de la base de datos se crea mediante el script SQL;
- Hibernate utiliza `ddl-auto=validate`, por lo que no se encarga de crear las tablas.

## Datos de prueba

El script SQL incluido en:

```text
database/biblioteca_escolar.sql
```

deja el proyecto listo para realizar pruebas sin tener que registrar todos los datos manualmente.

Si necesitas restaurar el estado inicial, puedes volver a ejecutar el script desde MySQL Workbench.
