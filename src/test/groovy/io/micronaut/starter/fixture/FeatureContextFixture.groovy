package io.micronaut.starter.fixture

import io.micronaut.context.BeanContext
import io.micronaut.starter.command.CreateAppCommand.CreateAppFeatures
import io.micronaut.starter.command.MicronautCommand
import io.micronaut.starter.feature.Feature
import io.micronaut.starter.feature.FeatureContext

trait FeatureContextFixture extends FeatureFixture {

    abstract BeanContext getBeanContext()

    FeatureContext buildAndProcessFeatureContextWithFeatures(List<Feature> features = []) {
        FeatureContext featureContext = new FeatureContext(
            null,
            null,
            null,
            MicronautCommand.CREATE_APP,
            beanContext.getBean(CreateAppFeatures),
            features
        )
        featureContext.processSelectedFeatures()

        featureContext
    }

}
