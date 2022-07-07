package io.micronaut.starter.feature.database

import spock.lang.Specification

class Hbm2ddlAutoSpec extends Specification {

    void "Hbm2ddlAuto::toString() returns value"(Hbm2ddlAuto e, String expected) {
        expect:
        expected == e.toString()

        where:
        e                       | expected
        Hbm2ddlAuto.CREATE      | 'create'
        Hbm2ddlAuto.CREATE_DROP | 'create-drop'
        Hbm2ddlAuto.DROP        | 'drop'
        Hbm2ddlAuto.NONE        | 'none'
        Hbm2ddlAuto.UPDATE      | 'update'
        Hbm2ddlAuto.CREATE_ONLY | 'create-only'
        Hbm2ddlAuto.VALIDATE    | 'validate'
    }
}
