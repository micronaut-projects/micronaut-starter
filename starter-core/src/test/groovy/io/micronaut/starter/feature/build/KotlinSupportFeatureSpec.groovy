package io.micronaut.starter.feature.build

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options

class KotlinSupportFeatureSpec extends BeanContextSpec implements CommandOutputFixture {

    void "test default plugins added for kotlin with buildTool=#buildTool"(BuildTool buildTool) {
        when:
        def output = generate(
                ApplicationType.DEFAULT,
                new Options(Language.KOTLIN, buildTool),
                []
        )

        String buildGradle = output[buildTool.getBuildFileName()]
        String pom = output['pom.xml']

        then:
        if (buildTool.isGradle()) {
            buildGradle
            buildGradle.contains('id("org.jetbrains.kotlin.jvm")')
            buildGradle.contains('id("org.jetbrains.kotlin.allopen")')
            buildGradle.contains('id("com.google.devtools.ksp")')
            !buildGradle.contains('id("org.jetbrains.kotlin.kapt")')
        } else if (buildTool == BuildTool.MAVEN) {
            pom
            pom.contains('<artifactId>kotlin-maven-plugin</artifactId>')
            pom.contains('<id>kapt</id>')
            pom.contains('<goal>kapt</goal>')
            !pom.contains('ksp')
        }

        where:
        buildTool << BuildTool.values()
    }
}