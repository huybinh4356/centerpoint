@echo off
echo ========================================================
echo   Starting CenterPoint Spring Boot App in DEBUG MODE
echo ========================================================
echo Debugger port listening on: 5005 (Suspend=N)
echo Attach your IDE debugger to localhost:5005
call mvnw.cmd spring-boot:run -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"
pause
