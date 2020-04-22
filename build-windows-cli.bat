call "C:\Program Files (x86)\Microsoft Visual Studio\2017\BuildTools\VC\Auxiliary\Build\vcvars64.bat"
echo "Building JAR File"
./gradlew starter-cli:copyShadowJar
native-image --no-server --no-fallback -cp build/libs/cli.jar