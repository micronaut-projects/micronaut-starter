package io.micronaut.starter.feature.function.oraclefunction

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Unroll

class OracleFunctionSpec extends BeanContextSpec  implements CommandOutputFixture {
    @Unroll
    void 'test gradle google cloud function feature for language=#language'() {
        when:
        def output = generate(
                ApplicationType.DEFAULT,
                new Options(language, TestFramework.JUNIT, BuildTool.GRADLE, JdkVersion.JDK_11),
                ['oracle-function']
        )
        String build = output['build.gradle']
        def readme = output["README.md"]

        then:
        readme
        build.contains('runtime "oracle_function"')
        build.contains('runtimeOnly("org.slf4j:slf4j-simple")')
        output.containsKey("${language.srcDir}/example/micronaut/Application.${extension}".toString())


        where:
        language << Language.values().toList()
        extension << Language.extensions()
        srcDir << Language.srcDirs()
        testSrcDir << Language.testSrcDirs()
    }
}
