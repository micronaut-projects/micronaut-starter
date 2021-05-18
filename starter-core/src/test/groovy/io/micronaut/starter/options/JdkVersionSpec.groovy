package io.micronaut.starter.options

import spock.lang.Specification

class JdkVersionSpec extends Specification {

    void 'test valueOf with a supported JDK version'() {
        expect:
        JdkVersion.JDK_16 == JdkVersion.valueOf(16)
    }

    void 'test valueOf when the JDK version does not exist'() {
        when:
        JdkVersion.valueOf(3)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "Unsupported JDK version: 3. Supported values are [8, 11, 16]"
    }
}
