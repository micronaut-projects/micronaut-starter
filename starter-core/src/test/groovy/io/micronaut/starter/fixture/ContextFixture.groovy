package io.micronaut.starter.fixture

import io.micronaut.context.BeanContext
import io.micronaut.inject.qualifiers.Qualifiers
import io.micronaut.starter.application.OperatingSystem
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

import java.util.function.Consumer
import java.util.function.Supplier

trait ContextFixture {

    abstract BeanContext getBeanContext()

    String getGradleAnnotationProcessorScope(Language language) {
        if (language == Language.JAVA) {
            "annotationProcessor"
        } else if (language == Language.KOTLIN) {
            "kapt"
        } else if (language == Language.GROOVY) {
            "compileOnly"
        }
    }

    Features getFeatures(List<String> features,
                         Language language = null,
                         TestFramework testFramework = null,
                         BuildTool buildTool = BuildTool.GRADLE,
                         ApplicationType applicationType = ApplicationType.DEFAULT) {
        Options options = new Options(language, testFramework, buildTool)
        return getFeatures(features, options, applicationType)
    }

    Features getFeatures(List<String> features,
                         Options options,
                         ApplicationType applicationType = ApplicationType.DEFAULT) {
        FeatureContext featureContext = buildFeatureContext(features, options, applicationType)
        featureContext.processSelectedFeatures()
        Set<Feature> finalFeatures = featureContext.getFinalFeatures(ConsoleOutput.NOOP)
        beanContext.getBean(FeatureValidator).validatePostProcessing(featureContext.getOptions(), applicationType, finalFeatures)
        return new Features(buildGeneratorContext(features, options, applicationType), finalFeatures, options)
    }

    FeatureContext buildFeatureContext(List<String> selectedFeatures,
                                       Options options = new Options(null, null, BuildTool.GRADLE),
                                       ApplicationType applicationType = ApplicationType.DEFAULT) {

        AvailableFeatures availableFeatures = beanContext.getBean(AvailableFeatures, Qualifiers.byName(applicationType.name))

        ContextFactory factory = beanContext.getBean(ContextFactory)

        factory.createFeatureContext(availableFeatures,
                selectedFeatures,
                applicationType,
                options,
                OperatingSystem.LINUX)
    }

    GeneratorContext buildGeneratorContext(List<String> selectedFeatures,
                                           Options options = new Options(null, null, BuildTool.GRADLE),
                                           ApplicationType applicationType = ApplicationType.DEFAULT) {
        if (this instanceof ProjectFixture) {
            ContextFactory factory = beanContext.getBean(ContextFactory)
            FeatureContext featureContext = buildFeatureContext(selectedFeatures, options, applicationType)
            GeneratorContext generatorContext = factory.createGeneratorContext(((ProjectFixture) this).buildProject(), featureContext, ConsoleOutput.NOOP)
            generatorContext.applyFeatures()
            return generatorContext
        } else {
            throw new IllegalStateException("Cannot get generator context without implementing ProjectFixture")
        }
    }

    GeneratorContext buildGeneratorContext(List<String> selectedFeatures,
                                           Consumer<GeneratorContext> mutate,
                                           Options options = new Options(null, null, BuildTool.GRADLE),
                                           ApplicationType applicationType = ApplicationType.DEFAULT) {
        if (this instanceof ProjectFixture) {
            ContextFactory factory = beanContext.getBean(ContextFactory)
            FeatureContext featureContext = buildFeatureContext(selectedFeatures, options, applicationType)
            GeneratorContext generatorContext = factory.createGeneratorContext(((ProjectFixture) this).buildProject(), featureContext, ConsoleOutput.NOOP)
            mutate.accept(generatorContext)
            generatorContext.applyFeatures()
            return generatorContext
        } else {
            throw new IllegalStateException("Cannot get generator context without implementing ProjectFixture")
        }
    }

}
