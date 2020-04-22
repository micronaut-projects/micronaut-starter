call "C:\Program Files (x86)\Microsoft Visual Studio\2017\BuildTools\VC\Auxiliary\Build\vcvars64.bat"
echo "Building JAR File"
./gradlew starter-cli:shadowJar --no-daemon
for %%x in (starter-cli\build\libs\*.jar) do if not defined jarFile set "jarFile=%%x"
echo "Building Native Image for JAR %jarFle%"
native-image --no-server --no-fallback -cp "%jarFile%"