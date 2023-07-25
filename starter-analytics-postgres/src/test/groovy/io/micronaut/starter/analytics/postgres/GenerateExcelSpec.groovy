package io.micronaut.starter.analytics.postgres

import io.micronaut.context.annotation.Property
import io.micronaut.context.annotation.Requires
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Consumes
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.*
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.apache.poi.ss.usermodel.DateUtil
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.testcontainers.DockerClientFactory
import org.testcontainers.containers.PostgreSQLContainer
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

import java.util.concurrent.CompletableFuture

import static io.micronaut.starter.analytics.postgres.ExcelGenerator.ExcelColumn.*

@Property(name = "spec.name", value = "GenerateExcelSpec")
@MicronautTest(transactional = false)
@spock.lang.Requires({ DockerClientFactory.instance().isDockerAvailable() })
class GenerateExcelSpec extends Specification implements TestPropertyProvider {

    @Shared @AutoCleanup PostgreSQLContainer postgres = new PostgreSQLContainer<>("postgres:10")
            .withDatabaseName("test-database")
            .withUsername("test")
            .withPassword("test")

    @Override
    Map<String, String> getProperties() {
        postgres.start()

        ["datasources.default.url":postgres.getJdbcUrl(),
         "datasources.default.username":postgres.getUsername(),
         "datasources.default.password":postgres.getPassword(),
         "datasources.default.dialect": Dialect.POSTGRES.name()]
    }

    @Inject AnalyticsClient client
    @Inject ApplicationRepository repository
    @Inject FeatureRepository featureRepository

    void "test generate excel"() {
        given: "there is one application stored in the repository"
        Application app = repository.save(new Application(ApplicationType.DEFAULT, Language.JAVA, BuildTool.GRADLE, TestFramework.SPOCK, JdkVersion.JDK_8, "4.0.1"))
        featureRepository.saveAll([new Feature(app, "micronaut-http-validation"), new Feature(app, "http-client")])

        when: "the spreadsheet is generated"
        byte[] data = client.generateExcel().get()
        Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(data))

        then: "the contents of the spreadsheet are correct"
        workbook.numberOfSheets == 1
        with(workbook.getSheetAt(0)) {
            sheetName == ExcelGenerator.SHEET_NAME
            physicalNumberOfRows == 2
            with(getRow(0)) {
                rowStyle.locked == true
                getCell(ID.ordinal()).stringCellValue == ID.title
                getCell(FEATURES.ordinal()).stringCellValue == FEATURES.title
                getCell(TYPE.ordinal()).stringCellValue == TYPE.title
                getCell(LANGUAGE.ordinal()).stringCellValue == LANGUAGE.title
                getCell(BUILD_TOOL.ordinal()).stringCellValue == BUILD_TOOL.title
                getCell(TEST_FRAMEWORK.ordinal()).stringCellValue == TEST_FRAMEWORK.title
                getCell(JDK_VERSION.ordinal()).stringCellValue == JDK_VERSION.title
                getCell(MICRONAUT_VERSION.ordinal()).stringCellValue == MICRONAUT_VERSION.title
                getCell(DATE_CREATED.ordinal()).stringCellValue == DATE_CREATED.title
            }
            with(getRow(1)) {
                getCell(ID.ordinal()).numericCellValue == app.id
                with (getCell(FEATURES.ordinal()).stringCellValue.split(", ")) {
                    size() == 2
                    contains("micronaut-http-validation")
                    contains("http-client")
                }
                getCell(TYPE.ordinal()).stringCellValue == "DEFAULT"
                getCell(LANGUAGE.ordinal()).stringCellValue == "java"
                getCell(BUILD_TOOL.ordinal()).stringCellValue == "gradle"
                getCell(TEST_FRAMEWORK.ordinal()).stringCellValue == "spock"
                getCell(JDK_VERSION.ordinal()).numericCellValue == 8
                getCell(MICRONAUT_VERSION.ordinal()).stringCellValue == "4.0.1"
                getCell(DATE_CREATED.ordinal()).numericCellValue == DateUtil.getExcelDate(app.dateCreated, false)
            }
        }
    }

    @Requires(property = "spec.name", value = "GenerateExcelSpec")
    @Client("/analytics")
    static interface AnalyticsClient {

        @Get("/excel")
        @Consumes(MediaType.MICROSOFT_EXCEL_OPEN_XML)
        CompletableFuture<byte[]> generateExcel()
    }
}
