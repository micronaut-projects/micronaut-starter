package io.micronaut.starter.application

import io.micronaut.context.ApplicationContext
import io.micronaut.starter.feature.AvailableFeatures
import io.micronaut.starter.feature.build.gradle.Gradle
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import spock.lang.AutoCleanup
import spock.lang.Issue
import spock.lang.Shared
import spock.lang.Specification

class ContextFactorySpec extends Specification {

    @Shared
    @AutoCleanup
    ApplicationContext applicationContext = ApplicationContext.run()

    @Shared
    AvailableFeatures availableFeatures = applicationContext.getBean(DefaultAvailableFeatures)

    @Shared
    ContextFactory contextFactory = applicationContext.getBean(ContextFactory)

    @Issue("https://github.com/micronaut-projects/micronaut-starter/issues/1473")
    void "avoid NPE by sorting default features"() {
        when:
        contextFactory.createFeatureContext(availableFeatures,
                [],
                ApplicationType.DEFAULT,
                new Options(Language.KOTLIN, null, BuildTool.GRADLE, JdkVersion.JDK_17),
                null
        )
        then:
        noExceptionThrown()
    }
}
