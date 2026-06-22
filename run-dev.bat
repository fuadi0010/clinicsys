@echo off
:: =====================================================================
:: UAS CLINIC SYSTEM - MICROSERVICES DEVELOPMENT LAUNCHER
:: =====================================================================
:: Script ini meluncurkan seluruh 6 Service Backend Spring Boot secara
:: bertahap dengan jeda waktu inisialisasi, diikuti Frontend Angular.
:: =====================================================================

:: Mengatur Warna Konsol (Text Hijau Terang di atas Latar Belakang Hitam)
color 0A

echo =====================================================================
echo                LAUNCHER MULTI-SERVICE: KLINIK KESEHATAN
echo =====================================================================
echo [INFO] Memulai semua microservice dan frontend secara bertahap...
echo [INFO] Direktori Kerja: %~dp0
echo =====================================================================
echo.

:: 1. Launch AUTH-SERVICE (Port 8081)
echo [1/7] Meluncurkan auth-service (Port 8081)...
start "Backend: auth-service" cmd /k "cd /d %~dp0backend\auth-service && echo [AUTH] Menjalankan auth-service... && mvnw.cmd spring-boot:run"
timeout /t 3 /nobreak >nul

:: 2. Launch PATIENT-SERVICE (Port 8082)
echo [2/7] Meluncurkan patient-service (Port 8082)...
start "Backend: patient-service" cmd /k "cd /d %~dp0backend\patient-service && echo [PATIENT] Menjalankan patient-service... && mvnw.cmd spring-boot:run"
timeout /t 3 /nobreak >nul

:: 3. Launch DOCTOR-SERVICE (Port 8083)
echo [3/7] Meluncurkan doctor-service (Port 8083)...
start "Backend: doctor-service" cmd /k "cd /d %~dp0backend\doctor-service && echo [DOCTOR] Menjalankan doctor-service... && mvnw.cmd spring-boot:run"
timeout /t 3 /nobreak >nul

:: 4. Launch APPOINTMENT-SERVICE (Port 8084)
echo [4/7] Meluncurkan appointment-service (Port 8084)...
start "Backend: appointment-service" cmd /k "cd /d %~dp0backend\appointment-service && echo [APPOINTMENT] Menjalankan appointment-service... && mvnw.cmd spring-boot:run"
timeout /t 3 /nobreak >nul

:: 5. Launch MEDICAL-RECORD-SERVICE (Port 8085)
echo [5/7] Meluncurkan medical-record-service (Port 8085)...
start "Backend: medical-record-service" cmd /k "cd /d %~dp0backend\medical-record-service && echo [MEDICAL-RECORD] Menjalankan medical-record-service... && mvnw.cmd spring-boot:run"
timeout /t 3 /nobreak >nul

:: 6. Launch API-GATEWAY (Port 8080)
echo [6/7] Meluncurkan api-gateway (Port 8080)...
start "Backend: api-gateway" cmd /k "cd /d %~dp0backend\api-gateway && echo [GATEWAY] Menjalankan api-gateway... && mvnw.cmd spring-boot:run"
timeout /t 4 /nobreak >nul

:: 7. Launch CLINIC-FRONTEND (Angular - Port 4200)
echo [7/7] Meluncurkan clinic-frontend (Angular - Port 4200)...
start "Frontend: clinic-frontend" cmd /k "cd /d %~dp0frontend\clinic-frontend && echo [FRONTEND] Menjalankan Angular Dev Server... && npm start"

echo.
echo =====================================================================
echo [SUCCESS] Semua 7 layanan berhasil dipicu peluncurannya!
echo [INFO] Silakan periksa masing-masing jendela konsol yang terbuka.
echo [TIPS] Frontend Angular dapat diakses di: http://localhost:4200
echo =====================================================================
pause
