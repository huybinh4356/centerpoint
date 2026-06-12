@echo off
echo ========================================================
echo        STARTING CENTERPOINT SPRING BOOT APP
echo ========================================================
echo.
echo The website will automatically open in your browser in 10 seconds...

:: Chạy ngầm một tiến trình đếm ngược 10 giây rồi mở trình duyệt
start cmd /c "timeout /t 10 /nobreak >nul && start http://localhost:8081"

:: Chạy Spring Boot ở cửa sổ hiện tại
call mvnw.cmd spring-boot:run

pause
