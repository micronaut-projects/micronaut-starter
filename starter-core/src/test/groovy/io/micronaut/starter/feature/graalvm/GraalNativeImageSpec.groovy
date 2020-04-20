package io.micronaut.starter.feature.graalvm

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language

class GraalNativeImageSpec extends BeanContextSpec {

    void 'test gradle graalvm feature'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(["graalvm"])).render().toString()

        then:
        template.contains('annotationProcessor platform("io.micronaut:micronaut-bom:\$micronautVersion")')
        template.contains('annotationProcessor "io.micronaut:micronaut-graal"')
        template.contains('compileOnly platform("io.micronaut:micronaut-bom:\$micronautVersion")')
        template.contains('compileOnly "org.graalvm.nativeimage:svm"')

        when:
        template = buildGradle.template(buildProject(), getFeatures(["graalvm"], Language.kotlin)).render().toString()

        then:
        template.contains('kapt platform("io.micronaut:micronaut-bom:\$micronautVersion")')
        template.contains('kapt "io.micronaut:micronaut-graal"')
        template.contains('compileOnly platform("io.micronaut:micronaut-bom:\$micronautVersion")')
        template.contains('compileOnly "org.graalvm.nativeimage:svm"')

        when:
        template = buildGradle.template(buildProject(), getFeatures(["graalvm"], Language.groovy)).render().toString()

        then:
        template.count('compileOnly platform("io.micronaut:micronaut-bom:\$micronautVersion")') == 1
        template.contains('compileOnly "org.graalvm.nativeimage:svm"')
    }

    void 'test maven graalvm feature'() {
        when:
        String template = pom.template(buildProject(), getFeatures(["graalvm"]), []).render().toString()

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
        template = pom.template(buildProject(), getFeatures(["graalvm"], Language.kotlin), []).render().toString()

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
        template = pom.template(buildProject(), getFeatures(["graalvm"], Language.groovy), []).render().toString()

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
