@echo off
set DIR="%~dp0"
set JAVA_EXEC="%DIR:"=%\javaw"



pushd %DIR% & start "pacman.game" %JAVA_EXEC% %CDS_JVM_OPTS% --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.io=ALL-UNNAMED -Djavafx.animation.fullspeed=true -Dfile.encoding=UTF-8 -p "%~dp0/../app" -m pacman.game/pacman.App  %* & popd
