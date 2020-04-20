docker build . -f DockerfileLambda -t micronaut-starter
mkdir -p build
docker run --rm --entrypoint cat micronaut-starter  /home/application/function.zip > build/function.zip