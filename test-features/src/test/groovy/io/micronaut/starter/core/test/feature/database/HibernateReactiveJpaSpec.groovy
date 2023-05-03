package io.micronaut.starter.core.test.feature.database

import io.micronaut.starter.core.test.feature.database.templates.book
import io.micronaut.starter.feature.database.HibernateReactiveJpa
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
import spock.lang.Requires

@Requires({ jvm.current.isJava11Compatible() })
class HibernateReactiveJpaSpec extends CommandSpec {
    @Override
    String getTempDirectoryPrefix() {
        return "hibernateReactiveJpa"
    }

    @Ignore('An exception java.lang.IncompatibleClassChangeError: class io.micronaut.http.filter.GenericHttpFilter$TerminalWithReactorContext cannot extend sealed interface io.micronaut.http.filter.GenericHttpFilter enable DEBUG level for full stacktrace was thrown by a user handler exceptionCaught() method while handling the following exception: java.lang.NoSuchFieldError: ROUTE')
    @IgnoreIf({ BuildToolTest.IGNORE_MAVEN })
    void "test maven hibernate-reactive-jpa with java and #db"(String db) {
        when:
        generateProject(Language.JAVA, BuildTool.MAVEN, [HibernateReactiveJpa.NAME, db])
        def fsoh = new FileSystemOutputHandler(dir, ConsoleOutput.NOOP)
        fsoh.write("src/main/java/example/micronaut/Book.java", new RockerWritable(book.template()))

        String output = executeMaven("compile test")

        then:
        output?.contains("BUILD SUCCESS")

        where:
        db << featuresNames()
    }

    void "test gradle hibernate-reactive-jpa with java and #db"(String db) {
        when:
        generateProject(Language.JAVA, BuildTool.GRADLE, [HibernateReactiveJpa.NAME, db])
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
                //SQLServer.NAME   - Unexpected error occurred: class java.lang.String cannot be cast to class java.lang.Boolean (java.lang.String and java.lang.Boolean are in module java.base of loader 'bootstrap') java.lang.ClassCastException: class java.lang.String cannot be cast to class java.lang.Boolean (java.lang.String and java.lang.Boolean are in module java.base of loader 'bootstrap') at io.micronaut.testresources.mssql.MSSQLTestResourceProvider.createMSSQLContainer(MSSQLTestResourceProvider.java:50)
        ]
    }
}
