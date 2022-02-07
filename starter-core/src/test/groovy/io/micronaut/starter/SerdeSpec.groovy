package io.micronaut.starter

import io.micronaut.serde.annotation.Serdeable
import io.micronaut.starter.analytics.Generated
import io.micronaut.starter.analytics.SelectedFeature
import io.micronaut.starter.build.dependencies.Coordinate
import io.micronaut.starter.build.dependencies.DependencyCoordinate
import io.micronaut.starter.client.github.oauth.AccessToken
import io.micronaut.starter.client.github.v3.GitHubRepository
import io.micronaut.starter.client.github.v3.GitHubSecret
import io.micronaut.starter.client.github.v3.GitHubSecretsPublicKey
import io.micronaut.starter.client.github.v3.GitHubUser
import io.micronaut.starter.client.github.v3.GitHubWorkflowRun
import io.micronaut.starter.client.github.v3.GitHubWorkflowRuns
import io.micronaut.starter.defaults.LanguageDefaults
import io.micronaut.starter.feature.function.DocumentationLink
import jakarta.inject.Inject
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import org.reflections.scanners.TypeAnnotationsScanner
import spock.lang.Specification

import java.lang.reflect.Type

class SerdeSpec extends Specification {


    void "#type is annotated with @Serdeable.Deserializable"(Type type) {
        expect:
        Reflections ref = new Reflections("io.micronaut.starter",
                new SubTypesScanner(),
                new TypeAnnotationsScanner())
        def classes = ref.getTypesAnnotatedWith(Serdeable)

        assert classes.contains(type)

        where:
        type << [Generated, SelectedFeature, Coordinate, DependencyCoordinate, AccessToken, GitHubRepository,
                GitHubSecret, GitHubSecretsPublicKey, GitHubUser, GitHubWorkflowRun, GitHubWorkflowRuns,
                LanguageDefaults, DocumentationLink]

    }
}
