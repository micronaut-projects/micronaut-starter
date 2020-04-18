## Running the app locally

```cmd
./gradlew clean starter-web-netty:run
```

And visit http://localhost:8080/features

## Google Cloud Run Deployment

First prepare the image using Google Cloud Build:

```
$ gcloud builds submit --tag gcr.io/[PROJECT-ID]/micronaut-starter --timeout 30m
```

This delegates building of the image to GCP infrastructure. The build process may take some time as the GraalVM compiler is not the fastest.

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