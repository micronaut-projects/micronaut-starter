package io.micronaut.starter.build.dependencies

import io.micronaut.starter.build.gradle.GradleConfiguration
import io.micronaut.starter.build.gradle.GradleDependency
import io.micronaut.starter.build.gradle.GradleDependencyComparator
import spock.lang.Specification

class GradleDependencyComparatorSpec extends Specification {

    void "sort based on gradle configuration"() {
        given:
        List<GradleDependency> dependencies = [
                dep(GradleConfiguration.IMPLEMENTATION, "io.micronaut:micronaut-validation"),
                dep(GradleConfiguration.IMPLEMENTATION, "io.swagger.core.v3:swagger-annotations"),
                dep(GradleConfiguration.IMPLEMENTATION, "io.micronaut:micronaut-runtime"),
                dep(GradleConfiguration.IMPLEMENTATION, "javax.annotation:javax.annotation-api"),
                dep(GradleConfiguration.IMPLEMENTATION, "io.micronaut:micronaut-http-client"),
                dep(GradleConfiguration.ANNOTATION_PROCESSOR, "io.micronaut.openapi:micronaut-openapi"),
                dep(GradleConfiguration.IMPLEMENTATION, "io.micronaut.sql:micronaut-jdbc-hikari"),
                dep(GradleConfiguration.TEST_IMPLEMENTATION, "org.testcontainers:testcontainers"),
                dep(GradleConfiguration.RUNTIME_ONLY, "mysql:mysql-connector-java"),
                dep(GradleConfiguration.TEST_IMPLEMENTATION, "org.testcontainers:junit-jupiter"),
                dep(GradleConfiguration.TEST_IMPLEMENTATION, "org.testcontainers:mysql"),
                dep(GradleConfiguration.RUNTIME_ONLY, "ch.qos.logback:logback-classic"),
        ]

        when:
        dependencies.sort(new GradleDependencyComparator())

        then:
        "${str(dependencies[0])}" == 'annotationProcessor("io.micronaut.openapi:micronaut-openapi")'
        "${str(dependencies[1])}" == 'implementation("io.micronaut:micronaut-http-client")'
        "${str(dependencies[2])}" == 'implementation("io.micronaut:micronaut-runtime")'
        "${str(dependencies[3])}" == 'implementation("io.micronaut:micronaut-validation")'
        "${str(dependencies[4])}" == 'implementation("io.micronaut.sql:micronaut-jdbc-hikari")'
        "${str(dependencies[5])}" == 'implementation("io.swagger.core.v3:swagger-annotations")'
        "${str(dependencies[6])}" == 'implementation("javax.annotation:javax.annotation-api")'
        "${str(dependencies[7])}" == 'runtimeOnly("ch.qos.logback:logback-classic")'
        "${str(dependencies[8])}" == 'runtimeOnly("mysql:mysql-connector-java")'
        "${str(dependencies[9])}" == 'testImplementation("org.testcontainers:junit-jupiter")'
        "${str(dependencies[10])}" == 'testImplementation("org.testcontainers:mysql")'
        "${str(dependencies[11])}" == 'testImplementation("org.testcontainers:testcontainers")'
    }

    private static String str(GradleDependency dependency) {
        "${dependency.getConfiguration().toString()}(\"${dependency.groupId}:${dependency.artifactId}\")"
    }

    private static GradleDependency dep(GradleConfiguration configuration, String coordinate) {
        new GradleDependency(configuration, coordinate.split(":")[0] as String, coordinate.split(":")[1] as String, null)
    }
}
