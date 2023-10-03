# Micronaut Launch

[![Maven Central](https://img.shields.io/maven-central/v/io.micronaut.starter/micronaut-starter-core.svg?label=Maven%20Central)](https://search.maven.org/artifact/io.micronaut.starter/micronaut-starter-core)
[![Build Status](https://github.com/micronaut-projects/micronaut-starter/workflows/Java%20CI/badge.svg)](https://github.com/micronaut-projects/micronaut-starter/actions)
[![Revved up by Gradle Enterprise](https://img.shields.io/badge/Revved%20up%20by-Gradle%20Enterprise-06A0CE?logo=Gradle&labelColor=02303A)](https://ge.micronaut.io/scans)

Generates Micronaut applications.

## Installation

The CLI application comes in various flavours from a universal Java applications to native applications for Windows, Linux and OS X. These are available for direct download on the [releases page](https://github.com/micronaut-projects/micronaut-starter/releases). For installation see the [Micronaut documentation](https://docs.micronaut.io/latest/guide/index.html#buildCLI).

If you prefer not to install an application to create Micronaut applications you can do so with `curl` directly from the API:

```bash
$ curl https://launch.micronaut.io/demo.zip -o demo.zip
$ unzip demo.zip -d demo
$ cd demo
$ ./gradlew run
```

Run `curl https://launch.micronaut.io` for more information on how to use the API or see the API documentation referenced below.

## UI

If you prefer a browser based user interface you can visit [Micronaut Launch](https://micronaut.io/launch).

The user interface is [written in React](https://github.com/micronaut-projects/micronaut-starter-ui) and is a static single page application that simply interacts with the https://launch.micronaut.io API.

## API

API documentation for the production instance can be found at:

* [Swagger / OpenAPI Doc](https://launch.micronaut.io/swagger/views/swagger-ui/index.html)
* [RAPI Doc](https://launch.micronaut.io/swagger/views/rapidoc/index.html)

API documentation for the snapshot /development instance can be found at:

* [Swagger / OpenAPI Doc](https://snapshot.micronaut.io/swagger/views/swagger-ui/index.html)
* [RAPI Doc](https://snapshot.micronaut.io/swagger/views/rapidoc/index.html)

## Documentation

<!-- See the [Documentation](https://micronaut-projects.github.io/micronaut-starter/1.0.x/guide/) for more information. -->

See the [Snapshot Documentation](https://micronaut-projects.github.io/micronaut-starter/snapshot/guide/) for the current development docs.

## Snapshots and Releases

Snaphots are automatically published to [Sonatype OSSRH](https://s01.oss.sonatype.org/content/repositories/snapshots/) using [Github Actions](https://github.com/micronaut-projects/micronaut-starter/actions).

See the documentation in the [Micronaut Docs](https://docs.micronaut.io/latest/guide/index.html#usingsnapshots) for how to configure your build to use snapshots.

Releases are published to Maven Central via [Github Actions](https://github.com/micronaut-projects/micronaut-starter/actions).

A release is performed with the following steps:

* [Publish the draft release](https://github.com/micronaut-projects/micronaut-starter/releases). There should be already a draft release created, edit and publish it. The Git Tag should start with `v`. For example `v1.0.0`.
* [Monitor the Workflow](https://github.com/micronaut-projects/micronaut-starter/actions?query=workflow%3ARelease) to check it passed successfully.
* Celebrate!



