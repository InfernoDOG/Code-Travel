@echo off
start "" "%~dp0runtime\bin\javaw.exe"     --module-path "%~dp0app\lib"     --add-modules javafx.controls,javafx.fxml,javafx.graphics     --enable-native-access=javafx.graphics     -jar "%~dp0app\Code-Travel.jar"
