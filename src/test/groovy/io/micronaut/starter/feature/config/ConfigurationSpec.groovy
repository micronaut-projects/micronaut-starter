package io.micronaut.starter.feature.config

import io.micronaut.context.BeanContext
import io.micronaut.starter.command.CommandContext
import io.micronaut.starter.command.CommandSpec
import io.micronaut.starter.feature.Feature
import io.micronaut.starter.feature.FeatureContext
import io.micronaut.starter.feature.micrometer.MicrometerFeature
import io.micronaut.starter.fixture.CommandFixture
import io.micronaut.starter.fixture.FeatureContextFixture
import io.micronaut.starter.fixture.ProjectFixture
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Unroll

class ConfigurationSpec extends CommandSpec implements CommandFixture, ProjectFixture, FeatureContextFixture {

    @AutoCleanup
    @Shared
    BeanContext beanContext = BeanContext.run()

    @Unroll
    void 'test micrometer configuration for feature=#micrometerFeature.name'() {
        given:
        FeatureContext featureContext = buildAndProcess([micrometerFeature])
        CommandContext commandContext = new CommandContext(featureContext, buildProject())

        when: 'applying all the features'
        for (Feature feature : featureContext.features) {
            feature.apply(commandContext)
        }

        then: 'the micrometer configuration is enabled for the feature'
        commandContext.configuration.get("micronaut.metrics.export.${configKey}.enabled".toString()) == true
        commandContext.configuration.get('micronaut.metrics.enabled') == true

        where:
        micrometerFeature << beanContext.getBeansOfType(MicrometerFeature)*.name.iterator()
        configKey = "${micrometerFeature - 'micrometer-'}".replace('-', '')
    }

}
