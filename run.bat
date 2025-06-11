@echo off

:: backend (Spring Boot)
start "BACKEND" cmd /k "cd backend && mvnw.cmd spring-boot:run"

:: frontend (React)
start "FRONTEND" cmd /k "cd frontend && npm start"

:: PostgreSQL w Dockerze
docker-compose up

