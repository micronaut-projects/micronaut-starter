package io.micronaut.starter.options

import spock.lang.Specification

class JdkVersionSpec extends Specification {

    void 'test valueOf with a supported JDK version'() {
        expect:
        JdkVersion.JDK_14 == JdkVersion.valueOf(14)
    }

    void 'test valueOf when the JDK version does not exist'() {
        when:
        JdkVersion.valueOf(16)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "Unsupported JDK version: 16. Supported values are [8, 9, 10, 11, 12, 13, 14, 15]"
    }
}
