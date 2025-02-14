plugins {
    id("io.micronaut.internal.starter.published-module")
}
val micronautVersion: String by project
dependencies {
    annotationProcessor(platform("io.micronaut.platform:micronaut-platform:${micronautVersion}"))
    annotationProcessor("io.micronaut:micronaut-inject-java")
    implementation(platform("io.micronaut.platform:micronaut-platform:${micronautVersion}"))
    implementation("io.micronaut:micronaut-inject")
    testAnnotationProcessor(platform("io.micronaut.platform:micronaut-platform:${micronautVersion}"))
    testAnnotationProcessor("io.micronaut:micronaut-inject-java")
    testImplementation(platform("io.micronaut.platform:micronaut-platform:${micronautVersion}"))
}
