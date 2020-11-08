package io.micronaut.starter.netty

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client
import io.micronaut.starter.api.SelectOptionsDTO
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification
import javax.inject.Inject

@MicronautTest
class SelectOptionsTest extends Specification {

    @Inject
    SelectOptionsClient client

    void "Select options are provided"() {
        when: "We ask for the options"
        SelectOptionsDTO selectOptions = client.selectOptions().body()

        then: "We have all the application type options"
        def typeOpts = selectOptions.types.options
        typeOpts.size() == ApplicationType.values().size()

        then: "We get the correct application type default"
        selectOptions.types.defaultOption.value ==
                ApplicationType.DEFAULT_OPTION; // This Matches DTO impl

        ApplicationType.values().each { t ->
            then: "We can find the ${t.name()} type"
            typeOpts.find {so -> t == so.value} != null
        }

        then: "We get all the languages"
        def languageOpts = selectOptions.languages.options
        languageOpts.size() == Language.values().size()

        then: "We get the correct Language default"
        selectOptions.languages.defaultOption.value == Language.DEFAULT_OPTION.name()

        Language.values().each { lang ->
            then: "We can find the ${lang.name()} language"
            languageOpts.find {so -> lang == so.value} != null
        }

        then: "We get all the jdk version options"
        def jdkVersionOpts = selectOptions.jdkVersions.options
        jdkVersionOpts.size() == JdkVersion.values().size()

        then: "We get the correct JdkVersion default"
        selectOptions.jdkVersions.defaultOption.value == JdkVersion.DEFAULT_OPTION

        JdkVersion.values().each { jdkVer ->
            then: "We can find the ${jdkVer.name()} jdk version"
            jdkVersionOpts.find {so -> jdkVer == so.value} != null
        }

        then: "We get all the test framework options"
        def testOpts = selectOptions.testFrameworks.options
        testOpts.size() == TestFramework.values().size()

        then: "We get the correct TestFramework default"
        selectOptions.testFrameworks.defaultOption.value == TestFramework.DEFAULT_OPTION

        TestFramework.values().each { t ->
            then: "We can find the ${t.name()} test framework"
            testOpts.find {so -> t == so.value} != null
        }


        then: "We have all the build tools options"
        def buildOpts = selectOptions.buildTools.options
        buildOpts.size() == BuildTool.values().size()

        then: "We get the correct build tool default"
        selectOptions.buildTools.defaultOption.value == BuildTool.DEFAULT_OPTION

        BuildTool.values().each { t ->
            then: "We can find the ${t.name()} build tool"
            buildOpts.find {so -> t == so.value} != null
        }
    }

    @Client('/select-options')
    static interface SelectOptionsClient {
        @Get('/')
        HttpResponse<SelectOptionsDTO> selectOptions();
    }
}
