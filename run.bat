@echo off
chcp 65001 > nul
echo ========================================================
echo        CENTERPOINT - Cửa hàng Laptop
echo ========================================================
echo.
echo [1] Kiem tra Java...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [LOI] Khong tim thay Java! Vui long cai dat JDK 17 tro len.
    pause
    exit /b
)
echo [OK] Java da duoc cai dat.
echo.
echo [2] Dang tai thu vien tu dong va Khoi dong Server...
echo [INFO] Neu day la lan dau chay, qua trinh tai thu vien co the mat vai phut...
echo.
call .\mvnw spring-boot:run
pause
