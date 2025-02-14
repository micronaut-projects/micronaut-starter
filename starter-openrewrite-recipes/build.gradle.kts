plugins {
    id("io.micronaut.internal.starter.published-module")
}
val micronautVersion: String by project
repositories {
    mavenCentral()
}
dependencies {
    annotationProcessor(platform("io.micronaut.platform:micronaut-platform:${micronautVersion}"))
    annotationProcessor("io.micronaut:micronaut-inject-java")

    implementation(platform("io.micronaut.platform:micronaut-platform:${micronautVersion}"))
    api(project(":starter-sdk"))
    implementation("io.micronaut:micronaut-http")
    implementation(platform(libs.rewrite.recipe.bom))
    implementation(libs.rewrite.gradle)

    testAnnotationProcessor(platform("io.micronaut.platform:micronaut-platform:${micronautVersion}"))
    testAnnotationProcessor("io.micronaut:micronaut-inject-java")
    testImplementation(platform("io.micronaut.platform:micronaut-platform:${micronautVersion}"))

    testImplementation("io.micronaut.test:micronaut-test-junit5")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}