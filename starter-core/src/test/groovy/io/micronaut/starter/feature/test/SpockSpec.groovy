package io.micronaut.starter.feature.test

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Features
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Issue

class SpockSpec extends BeanContextSpec {

    void 'test maven configure unit tests'() {
        given:
        Features features = getFeatures([], null, TestFramework.SPOCK, BuildTool.MAVEN)

        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), features, []).render().toString()

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
    void 'it is possible to create a #language application with #testFramework and JDK15'() {
        given:
        Options options = new Options(language, testFramework, BuildTool.GRADLE, JdkVersion.JDK_15)

        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures([], options)).render().toString()

        then:
        template.contains('''
java {
    sourceCompatibility = JavaVersion.toVersion('15')
    targetCompatibility = JavaVersion.toVersion('15')
}
''')

        where:
        [language, testFramework] << [(Language.values() - Language.KOTLIN), (TestFramework.values() - TestFramework.KOTEST)].combinations()
    }

    @Issue("https://github.com/micronaut-projects/micronaut-starter/issues/514")
    void 'With Kotlin or KoTest and JDK15 the sourceCompatibility is JDK14'() {
        given:
        Options options = new Options(language, testFramework, BuildTool.GRADLE, JdkVersion.JDK_15)

        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures([], options)).render().toString()

        then:
        template.contains("sourceCompatibility = JavaVersion.toVersion('14')")
        template.contains('''
    kotlinOptions {
        jvmTarget = '14'
    }
''')

        where:
        language        | testFramework
        Language.KOTLIN | TestFramework.JUNIT
        Language.JAVA   | TestFramework.KOTEST
    }

}
