package io.micronaut.starter.feature

import io.micronaut.context.BeanContext
import io.micronaut.starter.Options
import io.micronaut.starter.command.CommandContext
import io.micronaut.starter.command.MicronautCommand
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.util.NameUtils
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll


class FeatureSpec extends Specification {

    @Shared @AutoCleanup BeanContext ctx = BeanContext.run()

    @Unroll
    void "test default feature #feature.name cannot require a language"() {
        expect:
        //Default features cannot require a language because its inferred prior to them being applied
        //due to the language being necessary to determine if a feature should be applied by default
        !feature.requiredLanguage.isPresent()

        where:
        feature << ctx.getBeansOfType(DefaultFeature).toList()
    }

    @Unroll
    void "test feature #feature.name is not visible or has a description"() {
        expect:
        !feature.visible || feature.description != null

        where:
        feature << ctx.getBeansOfType(Feature).toList()
    }

    @Unroll
    void "test #feature.name does not add an unmodifiable map to config"() {
        when:
        def commandCtx = new CommandContext(NameUtils.parse("foo"),
                MicronautCommand.CREATE_APP,
                new Options(Language.java, TestFramework.junit, BuildTool.gradle), [feature])
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
        feature << ctx.getBeansOfType(Feature)
    }
}
