package io.micronaut.starter.options

import spock.lang.Specification

class JdkVersionSpec extends Specification {

    void 'test valueOf(Integer) with a supported JDK version'() {
        expect:
        JdkVersion.JDK_17 == JdkVersion.valueOf(17)
    }

    void 'test valueOf(String) with a supported JDK version'() {
        expect:
        JdkVersion.JDK_17 == JdkVersion.valueOf("JDK_17")
    }

    void 'test valueOf when the JDK version does not exist'() {
        when:
        JdkVersion.valueOf(3)

        then:
        IllegalArgumentException ex = thrown()
        ex.message.startsWith("Unsupported JDK version: 3. Supported values are ")
    }

    void "greaterThanOrEqual"() {
        expect:
        JdkVersion.JDK_11.greaterThanEqual(JdkVersion.JDK_11)
        JdkVersion.JDK_17.greaterThanEqual(JdkVersion.JDK_11)
        !JdkVersion.JDK_8.greaterThanEqual(JdkVersion.JDK_11)
    }
}
