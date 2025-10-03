package io.micronaut.starter.feature.build.gradle

import io.micronaut.starter.build.gradle.GradlePlugin
import spock.lang.Specification

class MicronautOpenrewriteGradlePluginSpec extends Specification {

    void "test Micronaut OpenRewrite Gradle plugin build"() {
        when:
        GradlePlugin gradlePlugin = MicronautOpenrewriteGradlePlugin.builder().build();

        then:
        gradlePlugin.id
    }
}
