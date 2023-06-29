package io.micronaut.starter.springboot

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework

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
        !verifier.hasBuildPlugin("io.micronaut.library")
        !verifier.hasBuildPlugin("com.github.johnrengelman.shadow")
        verifier.hasDependency("org.springframework.boot", "spring-boot-starter", Scope.COMPILE)
        verifier.hasDependency("org.springframework.boot", "spring-boot-starter-test", Scope.TEST)
        template.contains("mavenCentral()")
        template.contains("useJUnitPlatform()")

        where:
        [language, buildTool] << [[Language.JAVA], BuildTool.valuesGradle()].combinations()
    }

    void 'no Micronaut files get generated with springBoot feature and maven'() {
        given:
        Options options = new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.MAVEN).withFramework(SpringBootFramework.FRAMEWORK_SPRING_BOOT)

        when:
        Map<String, String> output = generate(ApplicationType.DEFAULT, options, [SpringBootStarter.NAME])

        then:
        !output['micronaut-cli.yml']
        output['pom.xml']

        when:
        String pom = output['pom.xml']

        then:
        !pom.contains("micronaut")
        pom.contains("spring-boot-maven-plugin")
        pom.contains("spring-boot-starter-parent")
        pom.contains("<relativePath/>")
        pom.contains("<relativePath/>")
        pom.contains("spring-boot-starter-test")
        pom.contains("<java.version>17</java.version>")
    }

    void 'no Micronaut files get generated with springBoot feature'() {
        given:
        Options options = new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.GRADLE).withFramework(SpringBootFramework.FRAMEWORK_SPRING_BOOT)

        when:
        Map<String, String> output = generate(ApplicationType.DEFAULT, options, [SpringBootStarter.NAME])

        then:
        output
        output['build.gradle']
        !output['gradle.properties']
        output['src/test/java/example/micronaut/ApplicationTest.java']
        output['src/test/java/example/micronaut/ApplicationTest.java'] == '''\
package example.micronaut;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTest {
    @Test
    void contextLoads() {
    }
}
'''
        output['src/main/java/example/micronaut/Application.java']
        output['src/main/java/example/micronaut/Application.java'] == '''\
package example.micronaut;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
'''
    }
}