package io.micronaut.starter.fixture

import io.micronaut.context.BeanContext
import io.micronaut.inject.qualifiers.Qualifiers
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.projectgen.core.generator.ContextFactory
import io.micronaut.projectgen.core.options.OperatingSystem
import io.micronaut.projectgen.core.generator.GeneratorContext
import io.micronaut.projectgen.core.buildtools.Source
import io.micronaut.projectgen.core.feature.AvailableFeatures
import io.micronaut.projectgen.core.feature.Feature
import io.micronaut.projectgen.core.feature.FeatureContext
import io.micronaut.projectgen.core.feature.Features
import io.micronaut.projectgen.core.feature.FeatureValidator
import io.micronaut.projectgen.core.io.ConsoleOutput
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.Language
import io.micronaut.projectgen.core.options.Options
import io.micronaut.projectgen.core.options.TestFramework
import io.micronaut.projectgen.micronaut.MicronautOptions

import java.util.function.Consumer

trait ContextFixture {

    abstract BeanContext getBeanContext()

    String getGradleAnnotationProcessorScope(Language language, Source source = Source.MAIN) {
        if (language == Language.JAVA) {
            if (source == Source.MAIN) {
                return "annotationProcessor"
            } else if (source == Source.TEST) {
                return "testAnnotationProcessor"
            }
        } else if (language == Language.KOTLIN) {
            "kapt"
        } else if (language == Language.GROOVY) {
            if (source == Source.MAIN) {
                return "compileOnly"
            } else if (source == Source.TEST) {
                return "testCompileOnly"
            }
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
                                       Options options = MicronautOptions.builder().buildTool(BuildTool.DEFAULT_OPTION).build(),
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
                                           Options options = MicronautOptions.builder().buildTool(BuildTool.DEFAULT_OPTION).build(),
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
                                           Options options = MicronautOptions.builder().buildTool(BuildTool.DEFAULT_OPTION).build(),
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
