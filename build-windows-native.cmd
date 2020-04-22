@echo off
@REM restore the normal current directory in case script was run with 'Run as Admin'
pushd "%~dp0"

@REM install GraalVM

set JAVA_VERSION=11
set GRAAL_VERSION=20.0.0
set GRAAL_FILE=graalvm-ce-java%JAVA_VERSION%-windows-amd64-%GRAAL_VERSION%.zip
set GRAAL_19_2_URL=https://github.com/oracle/graal/releases/download/vm-%GRAAL_VERSION%
set DOWNLOAD_URL=https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-%GRAAL_VERSION%/%GRAAL_FILE%
set CACHE_DIR=%USERPROFILE%\.m2\caches\info.picocli.graal

set JAVA_HOME=%CACHE_DIR%\graalvm\win\graalvm-ce-java%JAVA_VERSION%-%GRAAL_VERSION%\


if exist "%JAVA_HOME%" GOTO graal_already_installed
mkdir %CACHE_DIR%\graalvm\win
echo Downloading %DOWNLOAD_URL%. This may take a while.
pushd "%CACHE_DIR%\"
powershell -Command "(New-Object Net.WebClient).DownloadFile('%DOWNLOAD_URL%', '%GRAAL_FILE%')"
powershell -Command "Expand-Archive %GRAAL_FILE% graalvm\win"
popd
goto install-windows-sdk-7.1

:graal_already_installed
echo %CACHE_DIR%\graalvm\graalvm-ce-%GRAAL_VERSION%\ exists: skipping GraalVM download

@REM install windows-sdk-7.1 and C++ compilers if necessary
:install-windows-sdk-7.1
if not %JAVA_VERSION%==8 GOTO install-visualstudio2017-workload-vctools
if not exist "C:\Program Files\Microsoft SDKs\Windows\v7.1\" choco install windows-sdk-7.1 kb2519277

@REM activate the sdk-7.1 environment:
echo Activating the sdk-7.1 environment
call "C:\Program Files\Microsoft SDKs\Windows\v7.1\Bin\SetEnv.cmd"
@REM The above starts a new Command Prompt, with the sdk-7.1 environment enabled.
@REM The Maven build command (`mvnw clean verify`) must be run in this Command Prompt window.
GOTO set-java-home

@REM install Visual C++ build tools workload for Visual Studio 2017 Build Tools if necessary
:install-visualstudio2017-workload-vctools
if not exist "C:\Program Files (x86)\Microsoft Visual Studio\2017\" choco install visualstudio2017-workload-vctools

@REM activate the Visual Studio 2017 environment for Java 11
:activate-visual-studio-2017
echo Activating the Visual Studio 2017 environment
if exist "C:\Program Files (x86)\Microsoft Visual Studio\2017\BuildTools\VC\Auxiliary\Build\vcvars64.bat" (
  call "C:\Program Files (x86)\Microsoft Visual Studio\2017\BuildTools\VC\Auxiliary\Build\vcvars64.bat"
) else (
  if exist "C:\Program Files (x86)\Microsoft Visual Studio\2017\Community\VC\Auxiliary\Build\vcvars64.bat" (
    call "C:\Program Files (x86)\Microsoft Visual Studio\2017\Community\VC\Auxiliary\Build\vcvars64.bat"
  ) else (
    if exist "C:\Program Files (x86)\Microsoft Visual Studio\2017\Enterprise\VC\Auxiliary\Build\vcvars64.bat" (
      call "C:\Program Files (x86)\Microsoft Visual Studio\2017\Enterprise\VC\Auxiliary\Build\vcvars64.bat"
    )
  )
)
set MAVEN_OPTS=--add-exports=java.base/jdk.internal.module=ALL-UNNAMED

echo "Install Native Image"
%JAVA_HOME%\bin\gu install native-image
echo "GraalVM %GRAAL_VERSION% Install Complete."

echo "Building Micronaut Starter JAR"
.\gradlew.bat clean starter-cli:shadowJar --no-daemon

echo "Building Native Image"
powershell -Command "%JAVA_HOME%\bin\native-image --no-fallback --no-server -cp @(gci .\starter-cli\build\libs\starter-cli-*-all.jar)[0]"

