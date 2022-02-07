package io.micronaut.starter.api

import io.micronaut.serde.SerdeIntrospections
import io.micronaut.starter.api.create.github.GitHubCreateDTO
import io.micronaut.starter.api.options.ApplicationTypeSelectOptions
import io.micronaut.starter.api.options.BuildToolSelectOptions
import io.micronaut.starter.api.options.JdkVersionSelectOptions
import io.micronaut.starter.api.options.LanguageSelectOptions
import io.micronaut.starter.api.options.TestFrameworkSelectOptions
import io.micronaut.starter.api.preview.PreviewDTO
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import io.micronaut.core.type.Argument
import spock.lang.Specification

import java.lang.reflect.Type

@MicronautTest(startApplication = false)
class SerdeSpec extends Specification {

    @Inject
    SerdeIntrospections serdeIntrospections

    void "#type is annotated with @Serdeable.Deserializable"(Type type) {
        when:
        serdeIntrospections.getDeserializableIntrospection(Argument.of(type))

        then:
        noExceptionThrown()

        where:
        type << [JdkVersionDTO, ApplicationTypeDTO, ApplicationTypeList, BuildToolDTO, FeatureDTO,
                FeatureList, LanguageDTO, LinkDTO, Linkable, SelectOptionDTO, SelectOptionsDTO,
                TestFrameworkDTO, VersionDTO, GitHubCreateDTO, ApplicationTypeSelectOptions,
                BuildToolSelectOptions, JdkVersionSelectOptions, LanguageSelectOptions,
                TestFrameworkSelectOptions, PreviewDTO]
    }
}
