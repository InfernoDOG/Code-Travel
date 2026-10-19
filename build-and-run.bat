@echo off
setlocal enabledelayedexpansion

echo Building and Running Code-Travel...
echo.

set "JDK_PATH=C:\Program Files\Eclipse Adoptium\jdk-25.0.2.10-hotspot"
if not exist "!JDK_PATH!\bin\javac.exe" (
    for /d %%i in ("C:\Program Files\Eclipse Adoptium\jdk-25*") do set "JDK_PATH=%%i"
)

set "JFX_PATH=%USERPROFILE%\Java\javafx-25\lib"

if not exist "!JDK_PATH!\bin\javac.exe" (
    echo ERROR: Java 25 not found.
    pause
    exit /b 1
)

if not exist "!JFX_PATH!\javafx.controls.jar" (
    echo ERROR: JavaFX 25 not found.
    pause
    exit /b 1
)

echo [1/3] Cleaning...
if exist "build" rmdir /s /q "build"
mkdir "build\classes"

echo [2/3] Compiling...
"!JDK_PATH!\bin\javac.exe" -d build\classes --module-path "!JFX_PATH!" --add-modules javafx.controls,javafx.fxml,javafx.graphics src\main\java\com\codetravel\core\*.java src\main\java\com\codetravel\entities\*.java src\main\java\com\codetravel\combat\*.java src\main\java\com\codetravel\levels\*.java src\main\java\com\codetravel\ui\*.java src\main\java\com\codetravel\database\*.java

if !ERRORLEVEL! NEQ 0 (
    echo ERROR: Compilation failed.
    pause
    exit /b 1
)

echo [3/3] Packing Resources and FXML...
:: 1. Копируем ресурсы из папки ресурсов (если она есть)
if exist "src\main\resources" xcopy "src\main\resources" "build\classes" /E /I /Y >nul 2>&1

:: 2. НАДЕЖНЫЙ СХАН: Копируем ВСЕ .fxml, .css и картинки прямо из папок с кодом (на случай, если они там)
xcopy "src\main\java\*.fxml" "build\classes" /S /I /Y >nul 2>&1
xcopy "src\main\java\*.css" "build\classes" /S /I /Y >nul 2>&1
xcopy "src\main\java\*.png" "build\classes" /S /I /Y >nul 2>&1
xcopy "src\main\java\*.jpg" "build\classes" /S /I /Y >nul 2>&1

echo Manifest-Version: 1.0 > build\manifest.mf
echo Main-Class: com.codetravel.core.JavaFXGame >> build\manifest.mf
echo Class-Path: lib/sqlite-jdbc-3.45.1.0.jar lib/slf4j-api-2.0.13.jar lib/slf4j-simple-2.0.13.jar >> build\manifest.mf

"!JDK_PATH!\bin\jar.exe" cfm Code-Travel.jar build\manifest.mf -C build\classes .

if "%1"=="--no-run" (
    echo JAR created successfully.
    goto :eof
)

echo Starting Game...
:: Добавили --enable-native-access=ALL-UNNAMED, чтобы убрать предупреждения из консоли
"!JDK_PATH!\bin\java.exe" --enable-native-access=javafx.graphics --enable-native-access=ALL-UNNAMED --module-path "!JFX_PATH!" --add-modules javafx.controls,javafx.fxml,javafx.graphics -cp "lib\sqlite-jdbc-3.45.1.0.jar;lib\slf4j-api-2.0.13.jar;lib\slf4j-simple-2.0.13.jar" -jar Code-Travel.jar

if !ERRORLEVEL! NEQ 0 (
    echo ERROR: Failed to launch game.
    pause
)
pause