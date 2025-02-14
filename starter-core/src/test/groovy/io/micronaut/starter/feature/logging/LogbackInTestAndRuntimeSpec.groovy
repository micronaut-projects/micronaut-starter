package io.micronaut.starter.feature.logging

import io.micronaut.context.annotation.Requires
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.sdk.dependency.Dependency
import io.micronaut.starter.sdk.dependency.Scope
import io.micronaut.starter.feature.Feature
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.sdk.BuildTool
import io.micronaut.starter.options.Language
import jakarta.inject.Singleton

class LogbackInTestAndRuntimeSpec  extends ApplicationContextSpec implements CommandOutputFixture {
    private static final String ARTIFACT_ID_LOGBACK_CLASSIC = "logback-classic"
    private static final String GROUP_ID_LOGBACK = "ch.qos.logback"

    Map<String, Object> getConfiguration() {
        return super.getConfiguration() + ["spec.name": "LogbackInTestAndRuntimeSpec"]
    }

    void "compile if runtime and test dependency"() {
        given:
        BuildTool buildTool = BuildTool.MAVEN
        Language language = Language.JAVA
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features(["test-logback"])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        verifier.hasDependency(GROUP_ID_LOGBACK, ARTIFACT_ID_LOGBACK_CLASSIC, Scope.RUNTIME)
        !verifier.hasDependency(GROUP_ID_LOGBACK, ARTIFACT_ID_LOGBACK_CLASSIC, Scope.TEST)
    }

    @Requires(property = "spec.name", value = "LogbackInTestAndRuntimeSpec")
    @Singleton
    static class TestLogback implements Feature {

        @Override
        String getName() {
            return "test-logback"
        }

        @Override
        String getTitle() {
            return "Test Logback"
        }

        @Override
        void apply(GeneratorContext generatorContext) {
            generatorContext.addDependency(Dependency.builder().groupId(GROUP_ID_LOGBACK).artifactId(ARTIFACT_ID_LOGBACK_CLASSIC).scope(Scope.TEST).build())
        }

        @Override
        boolean supports(ApplicationType applicationType) {
            true
        }
    }
}
