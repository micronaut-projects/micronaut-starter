package io.micronaut.starter.feature.testresources

import spock.lang.Specification

class DbTypeSpec extends Specification {

    void "toString() returns lowercase"() {
        expect:
        toString == dbtype.toString()

        where:
        toString   | dbtype
        'postgres' | DbType.POSTGRESQL
        'mysql'    | DbType.MYSQL
        'mariadb'  | DbType.MARIADB
        'mssql'    | DbType.SQLSERVER
        'oracle'   | DbType.ORACLEFREE
    }

    void "of returns DbType for case-insensitive match"() {
        expect:
        DbType.of(input) == expected

        where:
        input        | expected
        'postgres'   | DbType.POSTGRESQL
        'POSTGRES'   | DbType.POSTGRESQL
        'Postgres'   | DbType.POSTGRESQL
        'mysql'      | DbType.MYSQL
        'MYSQL'      | DbType.MYSQL
        'mariadb'    | DbType.MARIADB
        'mssql'      | DbType.SQLSERVER
        'oracle'     | DbType.ORACLEFREE
        'ORACLE'     | DbType.ORACLEFREE
        'oracle-xe'  | DbType.ORACLEXE
        'ORACLE-XE'  | DbType.ORACLEXE
    }

    void "of returns null for null/empty inputs"() {
        expect:
        DbType.of(input) == null

        where:
        input << [null, '', '   ', '\t', '\n']
    }

    void "of returns null for unknown values"() {
        expect:
        DbType.of(input) == null

        where:
        input << ['h2', 'oracle-free', 'postgresql', 'sqlserver', 'maria', 'not-a-db']
    }
}
