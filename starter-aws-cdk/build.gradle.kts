plugins {
    id("io.micronaut.internal.build.starter-openapi-module")
}
val micronautVersion: String by project

dependencies {
    api("software.amazon.awscdk:aws-cdk-lib:2.166.0")
    api(project(":starter-core"))

    testImplementation(platform("io.micronaut.platform:micronaut-platform:$micronautVersion"))
    testImplementation("io.micronaut.aws:micronaut-function-aws-api-proxy")
}
