package io.micronaut.starter.build.gradle

import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import spock.lang.Specification
import spock.lang.Unroll

class GradleBuildTestVerifierSpec extends Specification {

    @Unroll
    void "hasDependencyRegex"(boolean expected, String template, String groupId, String artifactId, String scope) {
        given:
        GradleBuildTestVerifier verifier = new GradleBuildTestVerifier(template, Language.JAVA, TestFramework.JUNIT)

        expect:
        expected == verifier.hasDependency(groupId, artifactId, scope)

        where:
        expected | template | groupId | artifactId | scope
        false    | 'implementation("io.micronaut.oraclecloud:micronaut-oraclecloud-function-http")'       |  "io.micronaut.oraclecloud" | "micronaut-oraclecloud-function-http" | GradleConfiguration.TEST_IMPLEMENTATION.toString()
        true     | 'implementation("io.micronaut.oraclecloud:micronaut-oraclecloud-function-http")'       |  "io.micronaut.oraclecloud" | "micronaut-oraclecloud-function-http" | GradleConfiguration.IMPLEMENTATION.toString()
        true     | 'implementation("io.micronaut.oraclecloud:micronaut-oraclecloud-function-http:2.0.0")' |  "io.micronaut.oraclecloud" | "micronaut-oraclecloud-function-http" | GradleConfiguration.IMPLEMENTATION.toString()
        false    | 'implementation("io.micronaut.oraclecloud:micronaut-oraclecloud-function-http")'       |  "io.micronaut"             | "micronaut-oraclecloud-function-http" | GradleConfiguration.IMPLEMENTATION.toString()
        false    | 'implementation("io.micronaut.oraclecloud:micronaut-oraclecloud-function-http")'       |  "io.micronaut.oraclecloud" | "micronaut-oraclecloud-function"      | GradleConfiguration.IMPLEMENTATION.toString()

    }
}
