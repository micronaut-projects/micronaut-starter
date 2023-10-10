package io.micronaut.starter.core.test.feature.other

import io.micronaut.starter.feature.Feature
import io.micronaut.starter.feature.config.Yaml
import io.micronaut.starter.feature.other.SwaggerUI
import io.micronaut.starter.feature.security.Security
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.BuildToolTest
import io.micronaut.starter.test.CommandSpec
import spock.lang.IgnoreIf

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
    void "test maven #feature with #language without security"(String feature, Language language) {
        when:
        generateProject(language, BuildTool.MAVEN, [Yaml.NAME, 'kapt', feature])
        String output = executeMaven("compile")

        then:
        output?.contains("BUILD SUCCESS")

        checkOpenApiPropertiesFile()

        checkControllerFile(language)

        checkYAMLConfigFileWithoutSecurityFeature()

        where:
        [feature, language] << [
                [SwaggerUI.NAME],
                Language.values()].combinations()
    }

    void "test gradle #feature with #language without security"(String feature, Language language) {
        when:
        generateProject(language, BuildTool.GRADLE, [Yaml.NAME, 'kapt', feature])
        String output = executeGradle("compileJava")?.output

        then:
        output?.contains("BUILD SUCCESS")

        checkOpenApiPropertiesFile()

        checkYAMLConfigFileWithoutSecurityFeature()

        checkControllerFile(language)

        where:
        [feature, language] << [
                [SwaggerUI.NAME],
                Language.values()].combinations()
    }

    @IgnoreIf({ BuildToolTest.IGNORE_MAVEN })
    void "test maven #feature with #language with security"(String feature, Language language, String securityFeature) {
        when:
        generateProject(language, BuildTool.MAVEN, [Yaml.NAME, 'kapt', feature, securityFeature])
        String output = executeMaven("compile")

        then:
        output?.contains("BUILD SUCCESS")

        checkOpenApiPropertiesFile()

        checkYAMLConfigFileWithSecurityFeature()

        checkControllerFile(language)

        where:
        [feature, language, securityFeature] << [
                [SwaggerUI.NAME],
                Language.values(),
                [Security.NAME]
        ].combinations()
    }
    
    void "test gradle #feature with #language with security"(String feature, Language language, String securityFeature) {
        when:
        generateProject(language, BuildTool.GRADLE, [Yaml.NAME, 'kapt', feature, securityFeature])
        String output = executeGradle("compileJava")?.output

        then:
        output?.contains("BUILD SUCCESS")

        checkOpenApiPropertiesFile()

        checkYAMLConfigFileWithSecurityFeature()

        checkControllerFile(language)

        where:
        [feature, language, securityFeature] << [
                [SwaggerUI.NAME],
                Language.values(),
                [Security.NAME]
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
