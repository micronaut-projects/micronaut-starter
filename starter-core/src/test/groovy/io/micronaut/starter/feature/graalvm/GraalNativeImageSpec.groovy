package io.micronaut.starter.feature.graalvm

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class GraalNativeImageSpec extends BeanContextSpec {

    @Subject
    @Shared
    GraalNativeImage graalNativeImage = beanContext.getBean(GraalNativeImage)

    @Unroll("feature graalvm works for application type: #description")
    void "feature graalvm works for every type of application type"(ApplicationType applicationType,
                                                                    String description) {
        expect:
        graalNativeImage.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
        description = applicationType.name
    }

    void 'test gradle graalvm feature'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(["graalvm"])).render().toString()

        then:
        template.contains('annotationProcessor(platform("io.micronaut:micronaut-bom:\$micronautVersion"))')
        template.contains('annotationProcessor("io.micronaut:micronaut-graal")')
        template.contains('compileOnly(platform("io.micronaut:micronaut-bom:\$micronautVersion"))')
        template.contains('compileOnly("org.graalvm.nativeimage:svm")')

        when:
        template = buildGradle.template(buildProject(), getFeatures(["graalvm"], Language.KOTLIN)).render().toString()

        then:
        template.contains('kapt(platform("io.micronaut:micronaut-bom:\$micronautVersion"))')
        template.contains('kapt("io.micronaut:micronaut-graal")')
        template.contains('compileOnly(platform("io.micronaut:micronaut-bom:\$micronautVersion"))')
        template.contains('compileOnly("org.graalvm.nativeimage:svm")')

        when:
        template = buildGradle.template(buildProject(), getFeatures(["graalvm"], Language.GROOVY)).render().toString()

        then:
        template.count('compileOnly(platform("io.micronaut:micronaut-bom:\$micronautVersion"))') == 1
        template.contains('compileOnly("org.graalvm.nativeimage:svm")')
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
        template = pom.template(buildProject(), getFeatures(["graalvm"], Language.KOTLIN), []).render().toString()

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
        template = pom.template(buildProject(), getFeatures(["graalvm"], Language.GROOVY), []).render().toString()

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
      <scope>provided</scope>
    </dependency>
""")
    }

}
