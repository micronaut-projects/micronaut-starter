package io.micronaut.starter.options

import spock.lang.Specification

class MicronautVersionTest extends Specification {

    void majorIsCorrect(MicronautVersion version, int expectedMajor, String expectedToString) {
        expect:
        version.major == expectedMajor
        version.toString() == expectedToString

        where:
        version                | expectedMajor | expectedToString
        MicronautVersion.ONE   | 1             | '1'
        MicronautVersion.TWO   | 2             | '2'
        MicronautVersion.THREE | 3             | '3'
        MicronautVersion.FOUR  | 4             | '4'
    }

}
