plugins {
    `java-library`
}
val micronautVersion: String by project
repositories {
    mavenCentral()
    maven {
        url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
        mavenContent {
            snapshotsOnly()
        }
    }
}
dependencies {
    annotationProcessor(platform("io.micronaut.platform:micronaut-platform:${micronautVersion}"))
    annotationProcessor("io.micronaut:micronaut-inject-java")

    implementation(platform("io.micronaut.platform:micronaut-platform:${micronautVersion}"))
    api(project(":starter-sdk"))
    implementation("io.micronaut:micronaut-http")
    implementation(platform(libs.rewrite.recipe.bom))
    implementation(libs.rewrite.core)
    implementation(libs.rewrite.maven)
    implementation(libs.rewrite.gradle)
    implementation(libs.rewrite.java.dependencies) {
        exclude(group = "org.openrewrite", module = "rewrite-groovy")
    }
    testAnnotationProcessor(platform("io.micronaut.platform:micronaut-platform:${micronautVersion}"))
    testAnnotationProcessor("io.micronaut:micronaut-inject-java")
    testImplementation(platform("io.micronaut.platform:micronaut-platform:${micronautVersion}"))
    testImplementation("io.micronaut.test:micronaut-test-junit5")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}
tasks.withType<Test> {
    useJUnitPlatform()
}
java {
    sourceCompatibility = JavaVersion.toVersion("17")
    targetCompatibility = JavaVersion.toVersion("17")
}
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}