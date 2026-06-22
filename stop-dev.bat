@echo off
echo ==========================================
echo   MEMATIKAN SEMUA SERVICE KLINIK (UAS)
echo ==========================================

echo 1. Mematikan Service Angular (Port 4200)...
for /f "tokens=5" %%a in ('netstat -aon ^| findstr :4200 ^| findstr LISTENING') do taskkill /f /pid %%a 2>nul

echo 2. Mematikan Service Backend Spring Boot...
:: Jalur ini akan mematikan proses Java yang berjalan
taskkill /f /im java.exe 2>nul

echo 3. Menutup semua jendela CMD sisa...
taskkill /f /im cmd.exe /fi "WINDOWTITLE eq Menjalankan*" 2>nul

echo ==========================================
echo   Semua service BERHASIL dimatikan!
echo ==========================================
pause