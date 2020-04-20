## Running the function locally

From the root of the project run:

```
$ docker build . -f DockerfileLambda -t micronaut-starter
$ mkdir -p build
$ docker run --rm --entrypoint cat micronaut-starter  /home/application/function.zip > build/function.zip
```

Then start the function with SAM (https://github.com/awslabs/aws-sam-cli).

```cmd
$ sam local start-api --template sam-local.yml
```

And visit http://localhost:3000/

## Deploying the function

TODO
