package io.micronaut.starter.feature

import io.micronaut.context.BeanContext
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
}
