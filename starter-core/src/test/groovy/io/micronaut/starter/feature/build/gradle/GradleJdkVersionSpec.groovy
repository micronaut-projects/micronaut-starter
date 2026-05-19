package io.micronaut.starter.feature.build.gradle

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework

class GradleJdkVersionSpec extends BeanContextSpec  implements CommandOutputFixture {

    void 'java 25 uses gradle 9.5.1'() {
        when:
        Options options = new Options(Language.JAVA,
                TestFramework.JUNIT,
                BuildTool.GRADLE_KOTLIN,
                JdkVersion.JDK_25)
        Map<String, String> output = generate(ApplicationType.DEFAULT, options, [])

        then:
        output.containsKey('gradlew')
        output.containsKey('gradlew.bat')
        output.containsKey('gradle/wrapper/gradle-wrapper.jar')
        output.containsKey('gradle/wrapper/gradle-wrapper.properties')

        when:
        String wrapperProperties = output.get('gradle/wrapper/gradle-wrapper.properties')

        then:
        wrapperProperties.contains('distributionUrl=https\\://services.gradle.org/distributions/gradle-9.5.1-bin.zip')
    }
}
