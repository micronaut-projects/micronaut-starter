package io.micronaut.starter.feature.testresources

import spock.lang.Specification

class DbTypeSpec extends Specification {

    void "toString() returns lowercase"() {
        expect:
        toString == dbtype.toString()

        where:
        toString    | dbtype
        'postgresql'| DbType.POSTGRESQL
        'mysql'     | DbType.MYSQL
        'mariadb'   | DbType.MARIADB
        'mssql'     | DbType.SQLSERVER
        'oracle'    | DbType.ORACLEXE
    }
}
