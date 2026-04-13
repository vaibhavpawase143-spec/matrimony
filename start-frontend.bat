@echo off
echo ========================================
echo   Starting Gathbandhan Frontend
echo ========================================
echo.

echo [1/1] Starting Frontend Server...
cd /d "%~dp0"
start "Frontend Server" cmd /k "npm run dev"

echo.
echo ========================================
echo   Frontend Server Started!
echo ========================================
echo.
echo   Frontend: http://localhost:5173
echo.
echo   Press any key to open the application...
pause >nul
start http://localhost:5173

echo.
echo   To stop server: Close the terminal window
echo   Or press Ctrl+C in the terminal
pause
