package io.micronaut.starter.cli.command

import spock.lang.Specification

class JdkVersionCandidatesSpec extends Specification {

    void "25 are valid candidates"() {
        expect:
        ['25'] == new JdkVersionCandidates()
    }
}
