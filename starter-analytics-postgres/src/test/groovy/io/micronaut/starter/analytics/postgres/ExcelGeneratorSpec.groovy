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

        ["micronaut.starter.analytics.page-size":5,
         "datasources.default.url":postgres.getJdbcUrl(),
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

    void "generate spreadsheet with 12 applications"() {
        given: "there are two applications stored in the repository"
        List<Application> apps = applicationRepository.saveAll([
                new Application(ApplicationType.FUNCTION, Language.KOTLIN, BuildTool.GRADLE_KOTLIN, TestFramework.KOTEST, JdkVersion.JDK_17, "4.0.1"),
                new Application(ApplicationType.CLI, Language.GROOVY, BuildTool.MAVEN, TestFramework.JUNIT, JdkVersion.JDK_20, "4.0.2"),
                new Application(ApplicationType.DEFAULT, Language.JAVA, BuildTool.GRADLE, TestFramework.SPOCK, JdkVersion.JDK_17, "4.0.3"),
                new Application(ApplicationType.DEFAULT, Language.JAVA, BuildTool.GRADLE, TestFramework.SPOCK, JdkVersion.JDK_17, "4.0.4"),
                new Application(ApplicationType.DEFAULT, Language.JAVA, BuildTool.GRADLE, TestFramework.SPOCK, JdkVersion.JDK_17, "4.0.5"),
                new Application(ApplicationType.DEFAULT, Language.JAVA, BuildTool.GRADLE, TestFramework.SPOCK, JdkVersion.JDK_17, "4.0.6"),
                new Application(ApplicationType.DEFAULT, Language.JAVA, BuildTool.GRADLE, TestFramework.SPOCK, JdkVersion.JDK_17, "4.0.7"),
                new Application(ApplicationType.DEFAULT, Language.JAVA, BuildTool.GRADLE, TestFramework.SPOCK, JdkVersion.JDK_17, "4.0.8"),
                new Application(ApplicationType.DEFAULT, Language.JAVA, BuildTool.GRADLE, TestFramework.SPOCK, JdkVersion.JDK_17, "4.0.9"),
                new Application(ApplicationType.DEFAULT, Language.JAVA, BuildTool.GRADLE, TestFramework.SPOCK, JdkVersion.JDK_17, "4.0.10"),
                new Application(ApplicationType.DEFAULT, Language.JAVA, BuildTool.GRADLE, TestFramework.SPOCK, JdkVersion.JDK_17, "4.0.11"),
                new Application(ApplicationType.DEFAULT, Language.JAVA, BuildTool.GRADLE, TestFramework.SPOCK, JdkVersion.JDK_17, "4.0.12"),
        ])
        featureRepository.saveAll([
                new Feature(apps[0], "one"),
                new Feature(apps[1], "two"),
                new Feature(apps[2], "three"),
                new Feature(apps[3], "four"),
                new Feature(apps[4], "five"),
                new Feature(apps[5], "six"),
                new Feature(apps[6], "seven"),
                new Feature(apps[7], "eight"),
                new Feature(apps[8], "nine"),
                new Feature(apps[9], "ten"),
                new Feature(apps[10], "eleven"),
                new Feature(apps[11], "twelve"),
        ])
        when: "the spreadsheet is generated"
        Workbook workbook = excelGenerator.generateExcel()

        then: "the contents of the spreadsheet are correct"
        workbook.numberOfSheets == 1
        with(workbook.getSheetAt(0)) {
            sheetName == ExcelGenerator.SHEET_NAME
            physicalNumberOfRows == 13
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
                getCell(FEATURES.ordinal()).stringCellValue == "one"
                getCell(TYPE.ordinal()).stringCellValue == "FUNCTION"
                getCell(LANGUAGE.ordinal()).stringCellValue == "kotlin"
                getCell(BUILD_TOOL.ordinal()).stringCellValue == "gradle_kotlin"
                getCell(TEST_FRAMEWORK.ordinal()).stringCellValue == "kotest"
                getCell(JDK_VERSION.ordinal()).numericCellValue == 17
                getCell(MICRONAUT_VERSION.ordinal()).stringCellValue == "4.0.1"
                getCell(DATE_CREATED.ordinal()).numericCellValue == DateUtil.getExcelDate(apps[0].dateCreated, false)
            }
            with(getRow(2)) {
                getCell(ID.ordinal()).numericCellValue == apps[1].id
                getCell(FEATURES.ordinal()).stringCellValue == "two"
                getCell(TYPE.ordinal()).stringCellValue == "CLI"
                getCell(LANGUAGE.ordinal()).stringCellValue == "groovy"
                getCell(BUILD_TOOL.ordinal()).stringCellValue == "maven"
                getCell(TEST_FRAMEWORK.ordinal()).stringCellValue == "junit"
                getCell(JDK_VERSION.ordinal()).numericCellValue == 20
                getCell(MICRONAUT_VERSION.ordinal()).stringCellValue == "4.0.2"
                getCell(DATE_CREATED.ordinal()).numericCellValue == DateUtil.getExcelDate(apps[1].dateCreated, false)
            }
            with(getRow(3)) {
                getCell(ID.ordinal()).numericCellValue == apps[2].id
                getCell(FEATURES.ordinal()).stringCellValue == "three"
            }
            with(getRow(4)) {
                getCell(ID.ordinal()).numericCellValue == apps[3].id
                getCell(FEATURES.ordinal()).stringCellValue == "four"
            }
            with(getRow(5)) {
                getCell(ID.ordinal()).numericCellValue == apps[4].id
                getCell(FEATURES.ordinal()).stringCellValue == "five"
            }
            with(getRow(6)) {
                getCell(ID.ordinal()).numericCellValue == apps[5].id
                getCell(FEATURES.ordinal()).stringCellValue == "six"
            }
            with(getRow(7)) {
                getCell(ID.ordinal()).numericCellValue == apps[6].id
                getCell(FEATURES.ordinal()).stringCellValue == "seven"
            }
            with(getRow(8)) {
                getCell(ID.ordinal()).numericCellValue == apps[7].id
                getCell(FEATURES.ordinal()).stringCellValue == "eight"
            }
            with(getRow(9)) {
                getCell(ID.ordinal()).numericCellValue == apps[8].id
                getCell(FEATURES.ordinal()).stringCellValue == "nine"
            }
            with(getRow(10)) {
                getCell(ID.ordinal()).numericCellValue == apps[9].id
                getCell(FEATURES.ordinal()).stringCellValue == "ten"
            }
            with(getRow(11)) {
                getCell(ID.ordinal()).numericCellValue == apps[10].id
                getCell(FEATURES.ordinal()).stringCellValue == "eleven"
            }
            with(getRow(12)) {
                getCell(ID.ordinal()).numericCellValue == apps[11].id
                getCell(FEATURES.ordinal()).stringCellValue == "twelve"
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
