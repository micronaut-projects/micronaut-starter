package io.micronaut.starter.fixture

import io.micronaut.context.BeanContext
import io.micronaut.starter.command.CreateAppCommand
import io.micronaut.starter.command.MicronautCommand
import io.micronaut.starter.feature.DefaultFeature
import io.micronaut.starter.feature.Feature
import io.micronaut.starter.feature.FeatureContext
import io.micronaut.starter.feature.FeatureContextFactory
import io.micronaut.starter.feature.validation.FeatureValidator
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework

trait FeatureContextFixture extends FeatureFixture {

    abstract BeanContext getBeanContext()

    FeatureContext buildAndProcess(List<String> features,
                                   Language language = Language.java,
                                   TestFramework testFramework = TestFramework.junit,
                                   BuildTool buildTool = BuildTool.gradle) {
        FeatureContext featureContext = build(features, language, testFramework, buildTool)
        featureContext.processSelectedFeatures()
        featureContext
    }

    FeatureContext build(List<String> selectedFeatures,
                         Language language = null,
                         TestFramework testFramework = null,
                         BuildTool buildTool = BuildTool.gradle) {

        CreateAppCommand.CreateAppFeatures availableFeatures = beanContext.getBean(CreateAppCommand.CreateAppFeatures)
        FeatureContextFactory factory = beanContext.getBean(FeatureContextFactory)

        factory.build(availableFeatures, selectedFeatures, MicronautCommand.CREATE_APP, language, buildTool, testFramework)
    }

}
