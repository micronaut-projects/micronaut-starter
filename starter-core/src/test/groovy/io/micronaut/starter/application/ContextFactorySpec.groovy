package io.micronaut.starter.application

import io.micronaut.context.ApplicationContext
import io.micronaut.context.annotation.Replaces
import io.micronaut.context.annotation.Requires
import io.micronaut.projectgen.core.feature.AvailableFeatures
import io.micronaut.projectgen.core.feature.Feature
import io.micronaut.projectgen.core.feature.FeatureContext
import io.micronaut.projectgen.core.generator.ContextFactory
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.feature.test.KoTest
import io.micronaut.starter.feature.test.Mockk
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.JdkVersion
import io.micronaut.projectgen.core.options.Language
import spock.lang.AutoCleanup
import spock.lang.Issue
import spock.lang.Shared
import spock.lang.Specification
import jakarta.inject.Singleton

class ContextFactorySpec extends Specification {

    @Shared
    @AutoCleanup
    ApplicationContext applicationContext = ApplicationContext.run(['spec.name': 'ContextFactorySpec'])

    @Shared
    AvailableFeatures availableFeatures = applicationContext.getBean(DefaultAvailableFeatures)

    @Shared
    ContextFactory contextFactory = applicationContext.getBean(ContextFactory)

    @Issue("https://github.com/micronaut-projects/micronaut-starter/issues/1473")
    void "avoid NPE by sorting default features"() {
        when:
        FeatureContext featureContext = contextFactory.createFeatureContext(availableFeatures,
                [],
                MicronautOptions.builder().applicationType(ApplicationType.DEFAULT).language(Language.KOTLIN).buildTool(BuildTool.MAVEN).javaVersion(JdkVersion.JDK_17).build()
        )
        then:
        noExceptionThrown()

        when: 'testing features are applied before features that depend on them are present'
        Set<Feature> selectedFeatures = featureContext.getSelectedFeatures()

        then:
        selectedFeatures.stream()
                .anyMatch(f -> f.name == Mockk.NAME_MOCKK)
    }
//
//    @Requires(property = 'spec.name', value = 'ContextFactorySpec')
//    @Replaces(Junit.class)
//    @Singleton
//    static class JunitReplacement extends Junit {
//    }

    @Requires(property = 'spec.name', value = 'ContextFactorySpec')
    @Replaces(KoTest.class)
    @Singleton
    static class KoTestReplacement extends KoTest {
    }
}
