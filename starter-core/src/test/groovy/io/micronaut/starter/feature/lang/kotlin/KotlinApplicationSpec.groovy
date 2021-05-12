package io.micronaut.starter.feature.lang.kotlin

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.awsalexa.AwsAlexa
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.util.VersionInfo
import spock.lang.Issue
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class KotlinApplicationSpec extends BeanContextSpec implements CommandOutputFixture {
    @Shared
    @Subject
    KotlinApplication kotlinApplication = beanContext.getBean(KotlinApplication)

    @Unroll
    void 'Application file is generated for a default application type with #buildTool and language: kotlin and testing framework: #testFramework'(BuildTool buildTool, TestFramework testFramework) {
        when:
        def output = generate(
                ApplicationType.DEFAULT,
                new Options(Language.KOTLIN, testFramework, buildTool),
                []
        )

        then:
        output.containsKey("src/main/kotlin/example/micronaut/Application.${Language.KOTLIN.extension}".toString())

        when:
        def buildGradle = output[buildTool.getBuildFileName()]
        def pom = output['pom.xml']

        then:


        if (buildTool.isGradle()) {
            assert buildGradle
            assert buildGradle.contains('mainClass.set("example.micronaut.ApplicationKt")')
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

    void "test kotlin app gradle build plugins"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(Language.KOTLIN)
                .render()

        then:
        template.contains('id("org.jetbrains.kotlin.jvm") version "1.4.32"')
    }

    @Unroll
    @Issue('https://github.com/micronaut-projects/micronaut-starter/issues/774')
    void 'Do not allow create Kotlin applications with Java 16. buildTool=#buildTool'() {
        when:
        generate(ApplicationType.DEFAULT, new Options(Language.KOTLIN, TestFramework.JUNIT, buildTool, JdkVersion.JDK_16))

        then:
        def e = thrown(IllegalArgumentException)
        e.message == 'Kotlin is not supported with Java 16'

        when:
        generate(ApplicationType.DEFAULT, new Options(Language.KOTLIN, TestFramework.JUNIT, buildTool, JdkVersion.JDK_11))

        then:
        noExceptionThrown()

        when:
        generate(ApplicationType.DEFAULT, new Options(Language.JAVA, TestFramework.JUNIT, buildTool, JdkVersion.JDK_16))

        then:
        noExceptionThrown()

        where:
        buildTool << [BuildTool.MAVEN, BuildTool.GRADLE]
    }
}