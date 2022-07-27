package io.micronaut.starter.feature.testresources

import spock.lang.Specification

class DbTypeSpec extends Specification {

    void "toString() returns lowercase"() {
        expect:
        "mariadb" == DbType.MARIADB.toString();
    }
}
