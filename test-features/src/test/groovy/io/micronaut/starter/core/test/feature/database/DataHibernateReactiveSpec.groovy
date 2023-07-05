package io.micronaut.starter.core.test.feature.database

import io.micronaut.starter.core.test.feature.database.templates.book
import io.micronaut.starter.feature.database.DataHibernateReactive
import io.micronaut.starter.feature.database.MariaDB
import io.micronaut.starter.feature.database.MySQL
import io.micronaut.starter.feature.database.Oracle
import io.micronaut.starter.feature.database.PostgreSQL
import io.micronaut.starter.feature.database.SQLServer
import io.micronaut.starter.io.ConsoleOutput
import io.micronaut.starter.io.FileSystemOutputHandler
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.template.RockerWritable
import io.micronaut.starter.test.BuildToolTest
import io.micronaut.starter.test.CommandSpec
import org.gradle.testkit.runner.BuildResult
import spock.lang.Ignore
import spock.lang.IgnoreIf

class DataHibernateReactiveSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "hibernateReactiveJpa"
    }

    @IgnoreIf({ BuildToolTest.IGNORE_MAVEN })
    void "test maven data-hibernate-reactive with java and #db"(String db) {
        when:
        generateProject(Language.JAVA, BuildTool.MAVEN, [DataHibernateReactive.NAME, db])
        def fsoh = new FileSystemOutputHandler(dir, ConsoleOutput.NOOP)
        fsoh.write("src/main/java/example/micronaut/Book.java", new RockerWritable(book.template()))

        String output = executeMaven("-DtrimStackTrace=false compile test")

        then:
        output?.contains("BUILD SUCCESS")

        where:
        db << featuresNames()
    }

    void "test gradle data-hibernate-reactive with java and #db"(String db) {
        when:
        generateProject(Language.JAVA, BuildTool.GRADLE, [DataHibernateReactive.NAME, db])
        def fsoh = new FileSystemOutputHandler(dir, ConsoleOutput.NOOP)
        fsoh.write("src/main/java/example/micronaut/Book.java", new RockerWritable(book.template()))

        BuildResult result = executeGradle("test")

        then:
        result?.output?.contains("BUILD SUCCESS")

        where:
        db << featuresNames()
    }

    private static List<String> featuresNames() {
        [
                MySQL.NAME,
                MariaDB.NAME,
                PostgreSQL.NAME,
                Oracle.NAME,
                SQLServer.NAME
        ]
    }
}
