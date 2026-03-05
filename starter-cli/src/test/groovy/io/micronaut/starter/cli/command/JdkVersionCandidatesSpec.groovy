package io.micronaut.starter.cli.command

import spock.lang.Specification

class JdkVersionCandidatesSpec extends Specification {

    void "25 is a valid candidate"() {
        expect:
        ['25'] == new JdkVersionCandidates()
    }
}
