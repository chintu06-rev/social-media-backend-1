@echo off
title Launch SocialSync Full Stack
echo ========================================================
echo  Launching SocialSync Full Stack System...
echo ========================================================

start "SocialSync Backend (Port 8080)" cmd /k "%~dp0run-backend.bat"
timeout /t 3 /nobreak >nul
start "SocialSync Frontend (Port 5173)" cmd /k "%~dp0run-frontend.bat"

echo.
echo Both services are booting!
echo Frontend will be accessible at: http://localhost:5173
echo Backend API will run at:        http://localhost:8080
echo.
