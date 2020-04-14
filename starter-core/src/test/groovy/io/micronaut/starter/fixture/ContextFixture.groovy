package io.micronaut.starter.fixture

import io.micronaut.context.BeanContext
import io.micronaut.starter.Options
import io.micronaut.starter.command.CommandContext
import io.micronaut.starter.command.ConsoleOutput
import io.micronaut.starter.command.CreateAppCommand
import io.micronaut.starter.command.MicronautCommand
import io.micronaut.starter.feature.Feature
import io.micronaut.starter.feature.FeatureContext
import io.micronaut.starter.ContextFactory
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
                         BuildTool buildTool = BuildTool.gradle) {
        FeatureContext featureContext = buildFeatureContext(features, new Options(language, testFramework, buildTool))
        featureContext.processSelectedFeatures()
        List<Feature> finalFeatures = featureContext.getFinalFeatures(ConsoleOutput.NOOP)
        beanContext.getBean(FeatureValidator).validate(featureContext.getOptions(), finalFeatures)
        return new Features(finalFeatures)
    }

    FeatureContext buildFeatureContext(List<String> selectedFeatures,
                                       Options options = new Options(null, null, BuildTool.gradle)) {

        CreateAppCommand.CreateAppFeatures availableFeatures = beanContext.getBean(CreateAppCommand.CreateAppFeatures)
        ContextFactory factory = beanContext.getBean(ContextFactory)

        factory.createFeatureContext(availableFeatures,
                selectedFeatures,
                MicronautCommand.CREATE_APP,
                options.language, options.buildTool, options.testFramework)
    }

    CommandContext buildCommandContext(List<String> selectedFeatures,
                                       Options options = new Options(null, null, BuildTool.gradle)) {
        if (this instanceof ProjectFixture) {
            ContextFactory factory = beanContext.getBean(ContextFactory)
            FeatureContext featureContext = buildFeatureContext(selectedFeatures, options)
            CommandContext commandContext = factory.createCommandContext(((ProjectFixture) this).buildProject(), featureContext, ConsoleOutput.NOOP)
            commandContext.applyFeatures()
            return commandContext
        } else {
            throw new IllegalStateException("Cannot get command context without implementing ProjectFixture")
        }
    }

}
