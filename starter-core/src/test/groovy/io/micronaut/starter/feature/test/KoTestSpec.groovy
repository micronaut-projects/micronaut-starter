package io.micronaut.starter.feature.test

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.TestFramework

class KoTestSpec extends ApplicationContextSpec {

    void 'test maven configure unit tests'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .testFramework(TestFramework.KOTEST)
                .render()

        then:
        template.contains('''
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <configuration>
          <includes>
            <include>**/*Spec.*</include>
            <include>**/*Test.*</include>
          </includes>
        </configuration>
        <dependencies>
          <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-engine</artifactId>
            <version>${junit5.version}</version>
          </dependency>
        </dependencies>
      </plugin>
''')
    }

    void 'test maven configure unit tests for Kotest5'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .testFramework(TestFramework.KOTEST)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.MAVEN, template)

        then:
        verifier.hasDependency("io.kotest", "kotest-assertions-core-jvm", Scope.TEST)
        verifier.hasDependency("io.kotest", "kotest-runner-junit5-jvm", Scope.TEST)
        verifier.hasDependency("io.micronaut.test", "micronaut-test-kotest5", Scope.TEST)
        verifier.hasDependency("io.mockk", "mockk", Scope.TEST)
    }

    void 'test gradle configure unit tests for Kotest5'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .testFramework(TestFramework.KOTEST)
                .render()

        then:
        template.contains('testRuntime("kotest5")')
    }
}
