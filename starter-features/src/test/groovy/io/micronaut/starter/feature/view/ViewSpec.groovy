package io.micronaut.starter.feature.view

import io.micronaut.starter.BeanContextSpec

class ViewSpec extends BeanContextSpec {

    void 'test there can only be one view feature'() {
        when:
        getFeatures(["views-thymeleaf", "views-handlebars", "views-velocity", "views-freemarker", "views-rocker", "views-soy"])

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message.contains("There can only be one of the following features selected")
    }

}
