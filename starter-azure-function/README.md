## Running the function locally

First uncomment the Azure plugin from `starter-azure-function/build.grade`:

```cmd
./gradlew clean starter-azure-function:azureFunctionsRun
```

And visit http://localhost:7071/api/application-types

## Deploying the function


```cmd
./gradlew clean starter-azure-function:azureFunctionsDeploy
```
