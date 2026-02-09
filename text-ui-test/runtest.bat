@ECHO OFF
SETLOCAL ENABLEDELAYEDEXPANSION

SET SCRIPT_DIR=%~dp0
SET SRC_DIR=%SCRIPT_DIR%..\src\main\java
SET BIN_DIR=%SCRIPT_DIR%..\bin
SET FX_PATH=%JAVA_HOME%\lib

REM create bin directory if it doesn't exist
if not exist "%BIN_DIR%" mkdir "%BIN_DIR%"

REM delete output from previous run
if exist "%SCRIPT_DIR%ACTUAL.TXT" del "%SCRIPT_DIR%ACTUAL.TXT"

REM collect Java source files recursively
SET FILES=
for /r "%SRC_DIR%" %%f in (*.java) do (
    SET FILES=!FILES! "%%f"
)

REM compile with JavaFX modules
javac ^
  --module-path "%FX_PATH%" ^
  --add-modules javafx.controls,javafx.fxml ^
  -Xlint:none ^
  -d "%BIN_DIR%" ^
  %FILES%

IF ERRORLEVEL 1 (
    echo ********** BUILD FAILURE **********
    exit /b 1
)

REM run with JavaFX modules
java ^
  --module-path "%FX_PATH%" ^
  --add-modules javafx.controls,javafx.fxml ^
  -cp "%BIN_DIR%" ^
  her.m35.HERM35 ^
  < "%SCRIPT_DIR%input.txt" > "%SCRIPT_DIR%ACTUAL.TXT"

REM compare the output
FC "%SCRIPT_DIR%ACTUAL.TXT" "%SCRIPT_DIR%EXPECTED.TXT"
IF ERRORLEVEL 1 (
    echo Test result: FAILED
    exit /b 1
)

echo Test result: PASSED
exit /b 0