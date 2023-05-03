package io.micronaut.starter.core.test.feature.database

import io.micronaut.context.annotation.Replaces
import io.micronaut.starter.feature.database.SQLServer
import io.micronaut.starter.feature.database.TestContainers
import io.micronaut.starter.feature.database.jdbc.JdbcFeature
import io.micronaut.starter.feature.testresources.TestResources
import jakarta.inject.Singleton

@Replaces(SQLServer.class)
@Singleton
class AcceptLicenseSqlServer extends SQLServer {

    AcceptLicenseSqlServer(JdbcFeature jdbcFeature, TestContainers testContainers, TestResources testResources) {
        super(jdbcFeature, testContainers, testResources)
    }

    @Override
    boolean isVisible() {
        return false
    }

    @Override
    protected boolean acceptLicense() {
        return true
    }
}
