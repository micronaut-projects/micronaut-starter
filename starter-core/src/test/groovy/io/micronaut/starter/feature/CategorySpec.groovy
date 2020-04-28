package io.micronaut.starter.feature

import io.micronaut.starter.BeanContextSpec

class CategorySpec extends BeanContextSpec {

    void "all visible features are categorized"() {
        given:
        List<Feature> uncategorizedFeatures = beanContext.getBeansOfType(Feature)
                .findAll { it.isVisible() && it.category == Category.OTHER }

        expect:
        uncategorizedFeatures.empty
    }

}
