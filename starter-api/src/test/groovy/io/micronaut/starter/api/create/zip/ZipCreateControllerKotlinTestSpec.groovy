package io.micronaut.starter.api.create.zip

import io.micronaut.context.annotation.Primary
import io.micronaut.context.annotation.Requires
import io.micronaut.context.event.ApplicationEventPublisher
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable
import io.micronaut.http.HttpRequest
import io.micronaut.starter.api.EmbeddedServerSpecification
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.OperatingSystem
import io.micronaut.starter.application.Project
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.application.generator.ProjectGenerator
import io.micronaut.starter.feature.Feature
import io.micronaut.starter.io.ConsoleOutput
import io.micronaut.starter.io.OutputHandler
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework

import jakarta.inject.Singleton
import jakarta.validation.constraints.NotNull

class ZipCreateControllerKotlinTestSpec extends EmbeddedServerSpecification {

    @Override
    String getSpecName() {
        'CreateControllerKotlinTestSpec'
    }

    void "#path with test framework #testFramework is requested #expected is used"(String testFramework,
                                                                                   String path,
                                                                                   TestFramework expected) {
        when:
        client.exchange(HttpRequest.GET(path + "?test=${testFramework}".toString()))

        then:
        noExceptionThrown()

        when:
        MockProjectGenerator generator = applicationContext.getBean(MockProjectGenerator)

        then:
        generator.optionsList
        expected == generator.optionsList.first().testFramework

        cleanup:
        generator.optionsList = []

        where:
        testFramework | path                   || expected
        'junit'       | '/create/function/foo' || TestFramework.JUNIT
        'spock'       | '/create/function/foo' || TestFramework.SPOCK
        'kotest'      | '/create/function/foo' || TestFramework.KOTEST
        'kotlintest'  | '/create/function/foo' || TestFramework.KOTEST
        'junit'       | '/foo.zip'             || TestFramework.JUNIT
        'spock'       | '/foo.zip'             || TestFramework.SPOCK
        'kotest'      | '/foo.zip'             || TestFramework.KOTEST
        'kotlintest'  | '/foo.zip'             || TestFramework.KOTEST
    }

    @Requires(property = 'spec.name', value = 'CreateControllerKotlinTestSpec')
    @Primary
    @Singleton
    static class MockProjectGenerator implements ProjectGenerator {
        List<Options> optionsList = []

        @Override
        void generate(ApplicationType applicationType,
                      Project project,
                      Options options,
                      @Nullable OperatingSystem operatingSystem,
                      List<String> selectedFeatures,
                      OutputHandler outputHandler,
                      ConsoleOutput consoleOutput) throws Exception {
            optionsList.add(options)
        }

        @Override
        void generate(ApplicationType applicationType,
                      Project project,
                      OutputHandler outputHandler,
                      GeneratorContext generatorContext) throws Exception {
        }

        @Override
        GeneratorContext createGeneratorContext(ApplicationType applicationType,
                                                Project project,
                                                Options options,
                                                @Nullable OperatingSystem operatingSystem,
                                                List<String> selectedFeatures,
                                                ConsoleOutput consoleOutput) {
            optionsList.add(options)
            new GeneratorContext(project,
                                 applicationType,
                                 options,
                                 operatingSystem,
                                 [] as Set<Feature>,
                    () -> Optional.empty())
        }
    }

    @Requires(property = 'spec.name', value = 'CreateControllerKotlinTestSpec')
    @Primary
    @Singleton
    static class MockApplicationEventPublisher implements ApplicationEventPublisher {
        @Override
        void publishEvent(@NotNull @NonNull Object event) {
        }
    }
}
