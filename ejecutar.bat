@echo off
setlocal
cd /d "%~dp0"

echo ============================================
echo   Biblioteca Escolar - Control de prestamos
echo ============================================
echo.

where java >nul 2>nul
if errorlevel 1 (
  echo ERROR: Java no esta disponible en PATH.
  echo Instale un JDK 21 y configure JAVA_HOME y PATH.
  echo Ejemplo JAVA_HOME: C:\Program Files\Eclipse Adoptium\jdk-21...
  pause
  exit /b 1
)

java -version
echo.
where mvn >nul 2>nul
if errorlevel 1 (
  echo ERROR: No se encontro Maven en PATH.
  echo Importe la carpeta en Eclipse como Existing Maven Project
  echo y ejecute BibliotecaApplication.java desde Eclipse.
  echo Tambien puede instalar Maven y volver a ejecutar este archivo.
  pause
  exit /b 1
)

echo Iniciando la aplicacion...
call mvn spring-boot:run
if errorlevel 1 (
  echo.
  echo La aplicacion no pudo iniciar. Revise el error anterior,
  echo la conexion con MySQL y la base biblioteca_escolar.
  pause
  exit /b 1
)
endlocal
