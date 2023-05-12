import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage

plugins {
    id("io.micronaut.internal.starter.convention")
    id("io.micronaut.application")
}

dependencies {
    annotationProcessor("io.micronaut.data:micronaut-data-processor")
    annotationProcessor("io.micronaut:micronaut-graal")

    implementation(project(":starter-core"))
    implementation("io.micronaut:micronaut-http-server-netty")
    runtimeOnly("ch.qos.logback:logback-classic")
    implementation("io.micronaut.data:micronaut-data-jdbc")
    implementation("org.postgresql:postgresql:42.6.0")
    implementation("com.google.cloud.sql:postgres-socket-factory:1.11.1")
    implementation("io.micronaut.sql:micronaut-jdbc-hikari")
    implementation("io.micronaut.flyway:micronaut-flyway")
    runtimeOnly("org.yaml:snakeyaml")
    testCompileOnly("io.micronaut:micronaut-inject-groovy")
    testImplementation("io.micronaut:micronaut-http-client")
    testImplementation("io.micronaut.test:micronaut-test-spock")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("ch.qos.logback:logback-classic")
}


application {
    mainClass.set("io.micronaut.starter.analytics.postgres.Main")
}

tasks.named<DockerBuildImage>("dockerBuild") {
    images.add("gcr.io/${System.getenv("GCLOUD_PROJECT")}/micronaut-starter-analytics:${System.getenv("DOCKER_TAG")}")
}

