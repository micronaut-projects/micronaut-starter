package io.micronaut.starter.api.create.github

import io.micronaut.context.annotation.Property
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.Project
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.application.generator.ProjectGenerator
import io.micronaut.starter.io.ConsoleOutput
import io.micronaut.starter.options.Options
import io.micronaut.starter.util.NameUtils
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification

import jakarta.inject.Inject
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@Property(name = "micronaut.http.services.github-oauth.url", value =  "https://github.com")
@Property(name = "micronaut.http.services.github-api-v3.url", value =  "https://api.github.com")
@MicronautTest
class GitHubCreateServiceTest extends Specification{

    @Inject GitHubCreateService gitHubCreateService
    @Inject ProjectGenerator projectGenerator

    void 'test generate app to local path'(){
        given:
        Project project = NameUtils.parse('io.micronaut.foo')
        GeneratorContext context = projectGenerator.createGeneratorContext(
                ApplicationType.DEFAULT,
                project,
                new Options(),
                null,
                [],
                ConsoleOutput.NOOP
        )
        Path path = Files.createTempDirectory("test-generate")

        when:
        gitHubCreateService.generateAppLocally(context, path)

        then:
        Files.exists(Paths.get(path.toString(), "build.gradle.kts"))

        cleanup:
        path.toFile().deleteDir()
    }
}
