@echo off
setlocal
set MAVEN_PROJECTBASEDIR=%~dp0
if not defined JAVA_HOME goto noJavaHome
set JAVA_EXE=%JAVA_HOME%\bin\java.exe
goto run
:noJavaHome
set JAVA_EXE=java.exe
:run
"%JAVA_EXE%" -classpath "%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar" "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR:~0,-1%" org.apache.maven.wrapper.MavenWrapperMain %*
if ERRORLEVEL 1 exit /B %ERRORLEVEL%
endlocal
