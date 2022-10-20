package io.micronaut.starter.feature.function.oraclefunction

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.*

class OracleFunctionSpec extends BeanContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature oracle-function contains links to docs'() {
        when:
        Options options = new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.GRADLE, JdkVersion.JDK_8)
        def output = generate(appType, options, ['oracle-function'])
        String readme = output["README.md"]

        then:
        readme
        verifyAll {
            readme.contains("https://micronaut-projects.github.io/micronaut-oracle-cloud/latest/guide/#functions")
            readme.contains("https://docs.cloud.oracle.com/iaas/Content/Functions/Concepts/functionsoverview.htm")
        }

        when:
        readme = readme.replaceFirst("## Feature oracle-function documentation","")

        then:
        // make sure we didn't add docs more than once
        !readme.contains("## Feature oracle-function documentation")

        where:
        appType << [ApplicationType.DEFAULT, ApplicationType.FUNCTION]
    }

    void 'test gradle oracle cloud function feature for language=#language (using serde #useSerde)'(Language language, boolean useSerde) {
        when:
        def output = generate(
                ApplicationType.DEFAULT,
                new Options(language, TestFramework.JUNIT, BuildTool.GRADLE, JdkVersion.JDK_11),
                ['oracle-function'] + (useSerde ? ['serialization-jackson'] : [])
        )
        def readme = output["README.md"]
        def funcYaml = output["func.yml"]

        then:
        readme
        funcYaml

        output.containsKey("${language.srcDir}/example/micronaut/Application.${language.extension}".toString())
        output["${language.srcDir}/example/micronaut/FooController.${language.extension}".toString()]
                .contains("class FooController {")
        useSerde == output."${language.srcDir}/example/micronaut/FooController.${language.extension}".contains("@Serdeable")
        !useSerde == output."${language.srcDir}/example/micronaut/FooController.${language.extension}".contains("@Introspected")

        output["${language.testSrcDir}/example/micronaut/FooControllerTest.${language.extension}".toString()]
                .contains("class FooControllerTest")

        where:
        [language, useSerde] << [Language.values().toList(), [true, false]].combinations()
    }

    void "runtime for gradle and oracle-function"() {
        when:
        String build = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['oracle-function'])
                .testFramework(TestFramework.JUNIT)
                .jdkVersion(JdkVersion.JDK_11)
                .render()

        then:
        build.contains('runtime("oracle_function")')
        build.contains('runtimeOnly("org.slf4j:slf4j-simple")')

        where:
        language << Language.values().toList()
    }

    void 'test maven oracle cloud function feature for language=#language (using serde #useSerde)'(Language language, boolean useSerde) {
        when:
        def output = generate(
                ApplicationType.DEFAULT,
                new Options(language, TestFramework.JUNIT, BuildTool.MAVEN, JdkVersion.JDK_11),
                ['oracle-function'] + (useSerde ? ['serialization-jackson'] : [])
        )
        String build = output['pom.xml']
        def readme = output["README.md"]
        def funcYaml = output["func.yml"]

        then:
        readme
        funcYaml
        build.contains('<micronaut.runtime>oracle_function</micronaut.runtime>')

        build.contains('''
    <dependency>
      <groupId>org.slf4j</groupId>
      <artifactId>slf4j-simple</artifactId>
      <scope>runtime</scope>
    </dependency>''')

        output.containsKey("${language.srcDir}/example/micronaut/Application.${language.extension}".toString())

        output["${language.srcDir}/example/micronaut/FooController.${language.extension}".toString()]
                .contains("class FooController {")
        useSerde == output."${language.srcDir}/example/micronaut/FooController.${language.extension}".contains("@Serdeable")
        !useSerde == output."${language.srcDir}/example/micronaut/FooController.${language.extension}".contains("@Introspected")

        output["${language.testSrcDir}/example/micronaut/FooControllerTest.${language.extension}".toString()]
                .contains("class FooControllerTest")

        where:
        [language, useSerde] << [Language.values().toList(), [true, false]].combinations()
    }

    void 'test maven oracle cloud function app for language=#language (using serde #useSerde)'(Language language, boolean useSerde) {
        when:
        def output = generate(
                ApplicationType.FUNCTION,
                new Options(language, TestFramework.JUNIT, BuildTool.MAVEN, JdkVersion.JDK_11),
                ['oracle-function'] + (useSerde ? ['serialization-jackson'] : [])
        )
        String build = output['pom.xml']

        then:
        !output.containsKey("${language.srcDir}/example/micronaut/FooController.${language.extension}")

        build.contains('<micronaut.runtime>oracle_function</micronaut.runtime>')
        build.contains('<jib.docker.tag>${project.version}</jib.docker.tag>')
        build.contains('<exec.mainClass>com.fnproject.fn.runtime.EntryPoint</exec.mainClass>')
        build.contains('<jib.docker.image>[REGION].ocir.io/[TENANCY]/[REPO]/${project.artifactId}</jib.docker.image>')
        build.contains('<function.entrypoint>example.micronaut.Function::handleRequest</function.entrypoint>')
        build.contains('''
          <configuration>
            <nativeImageBuildArgs>
              <arg>-H:+StaticExecutableWithDynamicLibC</arg>
              <arg>-Dfn.handler=${function.entrypoint}</arg>
              <arg>--initialize-at-build-time=example.micronaut</arg>
            </nativeImageBuildArgs>
            <appArguments>
              <arg>${function.entrypoint}</arg>
            </appArguments>
          </configuration>''')

        build.contains('''
        <configuration>
          <to>
            <image>${jib.docker.image}:${jib.docker.tag}</image>
          </to>
          <container>
            <args>${function.entrypoint}</args>
            <mainClass>${exec.mainClass}</mainClass>
          </container>
        </configuration>''')

        build.contains('''
    <dependency>
      <groupId>com.fnproject.fn</groupId>
      <artifactId>api</artifactId>
      <scope>compile</scope>
    </dependency>''')

        build.contains('''
    <dependency>
      <groupId>com.fnproject.fn</groupId>
      <artifactId>runtime</artifactId>
      <scope>runtime</scope>
    </dependency>''')

        build.contains('''
    <dependency>
      <groupId>com.fnproject.fn</groupId>
      <artifactId>testing-junit4</artifactId>
      <scope>test</scope>
    </dependency>''')

        where:
        [language, useSerde] << [Language.values().toList(), [true, false]].combinations()
    }
}
