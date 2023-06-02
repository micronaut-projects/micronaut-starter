package io.micronaut.starter.feature

import io.micronaut.context.ApplicationContext
import io.micronaut.context.annotation.Replaces
import io.micronaut.context.annotation.Requires
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.ContextFactory
import io.micronaut.starter.application.DefaultAvailableFeatures
import io.micronaut.starter.feature.AvailableFeatures
import io.micronaut.starter.feature.Feature
import io.micronaut.starter.feature.FeatureContext
import io.micronaut.starter.feature.test.Junit
import io.micronaut.starter.feature.test.KoTest
import io.micronaut.starter.feature.test.Mockk
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
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
                ApplicationType.DEFAULT,
                new Options(Language.KOTLIN, null, BuildTool.MAVEN, JdkVersion.JDK_17),
                null
        )
        then:
        noExceptionThrown()

        when: 'testing features are applied before features that depend on them are present'
        Set<Feature> selectedFeatures = featureContext.getSelectedFeatures()

        then:
        selectedFeatures.stream()
                .anyMatch(f -> f.name == Mockk.NAME_MOCKK)
    }

    @Requires(property = 'spec.name', value = 'ContextFactorySpec')
    @Replaces(Junit.class)
    @Singleton
    static class JunitReplacement extends Junit {

        @Override
        boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
            false
        }
    }

    @Requires(property = 'spec.name', value = 'ContextFactorySpec')
    @Replaces(KoTest.class)
    @Singleton
    static class KoTestReplacement extends KoTest {
        @Override
        boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
            true
        }
    }
}
