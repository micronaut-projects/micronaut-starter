package io.micronaut.starter.feature.test

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.*
import spock.lang.Issue

class SpockSpec extends ApplicationContextSpec {

    void 'test maven configure unit tests'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .testFramework(TestFramework.SPOCK)
                .render()

        then:
        template.contains("""
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <configuration>
          <includes>
            <include>**/*Spec.*</include>
            <include>**/*Test.*</include>
          </includes>
        </configuration>
      </plugin>
""")

    }

    @Issue("https://github.com/micronaut-projects/micronaut-starter/issues/514")
    void 'it is possible to create a #language application with #testFramework and JDK_16'(Language language, TestFramework testFramework) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .jdkVersion(JdkVersion.JDK_16)
                .render()

        then:
        template.contains('''
java {
    sourceCompatibility = JavaVersion.toVersion("16")
    targetCompatibility = JavaVersion.toVersion("16")
}
''')

        where:
        [language, testFramework] << [(Language.values() - Language.KOTLIN), (TestFramework.values() - TestFramework.KOTEST)].combinations()
    }

    @Issue("https://github.com/micronaut-projects/micronaut-starter/issues/514")
    void 'With Kotlin or KoTest and JDK15 the sourceCompatibility is JDK14'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .jdkVersion(JdkVersion.JDK_16)
                .testFramework(testFramework)
                .render()

        then:
        template.contains("sourceCompatibility = JavaVersion.toVersion(\"14\")")
        template.contains('''
        kotlinOptions {
            jvmTarget = "14"
        }
''')

        where:
        language        | testFramework
        Language.KOTLIN | TestFramework.JUNIT
        Language.JAVA   | TestFramework.KOTEST
    }

}
