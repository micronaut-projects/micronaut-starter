package io.micronaut.starter.cli.command

import spock.lang.Specification

class JdkVersionCandidatesSpec extends Specification {

    void "17 and 21 are valid candidates"() {
        expect:
        ['17', '21'] == new JdkVersionCandidates()
    }
}
