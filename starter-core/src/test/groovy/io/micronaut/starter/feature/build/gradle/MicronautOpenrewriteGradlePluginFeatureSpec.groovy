package io.micronaut.starter.feature.build.gradle

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework

class MicronautOpenrewriteGradlePluginFeatureSpec extends BeanContextSpec implements CommandOutputFixture {

    void 'micronaut-openrewrite-gradle plugin added by default'() {
        when:
        Options options = new Options(Language.JAVA,
                TestFramework.JUNIT,
                BuildTool.GRADLE_KOTLIN,
                JdkVersion.JDK_21)
        Map<String, String> output = generate(ApplicationType.DEFAULT, options, [])

        then:
        output.containsKey('build.gradle.kts')
        output.containsKey('gradlew.bat')
        output.containsKey('gradle/wrapper/gradle-wrapper.jar')
        output.containsKey('gradle/wrapper/gradle-wrapper.properties')

        when:
        String buildFile = output.get('build.gradle.kts')

        then:
        buildFile.contains('id("io.micronaut.openrewrite") version "4.5.4"')
    }
}
