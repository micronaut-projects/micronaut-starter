## Running the function locally

```cmd
./gradlew clean starter-gcp-function:runFunction
```

And visit http://localhost:8081/application-types

## Deploying the function

First build the function with:

```bash
$ ./gradlew clean starter-gcp-function:shadowJar
```

Then `cd` into the `starter-gcp-function/build/libs` directory (deployment has to be done from the location where the JAR lives):

```bash
$ cd starter-gcp-function/build/libs
```

Now run:

```bash
$ gcloud alpha functions deploy micronaut-starter --entry-point io.micronaut.gcp.function.http.HttpFunction --runtime java11 --trigger-http
```

Choose unauthenticated access if you don't need auth.

To obtain the trigger URL do the following:

```bash
$ YOUR_HTTP_TRIGGER_URL=$(gcloud alpha functions describe micronaut-starter --format='value(httpsTrigger.url)')
```

You can then use this variable to test the function invocation:

```bash
$ curl -i $YOUR_HTTP_TRIGGER_URL/create/app/example --output application.zip
```%
