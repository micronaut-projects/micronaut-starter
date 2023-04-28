package io.micronaut.starter.feature.database

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Dependency
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.database.jdbc.Hikari
import io.micronaut.starter.options.BuildTool

import static io.micronaut.starter.build.dependencies.MicronautDependencyUtils.GROUP_ID_MICRONAUT
import static io.micronaut.starter.build.dependencies.MicronautDependencyUtils.GROUP_ID_MICRONAUT_DATA
import static io.micronaut.starter.build.dependencies.MicronautDependencyUtils.GROUP_ID_MICRONAUT_SQL
import static io.micronaut.starter.build.dependencies.MicronautDependencyUtils.ARTIFACT_ID_MICRONAUT_INJECT
import static io.micronaut.starter.feature.database.DataFeature.MICRONAUT_DATA_PROCESSOR_ARTIFACT

abstract class BaseHibernateReactiveSpec extends ApplicationContextSpec {

    boolean containsDataProcessor(String template) {
        mavenVerifier(template).hasDependencyWithExclusion(
                GROUP_ID_MICRONAUT_DATA, MICRONAUT_DATA_PROCESSOR_ARTIFACT,
                Scope.ANNOTATION_PROCESSOR,
                GROUP_ID_MICRONAUT, ARTIFACT_ID_MICRONAUT_INJECT)
    }

    boolean containsDataHibernateJpa(String template) {
        mavenVerifier(template).hasDependency(GROUP_ID_MICRONAUT_DATA, "micronaut-data-hibernate-jpa", Scope.COMPILE )
    }

    boolean containsDataHibernateReactive(String template) {
        mavenVerifier(template).hasDependency(GROUP_ID_MICRONAUT_DATA, "micronaut-data-hibernate-reactive", Scope.COMPILE )
    }

    boolean containsJdbcDriver(String template, Dependency migrationDriver) {
        mavenVerifier(template).hasDependency(migrationDriver.groupId, migrationDriver.artifactId, Scope.RUNTIME )
    }

    boolean containsMigrationLibrary(String template, String migration) {
        mavenVerifier(template).hasDependency("io.micronaut.$migration", "micronaut-$migration", Scope.COMPILE )
    }

    boolean containsVertXDriver(String template, String client) {

        mavenVerifier(template).hasDependency(HibernateReactiveFeature.IO_VERTX_DEPENDENCY_GROUP, client, Scope.COMPILE )
    }

    boolean contansSqlHibernateReactive(String template) {
        mavenVerifier(template).hasDependency(GROUP_ID_MICRONAUT_SQL, "micronaut-hibernate-reactive", Scope.COMPILE )
    }

    boolean containsHikariDependency(String template) {
        mavenVerifier(template).hasDependency(GROUP_ID_MICRONAUT_SQL, Hikari.MICRONAUT_JDBC_HIKARI_ARTIFACT, Scope.COMPILE )
    }

    BuildTestVerifier mavenVerifier(String template) {
        return BuildTestUtil.verifier(BuildTool.MAVEN, template)
    }
}
