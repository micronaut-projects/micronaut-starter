package io.micronaut.starter.feature.function

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Unroll

class AzureCloudFunctionSpec extends BeanContextSpec implements CommandOutputFixture {

    @Unroll
    void 'test gradle raw azure function feature for language=#language'() {
        when:
        def output = generate(
                ApplicationType.FUNCTION,
                new Options(language),
                ['azure-function']
        )
        String build = output['build.gradle']
        def readme = output["README.md"]

        then:
        build.contains('id "com.microsoft.azure.azurefunctions" version "1.1.0"')
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
        !output.containsKey("$srcDir/example/micronaut/HelloController.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/Function.$extension".toString())
        output.containsKey("$testSrcDir/example/micronaut/FunctionTest.$extension".toString())

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
                new Options(language),
                ['azure-function']
        )
        String build = output['build.gradle']
        def readme = output["README.md"]

        then:
        build.contains('id "com.microsoft.azure.azurefunctions" version "1.1.0"')
        build.contains('implementation("io.micronaut.azure:micronaut-azure-function-http")')
        build.contains('implementation("com.microsoft.azure.functions:azure-functions-java-library")')
        build.contains('azurefunctions {')
        !build.contains('implementation "io.micronaut:micronaut-http-server-netty"')
        !build.contains('implementation "io.micronaut:micronaut-http-client"')
        !build.contains('"com.github.johnrengelman.shadow"')
        !build.contains('shadowJar')
        readme?.contains("Micronaut and Azure Function")
        output.containsKey("host.json")
        output.containsKey("local.settings.json")
        output.containsKey("$srcDir/example/micronaut/HelloController.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/Function.$extension".toString())
        output.containsKey("$testSrcDir/example/micronaut/HelloFunctionTest.$extension".toString())

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
                new Options(language, TestFramework.JUNIT, BuildTool.MAVEN),
                ['azure-function']
        )
        String build = output['pom.xml']
        def readme = output["README.md"]

        then:
        build.contains('<artifactId>azure-functions-maven-plugin</artifactId>')
        build.contains('<artifactId>azure-functions-java-library</artifactId>')
        !build.contains('<artifactId>micronaut-http-server-netty</artifactId>')
        !build.contains("<artifactId>maven-shade-plugin</artifactId>")
        readme?.contains("Micronaut and Azure Function")
        output.containsKey("host.json")
        output.containsKey("local.settings.json")
        output.containsKey("$srcDir/example/micronaut/HelloController.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/Function.$extension".toString())
        output.containsKey("$testSrcDir/example/micronaut/HelloFunctionTest.$extension".toString())

        where:
        language << Language.values().toList()
        extension << Language.extensions()
        srcDir << Language.srcDirs()
        testSrcDir << Language.testSrcDirs()
    }
}
