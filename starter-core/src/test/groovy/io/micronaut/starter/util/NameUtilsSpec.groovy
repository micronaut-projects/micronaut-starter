package io.micronaut.starter.util

import io.micronaut.projectgen.core.utils.NameUtils
import spock.lang.Specification

class NameUtilsSpec extends Specification {

    void "test an empty name throws a useful exception"() {
        when:
        NameUtils.parse(null)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == '"null" is not a valid app name'

        when:
        NameUtils.parse("")

        then:
        ex = thrown(IllegalArgumentException)
        ex.message == '"" is not a valid app name'
    }
}
