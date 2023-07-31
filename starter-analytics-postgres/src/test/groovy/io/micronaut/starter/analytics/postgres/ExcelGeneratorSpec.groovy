package io.micronaut.starter.analytics.postgres

import io.micronaut.context.annotation.Property
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.apache.poi.ss.usermodel.DateUtil
import org.apache.poi.ss.usermodel.Workbook
import org.testcontainers.containers.PostgreSQLContainer
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

import static io.micronaut.starter.analytics.postgres.ExcelGenerator.ExcelColumn.*

@Property(name = "spec.name", value = "ExcelGeneratorSpec")
@MicronautTest(transactional = false)
class ExcelGeneratorSpec extends Specification implements TestPropertyProvider {

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

    @Inject ExcelGenerator excelGenerator
    @Inject ApplicationRepository applicationRepository
    @Inject FeatureRepository featureRepository

    void "generate spreadsheet with one application"() {
        given: "there is one application stored in the repository"
        Application app = applicationRepository.save(new Application(ApplicationType.DEFAULT, Language.JAVA, BuildTool.GRADLE, TestFramework.SPOCK, JdkVersion.JDK_8, "4.0.0"))
        featureRepository.saveAll([new Feature(app, "foo"), new Feature(app, "bar")])

        when: "the spreadsheet is generated"
        Workbook workbook = excelGenerator.generateExcel()

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
                with(getCell(FEATURES.ordinal()).stringCellValue.split(", ")) {
                    size() == 2
                    contains("foo")
                    contains("bar")
                }
                getCell(TYPE.ordinal()).stringCellValue == "DEFAULT"
                getCell(LANGUAGE.ordinal()).stringCellValue == "java"
                getCell(BUILD_TOOL.ordinal()).stringCellValue == "gradle"
                getCell(TEST_FRAMEWORK.ordinal()).stringCellValue == "spock"
                getCell(JDK_VERSION.ordinal()).numericCellValue == 8
                getCell(MICRONAUT_VERSION.ordinal()).stringCellValue == "4.0.0"
                getCell(DATE_CREATED.ordinal()).numericCellValue == DateUtil.getExcelDate(app.dateCreated, false)
            }
        }
        cleanup:
        featureRepository.deleteAll()
        applicationRepository.deleteAll()
    }

    void "generate spreadsheet with two applications"() {
        given: "there are two applications stored in the repository"
        List<Application> apps = applicationRepository.saveAll([
                new Application(ApplicationType.FUNCTION, Language.KOTLIN, BuildTool.GRADLE_KOTLIN, TestFramework.KOTEST, JdkVersion.JDK_17, "1.2.3"),
                new Application(ApplicationType.CLI, Language.GROOVY, BuildTool.MAVEN, TestFramework.JUNIT, JdkVersion.JDK_20, "3.2.1")])
        featureRepository.saveAll([new Feature(apps[0], "hello"), new Feature(apps[1], "world")])
        when: "the spreadsheet is generated"
        Workbook workbook = excelGenerator.generateExcel()

        then: "the contents of the spreadsheet are correct"
        workbook.numberOfSheets == 1
        with(workbook.getSheetAt(0)) {
            sheetName == ExcelGenerator.SHEET_NAME
            physicalNumberOfRows == 3
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
                getCell(ID.ordinal()).numericCellValue == apps[0].id
                getCell(FEATURES.ordinal()).stringCellValue == "hello"
                getCell(TYPE.ordinal()).stringCellValue == "FUNCTION"
                getCell(LANGUAGE.ordinal()).stringCellValue == "kotlin"
                getCell(BUILD_TOOL.ordinal()).stringCellValue == "gradle_kotlin"
                getCell(TEST_FRAMEWORK.ordinal()).stringCellValue == "kotest"
                getCell(JDK_VERSION.ordinal()).numericCellValue == 17
                getCell(MICRONAUT_VERSION.ordinal()).stringCellValue == "1.2.3"
                getCell(DATE_CREATED.ordinal()).numericCellValue == DateUtil.getExcelDate(apps[0].dateCreated, false)
            }
            with(getRow(2)) {
                getCell(ID.ordinal()).numericCellValue == apps[1].id
                getCell(FEATURES.ordinal()).stringCellValue == "world"
                getCell(TYPE.ordinal()).stringCellValue == "CLI"
                getCell(LANGUAGE.ordinal()).stringCellValue == "groovy"
                getCell(BUILD_TOOL.ordinal()).stringCellValue == "maven"
                getCell(TEST_FRAMEWORK.ordinal()).stringCellValue == "junit"
                getCell(JDK_VERSION.ordinal()).numericCellValue == 20
                getCell(MICRONAUT_VERSION.ordinal()).stringCellValue == "3.2.1"
                getCell(DATE_CREATED.ordinal()).numericCellValue == DateUtil.getExcelDate(apps[1].dateCreated, false)
            }
        }
        cleanup:
        featureRepository.deleteAll()
        applicationRepository.deleteAll()
    }

    void "generate spreadsheet with no applications"() {
        when: "there is no application stored in the repository"
        Workbook workbook = excelGenerator.generateExcel()

        then: "the contents of the spreadsheet are correct"
        workbook.numberOfSheets == 1
        with(workbook.getSheetAt(0)) {
            sheetName == ExcelGenerator.SHEET_NAME
            physicalNumberOfRows == 1
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
        }
    }
}
