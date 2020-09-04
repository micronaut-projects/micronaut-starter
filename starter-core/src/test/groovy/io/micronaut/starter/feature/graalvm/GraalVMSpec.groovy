package io.micronaut.starter.feature.graalvm

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Requires
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

@Requires({ jvm.isJava8() || jvm.isJava11() })
class GraalVMSpec extends BeanContextSpec implements CommandOutputFixture {

    @Subject
    @Shared
    GraalVM graalNativeImage = beanContext.getBean(GraalVM)

    @Unroll("feature graalvm works for application type: #applicationType")
    void "feature graalvm works for every type of application type"(ApplicationType applicationType) {
        expect:
        graalNativeImage.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
    }

    void 'graalvm feature not supported for groovy and gradle'() {
        when:
        buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(["graalvm"], Language.GROOVY)).render().toString()

        then:
        IllegalArgumentException e = thrown()
        e.message == 'GraalVM is not supported in Groovy applications'
    }

    void 'test maven graalvm feature'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(["graalvm"]), []).render().toString()

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
        template.contains("""
          <compilerArgs>
            <arg>-Amicronaut.processing.group=example.micronaut</arg>
            <arg>-Amicronaut.processing.module=foo</arg>
          </compilerArgs>
""")

        when:
        template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(["graalvm"], Language.KOTLIN), []).render().toString()

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
        template.contains("""
              <annotationProcessorArgs>
                <annotationProcessorArg>micronaut.processing.group=example.micronaut</annotationProcessorArg>
                <annotationProcessorArg>micronaut.processing.module=foo</annotationProcessorArg>
              </annotationProcessorArgs>
""")
    }

    void 'graalvm feature not supported for Groovy and maven'() {
        when:
        pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(["graalvm"], Language.GROOVY), []).render().toString()

        then:
        IllegalArgumentException e = thrown()
        e.message == 'GraalVM is not supported in Groovy applications'
    }

    @Unroll
    void 'it is not possible to use graalvm with JDK versions different than JDK8 through JDK11'(JdkVersion jdkVersion) {
        when:
        generate(
            ApplicationType.DEFAULT,
            new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.GRADLE, jdkVersion),
            ['graalvm']
        )

        then:
        IllegalArgumentException e = thrown()
        e.message == 'GraalVM only supports up to JDK 11'

        where:
        jdkVersion << JdkVersion.values() - [JdkVersion.JDK_8, JdkVersion.JDK_9, JdkVersion.JDK_10, JdkVersion.JDK_11]
    }

    @Unroll
    void 'Application file is NOT generated for a default application type with gradle and features graalvm & aws-lambda for language: #language'(Language language, String extension) {
        when:
        def output = generate(
            ApplicationType.DEFAULT,
            new Options(language, TestFramework.JUNIT, BuildTool.GRADLE, JdkVersion.JDK_11),
            ['graalvm', 'aws-lambda']
        )

        then:
        !output.containsKey("src/main/java/example/micronaut/Application.${extension}".toString())

        where:
        language << supportedLanguages()
        extension << supportedLanguages()*.extension
    }


    private List<Language> supportedLanguages() {
        Language.values().toList() - Language.GROOVY
    }

}
