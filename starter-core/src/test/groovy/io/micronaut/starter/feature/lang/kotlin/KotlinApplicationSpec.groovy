package io.micronaut.starter.feature.lang.kotlin

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.awsalexa.AwsAlexa
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.util.VersionInfo
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class KotlinApplicationSpec extends BeanContextSpec implements CommandOutputFixture {
    @Shared
    @Subject
    KotlinApplication kotlinApplication = beanContext.getBean(KotlinApplication)

    @Unroll
    void 'Application file is generated for a default application type with #buildTool and language: kotlin and testing framework: #testFramework'(BuildTool buildTool, TestFramework testFramework) {
        given:
        def policy = VersionInfo.isMicronautSnapshot() ? "enforcedPlatform" : "platform"
        def testPolicy = "enforcedPlatform"

        when:
        def output = generate(
                ApplicationType.DEFAULT,
                new Options(Language.KOTLIN, testFramework, buildTool),
                []
        )

        then:
        output.containsKey("src/main/kotlin/example/micronaut/Application.${Language.KOTLIN.extension}".toString())

        when:
        def buildGradle = output['build.gradle']
        def pom = output['pom.xml']

        then:


        if (buildTool == BuildTool.GRADLE) {
            assert buildGradle
            assert buildGradle.contains('mainClassName = "example.micronaut.ApplicationKt"')
            assert buildGradle.contains('implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")')

        } else if (buildTool == BuildTool.MAVEN) {
            assert pom.contains("""
    <dependency>
      <groupId>io.micronaut.kotlin</groupId>
      <artifactId>micronaut-kotlin-runtime</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        }

        where:
        [buildTool, testFramework] << [BuildTool.values(), [TestFramework.KOTEST]].combinations()
    }

    @Unroll
    void "kotlin-application does not support #description"(ApplicationType applicationType, String description) {
        expect:
        !kotlinApplication.supports(applicationType)

        where:
        applicationType << [
                ApplicationType.CLI,
                ApplicationType.FUNCTION
        ]
        description = applicationType.name
    }

    @Unroll
    void "kotlin-application supports #description application type"() {
        expect:
        kotlinApplication.supports(applicationType)

        where:
        applicationType << ApplicationType.values().toList() - [
                ApplicationType.CLI,
                ApplicationType.FUNCTION
        ]
        description = applicationType.name
    }
}
