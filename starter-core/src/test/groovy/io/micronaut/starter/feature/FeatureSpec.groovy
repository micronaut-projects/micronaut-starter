package io.micronaut.starter.feature

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.OperatingSystem
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.dependencies.DependencyCoordinate
import io.micronaut.starter.feature.database.JAsyncSQLFeature
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
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
    void "test #feature does not add an unmodifiable map to config"(Feature feature) {
        when:
        JdkVersion javaVersion = javaVersionForFeature(feature.getName())
        Language language = Language.JAVA
        if (feature instanceof LanguageSpecificFeature) {
            language = ((LanguageSpecificFeature) feature).getRequiredLanguage()
        }
        Options options = new Options(language, TestFramework.JUNIT, BuildTool.GRADLE, javaVersion)
        def features = [feature.getName()]
        if (feature instanceof JAsyncSQLFeature) {
            // JAsyncSQLFeatureValidator fails unless exactly one of mysql or postgress are included
            // so it can't be tested in isolation like this in isolation
            features += 'mysql'
        }
        def commandCtx = new GeneratorContext(buildProject(),
                ApplicationType.DEFAULT,
                options,
                OperatingSystem.LINUX,
                getFeatures(features, options).getFeatures(),
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

    private JdkVersion javaVersionForFeature(String feature) {
        feature == 'azure-function' ? JdkVersion.JDK_8 : JdkVersion.JDK_11
    }
}
