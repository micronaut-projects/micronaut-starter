package io.micronaut.starter.core.test.feature.other

import io.micronaut.starter.feature.Feature
import io.micronaut.starter.feature.other.SwaggerUI
import io.micronaut.starter.feature.security.Security
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.CommandSpec
import spock.lang.Unroll

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class SwaggerUISpec extends CommandSpec {

    Path applicationYAMLPath
    Path openAPIPropertiesPath
    Path fooControllerPathJava
    Path fooControllerTestPathKotlin
    Path fooControllerTestPathJava
    Path fooControllerPathKotlin

    void setup() {
        fooControllerPathKotlin = Paths.get(dir.getPath(), "/src/main/kotlin/example/micronaut/", "FooController.kt")
        fooControllerPathJava = Paths.get(dir.getPath(), "/src/main/java/example/micronaut/", "FooController.java")
        fooControllerTestPathKotlin = Paths.get(dir.getPath(), "/src/test/kotlin/example/micronaut/", "FooTest.kt")
        fooControllerTestPathJava = Paths.get(dir.getPath(), "/src/test/java/example/micronaut/", "FooTest.java")
        openAPIPropertiesPath = Paths.get(dir.getPath(), "openapi.properties")
        applicationYAMLPath = Paths.get(dir.getPath(), "/src/main/resources/", "application.yml")
    }

    @Override
    String getTempDirectoryPrefix() {
        return "other"
    }

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
                [Language.JAVA, Language.KOTLIN]].combinations()
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
                [Language.JAVA, Language.KOTLIN]].combinations()
    }


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
                [Language.JAVA, Language.KOTLIN],
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
                [Language.JAVA, Language.KOTLIN],
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
        assert Files.exists(fooControllerPathJava)
        assert Files.exists(fooControllerTestPathJava)
    }

    void checkYAMLConfigFileWithoutSecurityFeature() {
        assert Files.exists(applicationYAMLPath)
        assert """micronaut:
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
                .split(System.lineSeparator()).toList() == Files.readAllLines(applicationYAMLPath)
    }

    void checkYAMLConfigFileWithSecurityFeature() {
        assert Files.exists(applicationYAMLPath)
        assert """micronaut:
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
                .split(System.lineSeparator()).toList() == Files.readAllLines(applicationYAMLPath)
    }
}
