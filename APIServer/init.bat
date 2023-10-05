REM Clean and build with Maven
call mvn clean package test
REM Build the Docker image
call docker build -t api-image .
REM Run the Docker container
@echo off
setlocal enabledelayedexpansion

for /f "tokens=2 delims=:" %%a in ('ipconfig ^| findstr "IPv4"') do (
    set ip=%%a
)

echo !ip:~1!

call docker run --name api-server-container -e SPRING_DATASOURCE_URL=jdbc:mysql://!ip:~1!:3306/apiserver -p 8080:8080 api-image