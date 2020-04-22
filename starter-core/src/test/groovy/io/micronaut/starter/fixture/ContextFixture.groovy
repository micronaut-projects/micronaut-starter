package io.micronaut.starter.fixture

import io.micronaut.context.BeanContext
import io.micronaut.inject.qualifiers.Qualifiers
import io.micronaut.starter.io.ConsoleOutput
import io.micronaut.starter.options.Options
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.AvailableFeatures
import io.micronaut.starter.feature.Feature
import io.micronaut.starter.feature.FeatureContext
import io.micronaut.starter.application.ContextFactory
import io.micronaut.starter.feature.Features
import io.micronaut.starter.feature.validation.FeatureValidator
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework

trait ContextFixture {

    abstract BeanContext getBeanContext()

    Features getFeatures(List<String> features,
                         Language language = null,
                         TestFramework testFramework = null,
                         BuildTool buildTool = BuildTool.GRADLE,
                         ApplicationType applicationType = ApplicationType.DEFAULT
    ) {
        Options options = new Options(language, testFramework, buildTool)
        FeatureContext featureContext = buildFeatureContext(features, options, applicationType)
        featureContext.processSelectedFeatures()
        List<Feature> finalFeatures = featureContext.getFinalFeatures(ConsoleOutput.NOOP)
        beanContext.getBean(FeatureValidator).validate(featureContext.getOptions(), finalFeatures)
        return new Features(finalFeatures, options)
    }

    FeatureContext buildFeatureContext(List<String> selectedFeatures,
                                       Options options = new Options(null, null, BuildTool.GRADLE),
                                       ApplicationType applicationType = ApplicationType.DEFAULT) {

        AvailableFeatures availableFeatures = beanContext.getBean(AvailableFeatures, Qualifiers.byName(applicationType.name))
        ContextFactory factory = beanContext.getBean(ContextFactory)

        factory.createFeatureContext(availableFeatures,
                selectedFeatures,
                applicationType,
                options)
    }

    GeneratorContext buildGeneratorContext(List<String> selectedFeatures,
                                           Options options = new Options(null, null, BuildTool.GRADLE)) {
        if (this instanceof ProjectFixture) {
            ContextFactory factory = beanContext.getBean(ContextFactory)
            FeatureContext featureContext = buildFeatureContext(selectedFeatures, options)
            GeneratorContext commandContext = factory.createGeneratorContext(((ProjectFixture) this).buildProject(), featureContext, ConsoleOutput.NOOP)
            commandContext.applyFeatures()
            return commandContext
        } else {
            throw new IllegalStateException("Cannot get generator context without implementing ProjectFixture")
        }
    }

}
