package io.micronaut.starter.build

import spock.lang.Specification

class CentralSnapshotRepositorySpec extends Specification {

    void "central snapshot repository"() {
        given:
        CentralSnapshotRepository repository = new CentralSnapshotRepository()

        expect:
        'https://central.sonatype.com/repository/maven-snapshots/' == repository.url
        repository.snapshot
    }
}
