package io.micronaut.starter.feature.graalvm

import io.micronaut.context.BeanContext
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.ContextFixture
import io.micronaut.starter.fixture.ProjectFixture
import io.micronaut.starter.options.Language
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class GraalNativeImageSpec extends Specification implements ProjectFixture, ContextFixture {

    @Shared
    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    void 'test gradle graal-native-image feature'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(["graal-native-image"])).render().toString()

        then:
        template.contains('annotationProcessor platform("io.micronaut:micronaut-bom:\$micronautVersion")')
        template.contains('annotationProcessor "io.micronaut:micronaut-graal"')
        template.contains('compileOnly platform("io.micronaut:micronaut-bom:\$micronautVersion")')
        template.contains('compileOnly "org.graalvm.nativeimage:svm"')

        when:
        template = buildGradle.template(buildProject(), getFeatures(["graal-native-image"], Language.kotlin)).render().toString()

        then:
        template.contains('kapt platform("io.micronaut:micronaut-bom:\$micronautVersion")')
        template.contains('kapt "io.micronaut:micronaut-graal"')
        template.contains('compileOnly platform("io.micronaut:micronaut-bom:\$micronautVersion")')
        template.contains('compileOnly "org.graalvm.nativeimage:svm"')

        when:
        template = buildGradle.template(buildProject(), getFeatures(["graal-native-image"], Language.groovy)).render().toString()

        then:
        template.count('compileOnly platform("io.micronaut:micronaut-bom:\$micronautVersion")') == 1
        template.contains('compileOnly "org.graalvm.nativeimage:svm"')
    }

    void 'test maven graal-native-image feature'() {
        when:
        String template = pom.template(buildProject(), getFeatures(["graal-native-image"]), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>org.graalvm.nativeimage</groupId>
      <artifactId>svm</artifactId>
      <scope>provided</scope>
    </dependency>
""")
        template.contains("""
            <path>
                <groupId>io.micronaut</groupId>
                <artifactId>micronaut-graal</artifactId>
                <version>\${micronaut.version}</version>
            </path>
""")

        when:
        template = pom.template(buildProject(), getFeatures(["graal-native-image"], Language.kotlin), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>org.graalvm.nativeimage</groupId>
      <artifactId>svm</artifactId>
      <scope>provided</scope>
    </dependency>
""")
        template.contains("""
                <annotationProcessorPath>
                  <groupId>io.micronaut</groupId>
                  <artifactId>micronaut-graal</artifactId>
                  <version>\${micronaut.version}</version>
                </annotationProcessorPath>
""")

        when:
        template = pom.template(buildProject(), getFeatures(["graal-native-image"], Language.groovy), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>org.graalvm.nativeimage</groupId>
      <artifactId>svm</artifactId>
      <scope>provided</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-graal</artifactId>
      <scope>compile</scope>
    </dependency>
""")
    }

}
