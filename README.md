# Micronaut Starter

[![Maven Central](https://img.shields.io/maven-central/v/io.micronaut.starter/micronaut-starter-core.svg?label=Maven%20Central)](https://search.maven.org/artifact/io.micronaut.starter/micronaut-starter-core)
[![Build Status](https://github.com/micronaut-projects/micronaut-starter/workflows/Java%20CI/badge.svg)](https://github.com/micronaut-projects/micronaut-starter/actions)

Generates Micronaut applications.

## UI

[Micronaut Launch](https://micronaut.io/launch)

## Documentation

<!-- See the [Documentation](https://micronaut-projects.github.io/micronaut-starter/1.0.x/guide/) for more information. -->

See the [Snapshot Documentation](https://micronaut-projects.github.io/micronaut-starter/snapshot/guide/) for the current development docs.

## Snapshots and Releases

Snaphots are automatically published to [JFrog OSS](https://oss.jfrog.org/artifactory/oss-snapshot-local/) using [Github Actions](https://github.com/micronaut-projects/micronaut-starter/actions).

See the documentation in the [Micronaut Docs](https://docs.micronaut.io/latest/guide/index.html#usingsnapshots) for how to configure your build to use snapshots.

Releases are published to JCenter and Maven Central via [Github Actions](https://github.com/micronaut-projects/micronaut-starter/actions).

A release is performed with the following steps:

* [Publish the draft release](https://github.com/micronaut-projects/micronaut-starter/releases). There should be already a draft release created, edit and publish it. The Git Tag should start with `v`. For example `v1.0.0`.
* [Monitor the Workflow](https://github.com/micronaut-projects/micronaut-starter/actions?query=workflow%3ARelease) to check it passed successfully.
* Celebrate!



