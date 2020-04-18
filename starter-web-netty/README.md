## Running the app locally

From the project root:

```cmd
./gradlew clean starter-web-netty:run
```

And visit http://localhost:8080/features

## Google Cloud Run Deployment

From the project root build the docker image and push it to Google Container Registry:

```
$ docker build . -t micronaut-starter
$ docker tag micronaut-starter gcr.io/[PROJECT ID]/micronaut-starter
$ docker push gcr.io/[PROJECT ID]/micronaut-starter
```

You are now ready to deploy your application:

```
$ gcloud beta run deploy --image gcr.io/[PROJECT ID]/micronaut-starter 
```

Where `[PROJECT ID]` is replaced for your project ID. You should see output like the following:

```
Service name: (micronaut-starter):
Deploying container to Cloud Run service [micronaut-starter] in project [PROJECT_ID] region [us-central1]

✓ Deploying... Done.
  ✓ Creating Revision...
  ✓ Routing traffic...
Done.
Service [micronaut-starter] revision [micronaut-starter-00004] has been deployed and is serving traffic at https://micronaut-starter-9487r97234-uc.a.run.app
```

The URL is the URL of your Cloud Run application.%