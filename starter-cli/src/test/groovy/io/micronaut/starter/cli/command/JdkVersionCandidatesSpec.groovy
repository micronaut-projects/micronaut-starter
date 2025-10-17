package io.micronaut.starter.cli.command

import spock.lang.Specification

class JdkVersionCandidatesSpec extends Specification {

    void "17, 21 and 25 are valid candidates"() {
        expect:
        ['17', '21', '25'] == new JdkVersionCandidates()
    }
}
