@echo off
set "GIT_PATH=C:\Program Files\Git\cmd\git.exe"

echo ==========================================
echo LensVault - Push to GitHub Helper
echo ==========================================

if not exist "%GIT_PATH%" (
    echo [ERROR] Git executable not found at: %GIT_PATH%
    echo Please restart your computer or terminal to update your PATH variable.
    pause
    exit /b
)

echo.
echo Git found!
echo.

if "%~1"=="" (
    set /p REPO_URL=">> Paste your GitHub Repository URL here: "
) else (
    set REPO_URL=%~1
)

if "%REPO_URL%"=="" (
    echo [ERROR] No Repository URL provided.
    pause
    exit /b
)

echo.
echo Linking to remote: %REPO_URL%
"%GIT_PATH%" remote remove origin >nul 2>&1
"%GIT_PATH%" remote add origin %REPO_URL%

echo.
echo Adding and Committing changes...
"%GIT_PATH%" add .
"%GIT_PATH%" commit -m "Update from local builder"

echo.
echo Pushing code...
"%GIT_PATH%" branch -M main
"%GIT_PATH%" push -u origin main

echo.
echo Done! check your GitHub repository.
pause
