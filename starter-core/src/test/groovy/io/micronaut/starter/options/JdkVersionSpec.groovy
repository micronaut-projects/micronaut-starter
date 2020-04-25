package io.micronaut.starter.options

import spock.lang.Specification

class JdkVersionSpec extends Specification {

    void "test valueOf error"() {
        expect:
        JdkVersion.JDK_11 == JdkVersion.valueOf(11)

        when:
        JdkVersion.valueOf(15)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "Unsupported JDK version: 15. Supported values are [8, 9, 10, 11, 12, 13, 14]"
    }
}
