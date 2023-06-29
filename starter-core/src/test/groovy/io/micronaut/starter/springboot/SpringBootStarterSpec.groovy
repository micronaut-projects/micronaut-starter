package io.micronaut.starter.springboot

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language


class SpringBootStarterSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void "it is possible to generate a SpringBoot application"(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .framework(SpringBootFramework.FRAMEWORK_SPRING_BOOT)
                .features([SpringBootStarter.NAME])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        verifier.hasBuildPlugin("java")
        verifier.hasBuildPlugin("org.springframework.boot")
        verifier.hasBuildPlugin("io.spring.dependency-management")
        !verifier.hasBuildPlugin("io.micronaut.application")
        !verifier.hasBuildPlugin("com.github.johnrengelman.shadow")
        verifier.hasDependency("org.springframework.boot", "spring-boot-starter", Scope.COMPILE)
        verifier.hasDependency("org.springframework.boot", "spring-boot-starter-test", Scope.TEST)
        template.contains("mavenCentral()")
        template.contains("useJUnitPlatform()")


        where:
        [language, buildTool] << [[Language.JAVA], BuildTool.valuesGradle()].combinations()
    }
}
