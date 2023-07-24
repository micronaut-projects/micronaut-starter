package io.micronaut.starter.analytics.postgres

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import org.apache.poi.ss.usermodel.DateUtil
import org.apache.poi.ss.usermodel.Workbook
import spock.lang.Specification

import java.time.LocalDateTime
import java.util.stream.Collectors

import static io.micronaut.starter.analytics.postgres.ExcelGenerator.ExcelColumn.*

class ExcelGeneratorSpec extends Specification {

    void "generate spreadsheet with one application"() {
        given: "there is one application in the given list"
        Application app = app(123, ApplicationType.DEFAULT, Language.JAVA, BuildTool.GRADLE, TestFramework.SPOCK, JdkVersion.JDK_8, "4.0.0", "foo", "bar")
        List<Application> apps = [app]

        when: "the spreadsheet is generated"
        Workbook workbook = new ExcelGenerator().generateExcel(apps)

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
                getCell(ID.ordinal()).numericCellValue == 123
                getCell(FEATURES.ordinal()).stringCellValue == "foo, bar"
                getCell(TYPE.ordinal()).stringCellValue == "DEFAULT"
                getCell(LANGUAGE.ordinal()).stringCellValue == "java"
                getCell(BUILD_TOOL.ordinal()).stringCellValue == "gradle"
                getCell(TEST_FRAMEWORK.ordinal()).stringCellValue == "spock"
                getCell(JDK_VERSION.ordinal()).numericCellValue == 8
                getCell(MICRONAUT_VERSION.ordinal()).stringCellValue == "4.0.0"
                getCell(DATE_CREATED.ordinal()).numericCellValue == DateUtil.getExcelDate(app.dateCreated, false)
            }
        }
    }

    void "generate spreadsheet with two applications"() {
        given: "there are two applications in the given list"
        Application app1 = app(1, ApplicationType.FUNCTION, Language.KOTLIN, BuildTool.GRADLE_KOTLIN, TestFramework.KOTEST, JdkVersion.JDK_17, "1.2.3", "hello")
        Application app2 = app(2, ApplicationType.CLI, Language.GROOVY, BuildTool.MAVEN, TestFramework.JUNIT, JdkVersion.JDK_20, "3.2.1")
        List<Application> apps = [app1, app2]

        when: "the spreadsheet is generated"
        Workbook workbook = new ExcelGenerator().generateExcel(apps)

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
                getCell(ID.ordinal()).numericCellValue == 1
                getCell(FEATURES.ordinal()).stringCellValue == "hello"
                getCell(TYPE.ordinal()).stringCellValue == "FUNCTION"
                getCell(LANGUAGE.ordinal()).stringCellValue == "kotlin"
                getCell(BUILD_TOOL.ordinal()).stringCellValue == "gradle_kotlin"
                getCell(TEST_FRAMEWORK.ordinal()).stringCellValue == "kotest"
                getCell(JDK_VERSION.ordinal()).numericCellValue == 17
                getCell(MICRONAUT_VERSION.ordinal()).stringCellValue == "1.2.3"
                getCell(DATE_CREATED.ordinal()).numericCellValue == DateUtil.getExcelDate(app1.dateCreated, false)
            }
            with(getRow(2)) {
                getCell(ID.ordinal()).numericCellValue == 2
                getCell(FEATURES.ordinal()).stringCellValue == ""
                getCell(TYPE.ordinal()).stringCellValue == "CLI"
                getCell(LANGUAGE.ordinal()).stringCellValue == "groovy"
                getCell(BUILD_TOOL.ordinal()).stringCellValue == "maven"
                getCell(TEST_FRAMEWORK.ordinal()).stringCellValue == "junit"
                getCell(JDK_VERSION.ordinal()).numericCellValue == 20
                getCell(MICRONAUT_VERSION.ordinal()).stringCellValue == "3.2.1"
                getCell(DATE_CREATED.ordinal()).numericCellValue == DateUtil.getExcelDate(app2.dateCreated, false)
            }
        }
    }

    void "generate spreadsheet with no applications"() {
        given: "there is no application in the given list"
        List<Application> apps = []

        when: "the spreadsheet is generated"
        Workbook workbook = new ExcelGenerator().generateExcel(apps)

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

    static app(Long id, ApplicationType type, Language language, BuildTool buildTool, TestFramework testFramework, JdkVersion jdkVersion, String micronautVersion, String... features) {
        Application app = new Application(type, language, buildTool, testFramework, jdkVersion, micronautVersion)
        app.id = id
        app.features = Arrays.stream(features).map { new Feature(app, it) }.collect(Collectors.toCollection(LinkedHashSet::new))
        app.dateCreated = LocalDateTime.now()
        return app
    }
}
