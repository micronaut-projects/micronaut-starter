package io.micronaut.starter.netty

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client
import io.micronaut.starter.api.SelectOptionsDTO
import io.micronaut.starter.api.TestFrameworkDTO
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.MicronautJdkVersionConfiguration
import io.micronaut.starter.options.TestFramework
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Issue
import spock.lang.Specification

@MicronautTest
class SelectOptionsTest extends Specification {

    @Inject
    SelectOptionsClient client

    void "Select options are provided"() {
        when: "We ask for the options"
        SelectOptionsDTO selectOptions = client.selectOptions().body()

        then: "We have all the application type options"
        def typeOpts = selectOptions.type.options
        typeOpts.size() == ApplicationType.values().size()

        then: "We get the correct application type default"
        selectOptions.type.defaultOption.value ==
                ApplicationType.DEFAULT_OPTION; // This Matches DTO impl

        ApplicationType.values().each { t ->
            then: "We can find the ${t.name()} type"
            typeOpts.find {so -> t == so.value} != null
        }

        then: "We get all the languages"
        def languageOpts = selectOptions.lang.options
        languageOpts.size() == Language.values().size()

        then: "We get the correct Language default"
        selectOptions.lang.defaultOption.value == Language.DEFAULT_OPTION

        Language.values().each { lang ->
            then: "We can find the ${lang.name()} language"
            languageOpts.find {so -> lang == so.value} != null
        }

        then: "We get all the jdk version options"
        def jdkVersionOpts = selectOptions.jdkVersion.options
        jdkVersionOpts.size() == MicronautJdkVersionConfiguration.SUPPORTED_JDKS.size()

        then: "We get the correct JdkVersion default"
        selectOptions.jdkVersion.defaultOption.value == MicronautJdkVersionConfiguration.DEFAULT_OPTION

        MicronautJdkVersionConfiguration.SUPPORTED_JDKS.each { jdkVer ->
            then: "We can find the ${jdkVer.name()} jdk version"
            jdkVersionOpts.find {so -> jdkVer == so.value} != null
        }

        then: "We get all the test framework options"
        def testOpts = selectOptions.test.options
        testOpts.size() == TestFramework.values().size()

        then: "We get the correct TestFramework default"
        selectOptions.test.defaultOption.value == TestFramework.DEFAULT_OPTION

        TestFramework.values().each { t ->
            then: "We can find the ${t.name()} test framework"
            testOpts.find {so -> t == so.value} != null
        }

        then: "We have all the build tools options"
        def buildOpts = selectOptions.build.options
        buildOpts.size() == BuildTool.values().size()

        then: "We get the correct build tool default"
        selectOptions.build.defaultOption.value == BuildTool.DEFAULT_OPTION

        BuildTool.values().each { t ->
            then: "We can find the ${t.name()} build tool"
            buildOpts.find {so -> t == so.value} != null
        }
    }

    void "Test Default correlations"() {
        when: "We ask for the options"
        SelectOptionsDTO selectOptions = client.selectOptions().body()

        then:
        selectOptions.lang.options.each {
            def langDefault = it.defaults;

            then: "We get valid correlative build tools"
            selectOptions.build.options.find{ it.value == langDefault.build} != null

            then: "We get valid correlative test tool"
            selectOptions.test.options.find{ it.value == langDefault.test} != null
        }
    }

    @Issue("https://github.com/micronaut-projects/micronaut-starter-ui/issues/42")
    void 'SelectOption label of JUNIT is JUnit, not Junit'() {
        when: "We ask for the options"
        SelectOptionsDTO selectOptions = client.selectOptions().body()

        then:
        TestFrameworkDTO junit = selectOptions.test.options.find {it.value == TestFramework.JUNIT}
        junit.label == 'JUnit'
        junit.description == 'JUnit'
    }

    @Client('/select-options')
    static interface SelectOptionsClient {
        @Get('/')
        HttpResponse<SelectOptionsDTO> selectOptions();
    }
}
