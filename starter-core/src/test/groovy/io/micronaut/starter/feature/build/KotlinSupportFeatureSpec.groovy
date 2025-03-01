package io.micronaut.starter.feature.build

import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.BeanContextSpec
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.Language
import io.micronaut.projectgen.core.options.Options

class KotlinSupportFeatureSpec extends BeanContextSpec implements CommandOutputFixture {

    void "test default plugins added for kotlin with buildTool=#buildTool"(BuildTool buildTool) {
        when:
        def output = generate(
                ApplicationType.DEFAULT,
                MicronautOptions.builder().language(Language.KOTLIN).buildTool(buildTool).build(),
                []
        )

        String buildGradle = output[buildTool.getBuildFileName()]
        String pom = output['pom.xml']

        then: 'default for Gradle is Ksp, but for Maven is Kapt'
        if (buildTool.isGradle()) {
            assert buildGradle
            assert buildGradle.contains('id("org.jetbrains.kotlin.jvm")')
            assert buildGradle.contains('id("org.jetbrains.kotlin.plugin.allopen")')
            assert buildGradle.contains('id("com.google.devtools.ksp")')
            assert !buildGradle.contains('id("org.jetbrains.kotlin.kapt")')
        } else if (buildTool == BuildTool.MAVEN) {
            assert pom
            assert pom.contains('<artifactId>kotlin-maven-plugin</artifactId>')
            assert pom.contains('<id>kapt</id>')
            assert pom.contains('<goal>kapt</goal>')
            assert !pom.contains('ksp')
        }

        where:
        buildTool << BuildTool.values()
    }
}