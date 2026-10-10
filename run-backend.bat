@echo off
title SocialSync Backend (Spring Boot)
echo ========================================================
echo  Starting SocialSync Spring Boot Backend
echo  Port: 8080
echo  H2 Console: http://localhost:8080/h2-console
echo ========================================================
cd /d "%~dp0backend"
mvn.cmd spring-boot:run
pause
