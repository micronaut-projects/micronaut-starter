package io.micronaut.starter.feature.architecture

import io.micronaut.starter.feature.architecture.Architecture
import spock.lang.Specification

class ArchitectureSpec extends Specification {
    void "Architecture::toString() returns name"() {
        expect:
        'ARM' == Architecture.ARM.toString()
    }
}
