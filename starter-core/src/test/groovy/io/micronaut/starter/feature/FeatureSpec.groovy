package io.micronaut.starter.feature

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.options.Options
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import spock.lang.Unroll

class FeatureSpec extends BeanContextSpec {

    @Unroll
    void "test default feature #feature.name cannot require a language"() {
        expect:
        //Default features cannot require a language because its inferred prior to them being applied
        //due to the language being necessary to determine if a feature should be applied by default
        !feature.requiredLanguage.isPresent()

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
    void "test #feature.name does not add an unmodifiable map to config"() {
        when:
        def commandCtx = new GeneratorContext(buildProject(),
                                              ApplicationType.DEFAULT,
                                              new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.GRADLE),
                                              [feature] as Set
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
        feature << beanContext.getBeansOfType(Feature)
    }
}
