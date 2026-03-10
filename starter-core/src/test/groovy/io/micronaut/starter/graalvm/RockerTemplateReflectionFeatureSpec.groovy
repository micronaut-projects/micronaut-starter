package io.micronaut.starter.graalvm

import spock.lang.Specification

class RockerTemplateReflectionFeatureSpec extends Specification {

    void 'rocker template classes exist on test classpath'() {
        when:
        Class<?> clazz = Class.forName('io.micronaut.starter.rocker.feature.agorapulse.console.template.consoleGroovyDsl')

        then:
        clazz != null

        when:
        Class<?> inner = Class.forName('io.micronaut.starter.rocker.feature.agorapulse.console.template.consoleGroovyDsl$Template')

        then:
        inner != null
    }
}
