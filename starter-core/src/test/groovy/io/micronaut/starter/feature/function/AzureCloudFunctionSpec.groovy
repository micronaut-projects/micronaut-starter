package io.micronaut.starter.feature.function

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Unroll

class AzureCloudFunctionSpec extends BeanContextSpec implements CommandOutputFixture {

    @Unroll
    void "#jdkVersion not supported for #feature"(ApplicationType applicationType, JdkVersion jdkVersion, String feature) {
        when:
        generate(
                applicationType,
                new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.GRADLE, jdkVersion),
                [feature],
        )
        then:
        IllegalArgumentException e = thrown()
        e.message == 'The Azure Functions runtime only supports Java SE 8 LTS (zulu8.31.0.2-jre8.0.181-win_x64).'

        where:
        [applicationType, jdkVersion, feature] << [
                [ApplicationType.FUNCTION, ApplicationType.DEFAULT],
                ((JdkVersion.values() as List<JdkVersion>) - [JdkVersion.JDK_8]) as List<JdkVersion>,
                ['azure-function']
        ].combinations()
    }

    @Unroll
    void 'test gradle raw azure function feature for language=#language'() {
        when:
        def output = generate(
                ApplicationType.FUNCTION,
                new Options(language, TestFramework.JUNIT, BuildTool.GRADLE, JdkVersion.JDK_8),
                ['azure-function']
        )
        String build = output['build.gradle']
        def readme = output["README.md"]

        then:
        build.contains('id "com.microsoft.azure.azurefunctions"')
        !build.contains('implementation("io.micronaut:micronaut-function-aws")')
        !build.contains('implementation("io.micronaut.azure:micronaut-azure-function-http")')
        build.contains('implementation("io.micronaut.azure:micronaut-azure-function")')
        build.contains('implementation("com.microsoft.azure.functions:azure-functions-java-library")')
        build.contains('azurefunctions {')
        !build.contains('implementation "io.micronaut:micronaut-http-server-netty"')
        !build.contains('implementation "io.micronaut:micronaut-http-client"')
        !build.contains('"com.github.johnrengelman.shadow"')
        !build.contains('shadowJar')
        readme?.contains("Micronaut and Azure Function")
        output.containsKey("host.json")
        output.containsKey("local.settings.json")
        !output.containsKey("$srcDir/example/micronaut/Book.$extension".toString())
        !output.containsKey("$srcDir/example/micronaut/FooController.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/Function.$extension".toString())
        output.get("$srcDir/example/micronaut/Function.$extension".toString())
                .contains(" AzureFunction")
        output.containsKey("$testSrcDir/example/micronaut/FunctionTest.$extension".toString())
        output.get("$testSrcDir/example/micronaut/FunctionTest.$extension".toString())
                .contains("function.echo")

        where:
        language << Language.values().toList()
        extension << Language.extensions()
        srcDir << Language.srcDirs()
        testSrcDir << Language.testSrcDirs()
    }

    @Unroll
    void 'test gradle azure function feature for language=#language'() {
        when:
        def output = generate(
                ApplicationType.DEFAULT,
                new Options(language, TestFramework.JUNIT, BuildTool.GRADLE, JdkVersion.JDK_8),
                ['azure-function']
        )
        String build = output['build.gradle']
        def readme = output["README.md"]

        then:
        build.contains('id "com.microsoft.azure.azurefunctions"')
        build.contains('runtime "azure_function"')
        build.contains('azurefunctions {')
        !build.contains('implementation "io.micronaut:micronaut-http-server-netty"')
        !build.contains('implementation "io.micronaut:micronaut-http-client"')
        !build.contains('"com.github.johnrengelman.shadow"')
        !build.contains('shadowJar')
        output.containsKey("${language.srcDir}/example/micronaut/Application.${extension}".toString())

        readme?.contains("Micronaut and Azure Function")
        output.containsKey("host.json")
        output.containsKey("local.settings.json")
        output.containsKey("$srcDir/example/micronaut/FooController.$extension".toString())
        output.get("$srcDir/example/micronaut/Function.$extension".toString())
                .contains(" AzureHttpFunction")
        output.containsKey("$srcDir/example/micronaut/Function.$extension".toString())
        output.containsKey("$testSrcDir/example/micronaut/FooFunctionTest.$extension".toString())
        output.get("$testSrcDir/example/micronaut/FooFunctionTest.$extension".toString())
                .contains("HttpRequestMessageBuilder")
        where:
        language << Language.values().toList()
        extension << Language.extensions()
        srcDir << Language.srcDirs()
        testSrcDir << Language.testSrcDirs()
    }

    @Unroll
    void 'test gradle azure function feature for language=#language - maven'() {
        when:
        def output = generate(
                ApplicationType.DEFAULT,
                new Options(language, TestFramework.JUNIT, BuildTool.MAVEN, JdkVersion.JDK_8),
                ['azure-function']
        )
        String build = output['pom.xml']
        def readme = output["README.md"]

        then:
        build.contains('<artifactId>azure-functions-maven-plugin</artifactId>')
        build.contains('<artifactId>azure-functions-java-library</artifactId>')
        !build.contains('<artifactId>micronaut-http-server-netty</artifactId>')
        !build.contains("<artifactId>maven-shade-plugin</artifactId>")
        !output.containsKey("${language.srcDir}/example/micronaut/Application.${extension}".toString())

        readme?.contains("Micronaut and Azure Function")
        output.containsKey("host.json")
        output.containsKey("local.settings.json")
        output.containsKey("$srcDir/example/micronaut/FooController.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/Function.$extension".toString())
        output.containsKey("$testSrcDir/example/micronaut/FooFunctionTest.$extension".toString())
        output.get("$testSrcDir/example/micronaut/FooFunctionTest.$extension".toString())
              .contains("HttpRequestMessageBuilder")

        where:
        language << Language.values().toList()
        extension << Language.extensions()
        srcDir << Language.srcDirs()
        testSrcDir << Language.testSrcDirs()
    }
}
