package io.micronaut.starter.core.test.feature.other

import io.micronaut.starter.feature.Feature
import io.micronaut.starter.feature.other.SwaggerUI
import io.micronaut.starter.feature.security.Security
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.BuildToolTest
import io.micronaut.starter.test.CommandSpec
import spock.lang.IgnoreIf
import spock.lang.Unroll

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class SwaggerUISpec extends CommandSpec {

    Path applicationYAMLPath
    Path openAPIPropertiesPath
    Path fooControllerPathJava
    Path fooControllerTestPathJava
    Path fooControllerPathKotlin
    Path fooControllerTestPathKotlin
    Path fooControllerPathGroovy
    Path fooControllerTestPathGroovy

    void setup() {
        fooControllerPathJava = Paths.get(dir.getPath(), "/src/main/java/example/micronaut/", "FooController.java")
        fooControllerTestPathJava = Paths.get(dir.getPath(), "/src/test/java/example/micronaut/", "FooTest.java")

        fooControllerPathKotlin = Paths.get(dir.getPath(), "/src/main/kotlin/example/micronaut/", "FooController.kt")
        fooControllerTestPathKotlin = Paths.get(dir.getPath(), "/src/test/kotlin/example/micronaut/", "FooTest.kt")

        fooControllerPathGroovy = Paths.get(dir.getPath(), "/src/main/groovy/example/micronaut/", "FooController.groovy")
        fooControllerTestPathGroovy = Paths.get(dir.getPath(), "/src/test/groovy/example/micronaut/", "FooSpec.groovy")

        openAPIPropertiesPath = Paths.get(dir.getPath(), "openapi.properties")
        applicationYAMLPath = Paths.get(dir.getPath(), "/src/main/resources/", "application.yml")
    }

    @Override
    String getTempDirectoryPrefix() {
        return "other"
    }

    @IgnoreIf({ BuildToolTest.IGNORE_MAVEN })
    @Unroll
    void "test maven #feature.name with #language without security"(Feature feature, Language language) {
        when:
        generateProject(language, BuildTool.MAVEN, [feature.getName()])
        String output = executeMaven("compile")

        then:
        output?.contains("BUILD SUCCESS")

        checkOpenApiPropertiesFile()

        checkControllerFile(language)

        checkYAMLConfigFileWithoutSecurityFeature()

        where:
        [feature, language] << [
                beanContext.getBeansOfType(SwaggerUI),
                Language.values()].combinations()
    }

    @Unroll
    void "test gradle #feature.name with #language without security"(Feature feature, Language language) {
        when:
        generateProject(language, BuildTool.GRADLE, [feature.getName()])
        String output = executeGradle("compileJava")?.output

        then:
        output?.contains("BUILD SUCCESS")

        checkOpenApiPropertiesFile()

        checkYAMLConfigFileWithoutSecurityFeature()

        checkControllerFile(language)

        where:
        [feature, language] << [
                beanContext.getBeansOfType(SwaggerUI),
                Language.values()].combinations()
    }

    @IgnoreIf({ BuildToolTest.IGNORE_MAVEN })
    @Unroll
    void "test maven #feature.name with #language with security"(Feature feature, Language language, Feature securityFeature) {
        when:
        generateProject(language, BuildTool.MAVEN, [feature.getName(), securityFeature.getName()])
        String output = executeMaven("compile")

        then:
        output?.contains("BUILD SUCCESS")

        checkOpenApiPropertiesFile()

        checkYAMLConfigFileWithSecurityFeature()

        checkControllerFile(language)

        where:
        [feature, language, securityFeature] << [
                beanContext.getBeansOfType(SwaggerUI),
                [Language.JAVA, Language.KOTLIN, Language.GROOVY],
                beanContext.getBeansOfType(Security)
        ].combinations()
    }

    @Unroll
    void "test gradle #feature.name with #language with security"(Feature feature, Language language, Feature securityFeature) {
        when:
        generateProject(language, BuildTool.GRADLE, [feature.getName(), securityFeature.getName()])
        String output = executeGradle("compileJava")?.output

        then:
        output?.contains("BUILD SUCCESS")

        checkOpenApiPropertiesFile()

        checkYAMLConfigFileWithSecurityFeature()

        checkControllerFile(language)

        where:
        [feature, language, securityFeature] << [
                beanContext.getBeansOfType(SwaggerUI),
                [Language.JAVA, Language.KOTLIN, Language.GROOVY],
                beanContext.getBeansOfType(Security)
        ].combinations()
    }

    void checkOpenApiPropertiesFile() {
        assert Files.exists(openAPIPropertiesPath)

        InputStream openAPIPropertiesInputStream = Files.newInputStream(openAPIPropertiesPath)
        Properties openAPIProperties = new Properties();
        openAPIProperties.load(openAPIPropertiesInputStream);

        assert openAPIProperties.get("swagger-ui.enabled") == "true"
        assert openAPIProperties.get("redoc.enabled") == "false"
        assert openAPIProperties.get("rapidoc.enabled") == "false"
        assert openAPIProperties.get("rapidoc.bg-color") == "#14191f"
        assert openAPIProperties.get("rapidoc.text-color") == "#aec2e0"
        assert openAPIProperties.get("rapidoc.sort-endpoints-by") == "method"

        openAPIPropertiesInputStream.close()

    }

    void checkControllerFile(Language language) {
        if (language == Language.KOTLIN) {
            assert Files.exists(fooControllerPathKotlin)
            assert Files.exists(fooControllerTestPathKotlin)
            return
        }
        if (language == Language.GROOVY) {
            assert Files.exists(fooControllerPathGroovy)
            assert Files.exists(fooControllerTestPathGroovy)
            return
        }
        assert Files.exists(fooControllerPathJava)
        assert Files.exists(fooControllerTestPathJava)
    }

    void checkYAMLConfigFileWithoutSecurityFeature() {
        assert Files.exists(applicationYAMLPath)
        assert Collections.indexOfSubList(Files.readAllLines(applicationYAMLPath),
                """micronaut:
                  |  application:
                  |    name: foo
                  |  router:
                  |    static-resources:
                  |      swagger:
                  |        paths: classpath:META-INF/swagger
                  |        mapping: /swagger/**
                  |      swagger-ui:
                  |        paths: classpath:META-INF/swagger/views/swagger-ui
                  |        mapping: /swagger-ui/**""".stripMargin()
                .split(System.lineSeparator()).toList()) == 0
    }

    void checkYAMLConfigFileWithSecurityFeature() {
        assert Files.exists(applicationYAMLPath)
        assert Collections.indexOfSubList(Files.readAllLines(applicationYAMLPath),
                """micronaut:
                  |  application:
                  |    name: foo
                  |  router:
                  |    static-resources:
                  |      swagger:
                  |        paths: classpath:META-INF/swagger
                  |        mapping: /swagger/**
                  |      swagger-ui:
                  |        paths: classpath:META-INF/swagger/views/swagger-ui
                  |        mapping: /swagger-ui/**
                  |  security:
                  |    intercept-url-map:
                  |    - access: isAnonymous()
                  |      pattern: /swagger/**
                  |    - access: isAnonymous()
                  |      pattern: /swagger-ui/**""".stripMargin()
                .split(System.lineSeparator()).toList()) == 0
    }
}
