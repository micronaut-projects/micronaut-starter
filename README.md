# Micronaut Starter

[![Maven Central](https://img.shields.io/maven-central/v/io.micronaut.starter/micronaut-starter.svg?label=Maven%20Central)](https://search.maven.org/artifact/io.micronaut.starter/micronaut-starter)
[![Build Status](https://github.com/micronaut-projects/micronaut-starter/workflows/Java%20CI/badge.svg)](https://github.com/micronaut-projects/micronaut-starter/actions)

Generates Micronaut applications.

## Snapshots and Releases

Snaphots are automatically published to [JFrog OSS](https://oss.jfrog.org/artifactory/oss-snapshot-local/) using [Github Actions](https://github.com/micronaut-projects/micronaut-aws/actions).

See the documentation in the [Micronaut Docs](https://docs.micronaut.io/latest/guide/index.html#usingsnapshots) for how to configure your build to use snapshots.

Releases are published to JCenter and Maven Central via [Github Actions](https://github.com/micronaut-projects/micronaut-aws/actions).

A release is performed with the following steps:

* [Publish the draft release](https://github.com/micronaut-projects/micronaut-aws/releases). There should be already a draft release created, edit and publish it. The Git Tag should start with `v`. For example `v1.0.0`.
* [Monitor the Workflow](https://github.com/micronaut-projects/micronaut-aws/actions?query=workflow%3ARelease) to check it passed successfully.
* Celebrate!


## Deployments

To generate an [AWS Lambda Custom Runtime](https://docs.aws.amazon.com/lambda/latest/dg/runtimes-custom.html) which runs a native image of the app, run: 

`$ ./deploy.sh`

It generates a zip file `build/function.zip` which you must upload to AWS Lambda. Set `Handler` to `io.micronaut.starter.lambda.CustomMicronautLambdaRuntime`.  