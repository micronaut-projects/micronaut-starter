package io.micronaut.starter.core.test.feature.database

import io.micronaut.starter.core.test.feature.database.templates.book
import io.micronaut.starter.feature.database.DataHibernateReactive
import io.micronaut.starter.feature.database.MariaDB
import io.micronaut.starter.feature.database.MySQL
import io.micronaut.starter.feature.database.Oracle
import io.micronaut.starter.feature.database.SQLServer
import io.micronaut.starter.io.ConsoleOutput
import io.micronaut.starter.io.FileSystemOutputHandler
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.template.RockerWritable
import io.micronaut.starter.test.CommandSpec
import org.gradle.testkit.runner.BuildResult

class DataHibernateReactiveSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "hibernateReactiveJpa"
    }

    void "test maven data-hibernate-reactive with java and #db"(String db) {
        when:
        generateProject(Language.JAVA, BuildTool.MAVEN, [DataHibernateReactive.NAME, db])
        def fsoh = new FileSystemOutputHandler(dir, ConsoleOutput.NOOP)
        fsoh.write("src/main/java/example/micronaut/Book.java", new RockerWritable(book.template()))

        // If SqlServer, we need to accept the license
        if (db == SQLServer.NAME) {
            new File(dir, "src/test/resources/").mkdirs()
            fsoh.write("src/test/resources/application-test.yml", { OutputStream output -> output.write("\ntest-resources.containers.mssql.accept-license: true".bytes) })
        }

        String output = executeMaven("-DtrimStackTrace=false compile test")

        then:
        output?.contains("BUILD SUCCESS")

        where:
        db << [
                MySQL.NAME,
                MariaDB.NAME,
//                PostgreSQL.NAME, // TODO: Reinstate Postgres test once the vert.x driver missing dependency is sorted out
                Oracle.NAME,
                SQLServer.NAME,
        ]
    }

    void "test gradle data-hibernate-reactive with java and #db"(String db) {
        when:
        generateProject(Language.JAVA, BuildTool.GRADLE, [DataHibernateReactive.NAME, db])
        def fsoh = new FileSystemOutputHandler(dir, ConsoleOutput.NOOP)
        fsoh.write("src/main/java/example/micronaut/Book.java", new RockerWritable(book.template()))

        // If SqlServer, we need to accept the license
        if (db == SQLServer.NAME) {
            new File(dir, "src/test/resources/").mkdirs()
            fsoh.write("src/test/resources/application-test.yml", { OutputStream output -> output.write("\ntest-resources.containers.mssql.accept-license: true".bytes) })
        }

        BuildResult result = executeGradle("test")

        then:
        result?.output?.contains("BUILD SUCCESS")

        where:
        db << [
                MySQL.NAME,
                MariaDB.NAME,
//                PostgreSQL.NAME, // TODO: Reinstate Postgres test once the vert.x driver missing dependency is sorted out
                Oracle.NAME,
                SQLServer.NAME,
        ]
    }
}
