package io.micronaut.starter.feature.function.awslambda

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
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class AwsLambdaSpec extends BeanContextSpec implements CommandOutputFixture {

    @Shared
    @Subject
    AwsLambda awsLambda = beanContext.getBean(AwsLambda)

    void 'test readme.md with feature aws-lambda contains links to micronaut docs'() {
        when:
        def output = generate(['aws-lambda'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-aws/latest/guide/index.html#lambda")
    }

    void 'test readme.md for application #applicationType feature aws-lambda contains Handler '(ApplicationType applicationType,
                                                                                                String handler) {
        when:
        def output = generate(applicationType, ['aws-lambda'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("[AWS Lambda Handler](https://docs.aws.amazon.com/lambda/latest/dg/java-handler.html)")
        readme.contains("Handler: $handler".toString())

        where:
        applicationType             | handler
        ApplicationType.DEFAULT     | 'io.micronaut.function.aws.proxy.MicronautLambdaHandler'
        ApplicationType.FUNCTION    | 'example.micronaut.BookRequestHandler'
    }

    @Unroll
    void "aws-lambda does not support #description"(ApplicationType applicationType, String description) {
        expect:
        !awsLambda.supports(applicationType)

        where:
        applicationType << ApplicationType.values().toList() -  [ApplicationType.DEFAULT, ApplicationType.FUNCTION]
        description = applicationType.name
    }

    void "aws-lambda supports function application type"(ApplicationType applicationType) {
        expect:
        awsLambda.supports(applicationType)

        where:
        applicationType << [ApplicationType.DEFAULT, ApplicationType.FUNCTION]
    }

    @Unroll
    void 'aws-lambda is the default feature for function for gradle and language=#language'(Language language) {
        when:
        String template = buildGradle.template(ApplicationType.FUNCTION, buildProject(), getFeatures([], language, null, BuildTool.GRADLE, ApplicationType.FUNCTION), false).render().toString()

        then:
        template.contains('implementation("io.micronaut.aws:micronaut-function-aws")')

        where:
        language << Language.values()
    }

    @Unroll
    void 'test gradle aws-lambda feature for language=#language'(Language language) {
        when:
        String template = buildGradle.template(ApplicationType.FUNCTION, buildProject(), getFeatures(['aws-lambda'], language, null, BuildTool.GRADLE, ApplicationType.FUNCTION), false).render().toString()

        then:
        template.contains('implementation("io.micronaut.aws:micronaut-function-aws")')

        where:
        language << Language.values()
    }


    @Unroll
    void 'aws-lambda feature is default feature for function and language=#language'(Language language) {
        when:
        String template = pom.template(ApplicationType.FUNCTION, buildProject(), getFeatures([], language, null, BuildTool.GRADLE, ApplicationType.FUNCTION), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.aws</groupId>
      <artifactId>micronaut-function-aws</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values()
    }

    @Unroll
    void 'function with maven and aws-lambda feature for language=#language'(Language language) {
        when:
        String template = pom.template(ApplicationType.FUNCTION, buildProject(), getFeatures(['aws-lambda'], language, null, BuildTool.GRADLE, ApplicationType.FUNCTION), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.aws</groupId>
      <artifactId>micronaut-function-aws</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values()
    }

    @Unroll
    void 'function with gradle and feature aws-lambda for language=#language'() {
        when:
        def output = generate(
                ApplicationType.FUNCTION,
                new Options(language),
                ['aws-lambda']
        )
        String build = output['build.gradle']
        def readme = output["README.md"]

        then:
        build.contains('implementation("io.micronaut.aws:micronaut-function-aws")')
        !build.contains('implementation "io.micronaut:micronaut-http-server-netty"')
        !build.contains('implementation "io.micronaut:micronaut-http-client"')

        output.containsKey("$srcDir/example/micronaut/Book.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/BookSaved.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/BookRequestHandler.$extension".toString())
        output.containsKey("$testSrcDir/example/micronaut/BookRequestHandlerTest.$extension".toString())

        where:
        language << Language.values().toList()
        extension << Language.extensions()
        srcDir << Language.srcDirs()
        testSrcDir << Language.testSrcDirs()
    }

    @Unroll
    void 'function with maven and feature aws-lambda for language=#language'() {
        when:
        def output = generate(
                ApplicationType.FUNCTION,
                new Options(language, TestFramework.JUNIT, BuildTool.MAVEN),
                ['aws-lambda']
        )
        String build = output['pom.xml']

        then:
        build.contains('<artifactId>micronaut-function-aws</artifactId>')
        !build.contains('<exec.mainClass>')
        !build.contains('</exec.mainClass>')
        !build.contains('<artifactId>micronaut-http-server-netty</artifactId>')

        output.containsKey("$srcDir/example/micronaut/Book.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/BookSaved.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/BookRequestHandler.$extension".toString())
        output.containsKey("$testSrcDir/example/micronaut/BookRequestHandlerTest.$extension".toString())

        where:
        language << Language.values().toList()
        extension << Language.extensions()
        srcDir << Language.srcDirs()
        testSrcDir << Language.testSrcDirs()
    }

    @Unroll
    void 'Application file is generated for a default application type with gradle and features aws-lambda for language: #language'(Language language, String extension) {
        when:
        def output = generate(
                ApplicationType.DEFAULT,
                new Options(language, TestFramework.JUNIT, BuildTool.GRADLE),
                ['aws-lambda']
        )

        then:
        output.containsKey("${language.srcDir}/example/micronaut/Application.${extension}".toString())

        when:
        def buildGradle = output['build.gradle']

        then:
        !buildGradle.contains('id "application"')
        buildGradle.contains('mainClass.set')
        buildGradle.contains('id("io.micronaut.application")')

        where:
        language << Language.values().toList()
        extension << Language.extensions()
    }

    @Unroll
    void 'Application file is generated for a default application type with gradle and features aws-lambda and graalvm for language: #language'(Language language, String extension) {
        when:
        def output = generate(
                ApplicationType.DEFAULT,
                new Options(language, TestFramework.JUNIT, BuildTool.GRADLE, JdkVersion.JDK_11),
                ['aws-lambda', 'graalvm']
        )

        then:
        output.containsKey("${language.srcDir}/example/micronaut/Application.${extension}".toString())

        where:
        language << graalSupportedLanguages()
        extension << graalSupportedLanguages()*.extension
    }

    @Unroll
    void 'Application file is generated for a default application type with gradle and features aws-lambda and aws-lambda-custom-runtime for language: #language'(Language language, String extension) {
        when:
        def output = generate(
                ApplicationType.DEFAULT,
                new Options(language, TestFramework.JUNIT, BuildTool.GRADLE, JdkVersion.JDK_11),
                ['aws-lambda', 'aws-lambda-custom-runtime']
        )

        then:
        !output.containsKey("${language.srcDir}/example/micronaut/Application.${extension}".toString())

        when:
        def buildGradle = output['build.gradle']

        then:
        buildGradle.contains('mainClass.set("io.micronaut.function.aws.runtime.MicronautLambdaRuntime")')

        where:
        language << Language.values().toList()
        extension << Language.extensions()
    }

    @Unroll
    void 'aws-lambda features includes dependency to micronaut-function-aws-api-proxy for function for gradle and language=#language'(Language language) {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['aws-lambda'], language, null, BuildTool.GRADLE, ApplicationType.DEFAULT), false).render().toString()

        then:
        template.contains('runtime("lambda")')
        !template.contains('implementation("io.micronaut:micronaut-http-server-netty")')
        !template.contains('implementation("io.micronaut:micronaut-http-client")')

        where:
        language << Language.values()
    }

    @Unroll
    void 'test maven micronaut-function-aws-api-proxy feature for language=#language'(Language language) {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['aws-lambda'], language, null, BuildTool.GRADLE, ApplicationType.DEFAULT), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.aws</groupId>
      <artifactId>micronaut-function-aws-api-proxy</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values()
    }

    @Unroll
    void 'app with gradle and feature aws-lambda and graalvm applies aws-lambda-custom-runtime for language=#language'() {
        when:
        def output = generate(
                ApplicationType.DEFAULT,
                new Options(language, TestFramework.JUNIT, BuildTool.GRADLE, JdkVersion.JDK_11),
                ['aws-lambda', 'graalvm']
        )
        String build = output['build.gradle']

        then:
        build.contains('runtime("lambda")')
        !build.contains('implementation("io.micronaut.aws:micronaut-function-aws-custom-runtime")')
        !build.contains('implementation "io.micronaut:micronaut-http-server-netty"')
        !build.contains('implementation "io.micronaut:micronaut-http-client"')

        where:
        language << graalSupportedLanguages()
        extension << graalSupportedLanguages()*.extension
        srcDir << graalSupportedLanguages()*.srcDir
        testSrcDir << graalSupportedLanguages()*.testSrcDir
    }

    private List<Language> graalSupportedLanguages() {
        Language.values().toList() - Language.GROOVY
    }

    @Unroll
    void 'app with maven and feature aws-lambda does not apply aws-lambda-custom-runtime for language=#language'() {
        when:
        def output = generate(
                ApplicationType.DEFAULT,
                new Options(language, TestFramework.JUNIT, BuildTool.MAVEN),
                ['aws-lambda']
        )
        String build = output['pom.xml']

        then:
        !build.contains('<artifactId>micronaut-function-aws-custom-runtime</artifactId>')
        build.contains('<exec.mainClass>')
        build.contains('</exec.mainClass>')
        !build.contains('<artifactId>micronaut-http-server-netty</artifactId>')
        build.contains('<artifactId>micronaut-http-client</artifactId>')
        build.contains('<artifactId>micronaut-function-aws-api-proxy</artifactId>')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'app with maven and feature aws-lambda and graalvm applies aws-lambda-custom-runtime for language=#language'() {
        when:
        def output = generate(
                ApplicationType.DEFAULT,
                new Options(language, TestFramework.JUNIT, BuildTool.MAVEN, JdkVersion.JDK_11),
                ['aws-lambda', 'graalvm']
        )
        String build = output['pom.xml']

        then:
        build.contains('<exec.mainClass>io.micronaut.function.aws.runtime.MicronautLambdaRuntime</exec.mainClass>')
        build.contains('<artifactId>micronaut-function-aws-api-proxy</artifactId>')
        build.contains('<artifactId>micronaut-function-aws-custom-runtime</artifactId>')
        !build.contains('<artifactId>micronaut-http-server-netty</artifactId>')
        build.contains('<artifactId>micronaut-http-client</artifactId>')

        where:
        language << graalSupportedLanguages()
    }

    @Unroll
    void 'app with gradle and feature aws-lambda for language=#language'() {
        when:
        def output = generate(
                ApplicationType.DEFAULT,
                new Options(language),
                ['aws-lambda']
        )
        String build = output['build.gradle']

        then:
        build.contains('runtime("lambda")')
        !build.contains('implementation "io.micronaut:micronaut-http-server-netty"')
        !build.contains('implementation "io.micronaut:micronaut-http-client"')

        output.containsKey("$srcDir/example/micronaut/Book.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/BookSaved.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/BookController.$extension".toString())
        output.containsKey("$testSrcDir/example/micronaut/BookControllerTest.$extension".toString())

        where:
        language << Language.values().toList()
        extension << Language.extensions()
        srcDir << Language.srcDirs()
        testSrcDir << Language.testSrcDirs()
    }

    @Unroll
    void 'app with maven and feature aws-lambda for language=#language'() {
        when:
        def output = generate(
                ApplicationType.DEFAULT,
                new Options(language, TestFramework.JUNIT, BuildTool.MAVEN),
                ['aws-lambda']
        )
        String build = output['pom.xml']

        then:
        build.contains('<artifactId>micronaut-function-aws-api-proxy</artifactId>')
        !build.contains('<artifactId>micronaut-http-server-netty</artifactId>')

        output.containsKey("$srcDir/example/micronaut/Book.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/BookSaved.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/BookController.$extension".toString())
        output.containsKey("$testSrcDir/example/micronaut/BookControllerTest.$extension".toString())

        where:
        language << Language.values().toList()
        extension << Language.extensions()
        srcDir << Language.srcDirs()
        testSrcDir << Language.testSrcDirs()
    }
}
