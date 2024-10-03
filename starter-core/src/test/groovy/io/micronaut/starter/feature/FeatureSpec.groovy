package io.micronaut.starter.feature

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.OperatingSystem
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.dependencies.DependencyCoordinate
import io.micronaut.starter.feature.aws.AwsLambdaEventFunctionFeature
import io.micronaut.starter.feature.aws.Cdk
import io.micronaut.starter.feature.database.HibernateReactiveFeature
import io.micronaut.starter.feature.database.JAsyncSQLFeature
import io.micronaut.starter.feature.function.awslambda.AwsLambda
import io.micronaut.starter.feature.json.JsonSchemaFeature
import io.micronaut.starter.feature.json.JsonSchemaValidationFeature
import io.micronaut.starter.feature.lang.groovy.module.GroovyModuleFeature
import io.micronaut.starter.options.*
import spock.lang.Unroll

import java.util.stream.Collectors

class FeatureSpec extends BeanContextSpec {

    @Unroll
    void "test default feature #feature.name cannot require a language"() {
        expect:
        //Default features cannot require a language because its inferred prior to them being applied
        //due to the language being necessary to determine if a feature should be applied by default
        !(feature instanceof LanguageSpecificFeature)

        where:
        feature << beanContext.getBeansOfType(DefaultFeature).toList()
    }

    @Unroll
    void "test feature #feature.name is not visible or has a description and title"() {
        expect:
        !feature.visible || (feature.description != null && feature.title != null)

        where:
        feature << beanContext.getBeansOfType(Feature).toList()
    }

    @Unroll
    void "test #feature.name does not add an unmodifiable map to config"(Feature feature) {
        when:
        JdkVersion javaVersion = javaVersionForFeature(feature.name)
        Language language = Language.JAVA
        if (feature instanceof LanguageSpecificFeature) {
            language = ((LanguageSpecificFeature) feature).getRequiredLanguage()
        }
        if (feature instanceof GroovyModuleFeature) {
            // can't make GroovyModuleFeature a LanguageSpecificFeature,
            // because it's valid when using Spock framework too
            language = Language.GROOVY
        }
        def buildTool = BuildTool.GRADLE
        if (feature instanceof MavenSpecificFeature) {
            buildTool = BuildTool.MAVEN
        }
        Options options = new Options(language, TestFramework.JUNIT, buildTool, javaVersion)
        List<String> features = [feature.getName()]

        if (feature instanceof JAsyncSQLFeature) {
            // JAsyncSQLFeatureValidator fails unless exactly one of mysql or postgress are included
            // so it can't be tested in isolation like this in isolation
            features << 'mysql'
        } else if (feature instanceof HibernateReactiveFeature) {
            // Validators for these fail unless a supported database feature is also added
            features << 'mysql'
        } else if (feature instanceof Cdk || feature instanceof AwsLambdaEventFunctionFeature) {
            // Cdk fails unless it is combined with Lambda
            features << AwsLambda.FEATURE_NAME_AWS_LAMBDA
        } else if (feature instanceof JsonSchemaValidationFeature) {
            // fails unless it is combined with JsonSchemaFeature
            features << JsonSchemaFeature.NAME
        }
        ApplicationType applicationType = applicationTypeForFeature(feature)
        def commandCtx = new GeneratorContext(buildProject(),
                applicationType,
                options,
                OperatingSystem.LINUX,
                getFeatures(features, options, applicationType).getFeatures(),
                (String artifactId) -> Optional.of(new DependencyCoordinate("io.test", artifactId, null, 0, false))
        )
        commandCtx.applyFeatures()

        then:
        commandCtx.configuration.values().stream()
            .filter(val -> val instanceof Map)
            .map(val -> (Map) val)
            .noneMatch((Map m) -> {
                try {
                    m.put("hello", "world")
                    return false
                } catch (UnsupportedOperationException e) {
                    return true
                }
            })

        where:
        feature << beanContext.getBeansOfType(Feature).stream()
            .filter(f -> f.isVisible())
            .collect(Collectors.toList())
    }

    private static JdkVersion javaVersionForFeature(String feature) {
        // Azure functions support 21 as a preview for functions version 4.x in Linux. Java 21 is not supported in Windows yet
        // https://learn.microsoft.com/en-us/azure/azure-functions/functions-reference-java?tabs=bash%2Cconsumption#supported-versions
        return feature in ["azure-function",
                           "azure-function-http",
                           "chatbots-basecamp-azure-function",
                           "chatbots-telegram-azure-function"
        ] ? JdkVersion.JDK_17 : MicronautJdkVersionConfiguration.DEFAULT_OPTION
    }

    private static ApplicationType applicationTypeForFeature(Feature feature) {
        feature.supports(ApplicationType.FUNCTION) ? ApplicationType.FUNCTION : ApplicationType.DEFAULT
    }
}
