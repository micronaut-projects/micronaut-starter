package io.micronaut.starter.feature.test

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
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
    void 'it is possible to create a #language application with #testFramework and JDK_17'(Language language, TestFramework testFramework) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .jdkVersion(JdkVersion.JDK_17)
                .render()

        then:
        template.contains('''
java {
    sourceCompatibility = JavaVersion.toVersion("17")
    targetCompatibility = JavaVersion.toVersion("17")
}
''')

        where:
        [language, testFramework] << [(Language.values() - Language.KOTLIN), (TestFramework.values() - TestFramework.KOTEST)].combinations()
    }

    @Issue("https://github.com/micronaut-projects/micronaut-starter/issues/514")
    void 'With #language, #testFramework and JDK17 the sourceCompatibility is JDK17'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .jdkVersion(JdkVersion.JDK_17)
                .testFramework(testFramework)
                .render()

        then:
        template.contains('sourceCompatibility = JavaVersion.toVersion("17")')

        and: 'since Kotlin 1.9.20 we do not require jvmTarget anymore'
        !template.contains('''\
tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile).configureEach {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
    }
}''')

        where:
        language        | testFramework
        Language.KOTLIN | TestFramework.JUNIT
        Language.JAVA   | TestFramework.KOTEST
    }

    void 'With #language, #testFramework and #jdk and kapt we use the kotlin toolchain for JDK17'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['kapt'])
                .jdkVersion(jdk)
                .testFramework(testFramework)
                .render()

        then:
        template.contains('sourceCompatibility = JavaVersion.toVersion("17")')

        template.contains('''\
kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}''')
        !template.contains('''\
tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile).configureEach {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
    }
}''')

        where:
        language        | testFramework        | jdk
        Language.KOTLIN | TestFramework.JUNIT  | JdkVersion.JDK_17
        Language.KOTLIN | TestFramework.JUNIT  | JdkVersion.JDK_21
        Language.JAVA   | TestFramework.KOTEST | JdkVersion.JDK_17
        Language.JAVA   | TestFramework.KOTEST | JdkVersion.JDK_21
    }

    void 'test spock with Maven applies gmavenplus plugin'() {
        given:
        Language language = Language.JAVA
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .testFramework(TestFramework.SPOCK)
                .render()

        then:
        template.contains('''\
      <plugin>
        <groupId>org.codehaus.gmavenplus</groupId>
        <artifactId>gmavenplus-plugin</artifactId>
      </plugin>
''')
    }

}
