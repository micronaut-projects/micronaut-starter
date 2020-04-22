package io.micronaut.starter.feature

import io.micronaut.starter.BeanContextSpec

class FeatureConfigurationSpec extends BeanContextSpec {

    void "context contains multiple FeatureConfiguration"() {
        when:
        Collection<FeatureConfiguration> featureConfigurations = beanContext.getBeansOfType(FeatureConfiguration)

        then:
        !featureConfigurations.isEmpty()
    }
}
