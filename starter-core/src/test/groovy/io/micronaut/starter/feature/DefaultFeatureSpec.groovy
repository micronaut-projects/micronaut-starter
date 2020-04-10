package io.micronaut.starter.feature

import io.micronaut.context.BeanContext
import spock.lang.Specification


class DefaultFeatureSpec extends Specification {

    void "test default features cannot require a language"() {
        BeanContext ctx = BeanContext.run()
        List<DefaultFeature> features = ctx.getBeansOfType(DefaultFeature).toList()

        expect:
        //Default features cannot require a language because its inferred prior to them being applied
        //due to the language being necessary to determine if a feature should be applied by default
        features.every { !it.getRequiredLanguage().isPresent() }
    }
}
