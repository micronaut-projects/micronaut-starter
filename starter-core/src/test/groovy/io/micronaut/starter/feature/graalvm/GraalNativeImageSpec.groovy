package io.micronaut.starter.feature.graalvm

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class GraalNativeImageSpec extends BeanContextSpec implements CommandOutputFixture {

    @Subject
    @Shared
    GraalNativeImage graalNativeImage = beanContext.getBean(GraalNativeImage)

    @Unroll("feature graalvm works for application type: #applicationType")
    void "feature graalvm works for every type of application type"(ApplicationType applicationType) {
        expect:
        graalNativeImage.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
    }

    void 'test gradle graalvm feature'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(["graalvm"])).render().toString()

        then:
        template.contains('annotationProcessor(enforcedPlatform("io.micronaut:micronaut-bom:\$micronautVersion"))')
        template.contains('annotationProcessor("io.micronaut:micronaut-graal")')
        template.contains('compileOnly(enforcedPlatform("io.micronaut:micronaut-bom:\$micronautVersion"))')
        template.contains('compileOnly("org.graalvm.nativeimage:svm")')

        when:
        template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(["graalvm"], Language.KOTLIN)).render().toString()

        then:
        template.contains('kapt(enforcedPlatform("io.micronaut:micronaut-bom:\$micronautVersion"))')
        template.contains('kapt("io.micronaut:micronaut-graal")')
        template.contains('compileOnly(enforcedPlatform("io.micronaut:micronaut-bom:\$micronautVersion"))')
        template.contains('compileOnly("org.graalvm.nativeimage:svm")')
    }

    void 'graalvm feature not supported for groovy and gradle'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(["graalvm"], Language.GROOVY)).render().toString()

        then:
        !template.contains('annotationProcessor("io.micronaut:micronaut-graal")')
        !template.contains('compileOnly("org.graalvm.nativeimage:svm")')
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
    }

    void 'graalvm feature not supported for Groovy and maven'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(["graalvm"], Language.GROOVY), []).render().toString()

        then:
        !template.contains("""
    <dependency>
      <groupId>org.graalvm.nativeimage</groupId>
      <artifactId>svm</artifactId>
      <scope>provided</scope>
    </dependency>
""")
        !template.contains("""
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-graal</artifactId>
      <scope>provided</scope>
    </dependency>
""")
    }

    @Unroll
    void 'deploy.sh script is created for a function with feature graalvm for language: #language'(Language language) {
        when:
        def output = generate(
                ApplicationType.FUNCTION,
                new Options(language, TestFramework.JUNIT, BuildTool.MAVEN),
                ['graalvm', 'aws-lambda-custom-runtime']
        )
        String deployscript = output['deploy.sh']

        then:
        deployscript
        deployscript.contains('docker build . -t foo')
        deployscript.contains('docker run --rm --entrypoint cat foo  /home/application/function.zip > build/function.zip')

        where:
        language << supportedLanguages()
    }

    @Unroll
    void 'deploy.sh script is not created for an default app with feature graalvm for language: #language'(Language language) {
        when:
        def output = generate(
                ApplicationType.DEFAULT,
                new Options(language, TestFramework.JUNIT, BuildTool.MAVEN),
                ['graalvm']
        )

        then:
        !output.containsKey('deploy.sh')

        where:
        language << supportedLanguages()
    }

    @Unroll
    void 'verify dockerfile for a default application type with maven and feature graalvm for language: #language'(Language language) {
        when:
        def output = generate(
                ApplicationType.DEFAULT,
                new Options(language, TestFramework.JUNIT, BuildTool.MAVEN),
                ['graalvm']
        )
        String dockerfile = output['Dockerfile']

        then:
        dockerfile
        !dockerfile.contains('gradle')
        dockerfile.contains('RUN native-image --no-server -cp target/foo-*.jar')
        dockerfile.contains('COPY --from=graalvm /home/app/foo/foo /app/foo')
        dockerfile.contains('ENTRYPOINT ["/app/foo"]')

        and: 'defaults to graalvm ce jdk8 image'
        dockerfile.contains('FROM oracle/graalvm-ce:20.0.0-java8 as graalvm')
        dockerfile.contains('#FROM oracle/graalvm-ce:20.0.0-java11 as graalvm')

        where:
        language << supportedLanguages()
    }

    @Unroll
    void 'verify dockerfile for a default application type with gradle and feature graalvm for language: #language'(Language language) {
        when:
        def output = generate(
                ApplicationType.DEFAULT,
                new Options(language, TestFramework.JUNIT, BuildTool.GRADLE),
                ['graalvm']
        )
        String dockerfile = output['Dockerfile']

        then:
        dockerfile
        dockerfile.contains('FROM oracle/graalvm-ce:20.0.0-java8 as graalvm')
        dockerfile.contains('RUN native-image --no-server -cp build/libs/foo-*-all.jar')
        dockerfile.contains('COPY --from=graalvm /home/app/foo/foo /app/foo')
        dockerfile.contains('ENTRYPOINT ["/app/foo"]')

        and: 'defaults to graalvm ce jdk8 image'
        dockerfile.contains('FROM oracle/graalvm-ce:20.0.0-java8 as graalvm')
        dockerfile.contains('#FROM oracle/graalvm-ce:20.0.0-java11 as graalvm')

        where:
        language << supportedLanguages()
    }

    @Unroll
    void 'verify native-image.properties for a default application type with gradle and feature graalvm for language: #language'(Language language) {
        when:
        def output = generate(
                ApplicationType.DEFAULT,
                new Options(language, TestFramework.JUNIT, BuildTool.GRADLE),
                ['graalvm']
        )
        String nativeImageProperties = output['src/main/resources/META-INF/native-image/example.micronaut/foo-application/native-image.properties']

        then:
        nativeImageProperties

        nativeImageProperties.contains('-H:IncludeResources=logback.xml|application.yml|bootstrap.yml')
        nativeImageProperties.contains('-H:Name=foo')
        nativeImageProperties.contains('-H:Class=example.micronaut.Application')

        where:
        language << supportedLanguages()
    }

    @Unroll
    void 'Application file is NOT generated for a default application type with gradle and features graalvm & aws-lambda for language: #language'(Language language, String extension) {
        when:
        def output = generate(
                ApplicationType.DEFAULT,
                new Options(language, TestFramework.JUNIT, BuildTool.GRADLE),
                ['graalvm', 'aws-lambda']
        )

        then:
        !output.containsKey("src/main/java/example/micronaut/Application.${extension}".toString())

        where:
        language << supportedLanguages()
        extension << supportedLanguages()*.extension
    }

    @Unroll
    void 'verify native-image.properties for a default application type with gradle and feature graalvm and aws-lambda" for language: #language'(Language language) {
        when:
        def output = generate(
                ApplicationType.DEFAULT,
                new Options(language, TestFramework.JUNIT, BuildTool.GRADLE),
                ['graalvm', 'aws-lambda']
        )

        String nativeImageProperties = output['src/main/resources/META-INF/native-image/example.micronaut/foo-application/native-image.properties']

        then:
        nativeImageProperties

        nativeImageProperties.contains('-H:IncludeResources=logback.xml|application.yml|bootstrap.yml')
        nativeImageProperties.contains('-H:Name=foo')
        nativeImageProperties.contains('-H:Class=io.micronaut.function.aws.runtime.MicronautLambdaRuntime')

        and:
        !output.containsKey("src/main/${language.srcDir}/example/micronaut/BookLambdaRuntime".toString())

        where:
        language << supportedLanguages()
    }

    @Unroll
    void 'verify dockerfile for a function application type with gradle and feature graalvm for language: #language'(Language language) {
        when:
        def output = generate(
                ApplicationType.FUNCTION,
                new Options(language, TestFramework.JUNIT, BuildTool.GRADLE),
                ['graalvm'] // it will uses aws-lambda as its the default feature for function
        )
        String dockerfile = output['Dockerfile']

        then:
        dockerfile
        !dockerfile.contains('maven')
        !dockerfile.contains('mvn')
        dockerfile.contains('FROM gradle:6.3.0-jdk8 as builder')
        dockerfile.contains('RUN /usr/lib/graalvm/bin/native-image --no-server -cp build/libs/foo-*-all.jar')
        dockerfile.contains('RUN chmod 777 bootstrap')
        dockerfile.contains('RUN chmod 777 foo')
        dockerfile.contains('RUN zip -j function.zip bootstrap foo')
        dockerfile.contains('ENTRYPOINT ["/home/application/foo"]')


        and: 'defaults to graalvm ce jdk8 image'
        !dockerfile.contains('FROM oracle/graalvm-ce:20.0.0-java8 as graalvm')
        !dockerfile.contains('#FROM oracle/graalvm-ce:20.0.0-java11 as graalvm')

        where:
        language << supportedLanguages()
    }

    @Unroll
    void 'verify dockerfile for a function application type with maven and feature graalvm for language: #language'() {
        when:
        def output = generate(
                ApplicationType.FUNCTION,
                new Options(language, TestFramework.JUNIT, BuildTool.MAVEN),
                ['graalvm'] // it will uses aws-lambda as its the default feature for function
        )
        String dockerfile = output['Dockerfile']

        then:
        dockerfile
        !dockerfile.contains('gradle')
        dockerfile.contains('FROM maven:3.6.3-openjdk-8')
        dockerfile.contains('RUN /usr/lib/graalvm/bin/native-image --no-server -cp target/foo-*.jar')
        dockerfile.contains('RUN chmod 777 bootstrap')
        dockerfile.contains('RUN chmod 777 foo')
        dockerfile.contains('RUN zip -j function.zip bootstrap foo')
        dockerfile.contains('ENTRYPOINT ["/home/application/foo"]')


        and: 'defaults to graalvm ce jdk8 image'
        !dockerfile.contains('FROM oracle/graalvm-ce:20.0.0-java8 as graalvm')
        !dockerfile.contains('#FROM oracle/graalvm-ce:20.0.0-java11 as graalvm')

        where:
        language << supportedLanguages()
    }

    private List<Language> supportedLanguages() {
        Language.values().toList() - Language.GROOVY
    }

}
