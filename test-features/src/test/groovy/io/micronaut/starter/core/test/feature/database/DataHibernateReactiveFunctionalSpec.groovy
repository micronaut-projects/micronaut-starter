package io.micronaut.starter.core.test.feature.database

import io.micronaut.starter.core.test.feature.database.templates.book
import io.micronaut.starter.feature.database.DataHibernateReactive
import io.micronaut.starter.feature.database.MariaDB
import io.micronaut.starter.feature.database.MySQL
import io.micronaut.starter.feature.database.Oracle
import io.micronaut.starter.feature.database.PostgreSQL
import io.micronaut.starter.feature.database.SQLServer
import io.micronaut.starter.feature.validator.MicronautValidationFeature
import io.micronaut.starter.io.ConsoleOutput
import io.micronaut.starter.io.FileSystemOutputHandler
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.template.RockerWritable
import io.micronaut.starter.test.BuildToolTest
import io.micronaut.starter.test.CommandSpec
import io.micronaut.starter.test.PredicateUtils
import org.gradle.testkit.runner.BuildResult
import spock.lang.IgnoreIf
// Don't delete this import.  It is not an unused import
import io.micronaut.starter.core.test.feature.database.templates.book

class DataHibernateReactiveFunctionalSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "hibernateReactiveJpa"
    }

    @IgnoreIf({ BuildToolTest.IGNORE_MAVEN })
    void "test maven data-hibernate-reactive with java and #db"(String db) {
        when:
        generateProject(Language.JAVA, BuildTool.MAVEN, [DataHibernateReactive.NAME, db, MicronautValidationFeature.NAME])
        def fsoh = new FileSystemOutputHandler(dir, ConsoleOutput.NOOP)
        fsoh.write("src/main/java/example/micronaut/Book.java", new RockerWritable(book.template()))

        String output = executeMaven("-DtrimStackTrace=false compile test")

        then:
        output?.contains("BUILD SUCCESS")

        where:
        db << featuresNames()
                .stream()
                .filter( f -> PredicateUtils.testFeatureIfMacOS(List.of(Oracle.NAME, SQLServer.NAME)).test(f))
                .toList()
    }

    void "test #buildTool data-hibernate-reactive with java and #db"(BuildTool buildTool, String db) {
        when:
        generateProject(Language.JAVA, buildTool, [DataHibernateReactive.NAME, db, MicronautValidationFeature.NAME])
        def fsoh = new FileSystemOutputHandler(dir, ConsoleOutput.NOOP)
        fsoh.write("src/main/java/example/micronaut/Book.java", new RockerWritable(book.template()))

        BuildResult result = executeGradle("test")

        then:
        result?.output?.contains("BUILD SUCCESS")

        where:
        [buildTool, db] << [BuildTool.valuesGradle(), featuresNames()
                .stream()
                .filter( f -> PredicateUtils.testFeatureIfMacOS(List.of(Oracle.NAME, SQLServer.NAME)).test(f))
                .toList()
        ].combinations()
    }

    private static List<String> featuresNames() {
        [
                MySQL.NAME,
                MariaDB.NAME,
                PostgreSQL.NAME,
                Oracle.NAME,
                SQLServer.NAME
        ].stream()
                .filter(n -> n != Oracle.NAME || Oracle.COMPATIBLE_WITH_HIBERNATE_REACTIVE)
                .toList()
    }
}
