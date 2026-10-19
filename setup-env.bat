@echo off
setlocal enabledelayedexpansion

echo ===================================================
echo Code-Travel: Setup Environment (JDK 25 + JavaFX 25)
echo ===================================================
echo.

REM Ожидаемые имена файлов (исправлено на основе проверки системы)
set JDK_MSI=OpenJDK25U-jdk_x64_windows_hotspot_25.0.2_10.msi
set JAVAFX_ZIP=openjfx-25.0.2_windows-x64_bin-sdk.zip

echo [1/2] Поиск установочных файлов...

REM Поиск файлов в текущей папке и на один уровень выше
set FOUND_JDK=
if exist "%JDK_MSI%" set FOUND_JDK=%JDK_MSI%
if exist "..\%JDK_MSI%" set FOUND_JDK=..\%JDK_MSI%

set FOUND_JFX=
if exist "%JAVAFX_ZIP%" set FOUND_JFX=%JAVAFX_ZIP%
if exist "..\%JAVAFX_ZIP%" set FOUND_JFX=..\%JAVAFX_ZIP%

if not defined FOUND_JDK (
    echo ❌ Файл %JDK_MSI% не найден!
    echo.
    echo Пожалуйста, убедитесь, что файлы скачаны и лежат:
    echo 1. В папке проекта: f:\Рабочий стол\Code-Travel\
    echo 2. Или на рабочем столе: f:\Рабочий стол\
    echo.
    pause
    exit /b 1
)

if not defined FOUND_JFX (
    echo ❌ Файл %JAVAFX_ZIP% не найден!
    pause
    exit /b 1
)

echo ✅ Найдена установка JDK 25: !FOUND_JDK!
echo ✅ Найден архив JavaFX 25: !FOUND_JFX!
echo.

echo [2/2] Установка JDK и JavaFX...

echo Запуск установки JDK... (дождитесь завершения)
start /wait msiexec.exe /i "!FOUND_JDK!" /passive ADDLOCAL=FeatureMain,FeatureEnvironment,FeatureJarFileRunWith,FeatureJavaHome

REM Используем папку пользователя во избежание проблем с правами доступа
set "USER_JAVA_DIR=%USERPROFILE%\Java"
if not exist "!USER_JAVA_DIR!" mkdir "!USER_JAVA_DIR!"

echo Распаковка JavaFX в !USER_JAVA_DIR!...
powershell -Command "Expand-Archive -Path '!FOUND_JFX!' -DestinationPath '!USER_JAVA_DIR!' -Force"

for /d %%i in ("!USER_JAVA_DIR!\javafx-sdk-25*") do (
    if exist "!USER_JAVA_DIR!\javafx-25" rmdir /s /q "!USER_JAVA_DIR!\javafx-25"
    move "%%i" "!USER_JAVA_DIR!\javafx-25"
)

echo.
echo ===================================================
echo Настройка завершена! 
echo.
echo JavaFX установлен в: !USER_JAVA_DIR!\javafx-25
echo Теперь вы можете запустить build-and-run.bat
echo ===================================================
echo.
pause
