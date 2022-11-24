package io.micronaut.starter.cli.command

import spock.lang.Specification

class YesOrNoSpec extends Specification {
    void "YesOrNo::toString() returns name"() {
        expect:
        'YES' == YesOrNo.YES.toString()
    }
}

