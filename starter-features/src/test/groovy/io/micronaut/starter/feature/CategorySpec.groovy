package io.micronaut.starter.feature

import io.micronaut.starter.BeanContextSpec
import spock.lang.Unroll

class CategorySpec extends BeanContextSpec {

    @Unroll
    void "#description is categorized"(Feature feature, String description) {
        expect:
        feature.category != Category.OTHER

        where:
        feature << beanContext.getBeansOfType(Feature).findAll { it.isVisible() }
        description = feature.name
    }

}
