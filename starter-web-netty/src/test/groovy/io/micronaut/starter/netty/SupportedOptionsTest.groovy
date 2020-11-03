package io.micronaut.starter.netty

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client
import io.micronaut.starter.api.options.SupportedOptionsDTO
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification
import javax.inject.Inject

@MicronautTest
class SupportedOptionsTest extends Specification {

    @Inject
    SupportedOptionsClient client

    void "Select options are provided"() {
        when: "We ask for the options"
        SupportedOptionsDTO supportedOptions = client.supportedOptions().body()

        then: "We have all the application type options"
        def typeOpts = supportedOptions.types.options
        typeOpts.size() == ApplicationType.values().size()

        then: "There's a matching application type default"
        supportedOptions.types.defaultOption.value ==
                ApplicationType.DEFAULT_OPTION.name().toLowerCase(Locale.ENGLISH); // This Matches DTO impl

        ApplicationType.values().each { t ->
            then: "We can find the ${t.name()} type"
            typeOpts.find {so -> t.name() == so.value} != null
        }

        then: "We get all the languages"
        def languageOpts = supportedOptions.languages.options
        languageOpts.size() == Language.values().size()

        then: "There's a matching Language default"
        supportedOptions.languages.defaultOption.value == Language.DEFAULT_OPTION.name()

        Language.values().each { lang ->
            then: "We can find the ${lang.name()} language"
            languageOpts.find {so -> lang.name() == so.value} != null
        }

        then: "We get all the jdk version options"
        def jdkVersionOpts = supportedOptions.jdkVersions.options
        jdkVersionOpts.size() == JdkVersion.values().size()

        then: "There's a matching JdkVersion default"
        supportedOptions.jdkVersions.defaultOption.value == JdkVersion.DEFAULT_OPTION.name()

        JdkVersion.values().each { jdkVer ->
            then: "We can find the ${jdkVer.name()} jdk version"
            jdkVersionOpts.find {so -> jdkVer.name() == so.value} != null
        }

        then: "We get all the test framework options"
        def testOpts = supportedOptions.testFrameworks.options
        testOpts.size() == TestFramework.values().size()

        then: "There's a matching TestFramework default"
        supportedOptions.testFrameworks.defaultOption.value == TestFramework.DEFAULT_OPTION.name()

        TestFramework.values().each { t ->
            then: "We can find the ${t.name()} test framework"
            testOpts.find {so -> t.name() == so.value} != null
        }


        then: "We have all the build tools options"
        def buildOpts = supportedOptions.buildTools.options
        buildOpts.size() == BuildTool.values().size()

        then: "There's a matching build tool default"
        supportedOptions.buildTools.defaultOption.value == BuildTool.DEFAULT_OPTION.name()

        BuildTool.values().each { t ->
            then: "We can find the ${t.name()} build tool"
            buildOpts.find {so -> t.name() == so.value} != null
        }
    }

    @Client('/supported-options')
    static interface SupportedOptionsClient {
        @Get('/')
        HttpResponse<SupportedOptionsDTO> supportedOptions();
    }
}
