package io.micronaut.starter.cli.command

import spock.lang.Specification

class JdkVersionCandidatesSpec extends Specification {

    void "ony 17 is valid candidate"() {
        expect:
        ['17'] == new JdkVersionCandidates()
    }
}
