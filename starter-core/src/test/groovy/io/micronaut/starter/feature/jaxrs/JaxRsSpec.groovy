package io.micronaut.starter.feature.jaxrs

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class JaxRsSpec extends ApplicationContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature jax-rs contains links to micronaut docs'() {
        when:
        def output = generate([JaxRs.NAME])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-jaxrs/latest/guide/index.html")
    }

    @Unroll
    void 'test jax-rs with Gradle for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features([JaxRs.NAME, 'kapt'])
                .language(language)
                .render()

        then:
        template.contains('implementation("io.micronaut.jaxrs:micronaut-jaxrs-server")')
        template.contains("$scope(\"io.micronaut.jaxrs:micronaut-jaxrs-processor\")")

        where:
        language        | scope
        Language.JAVA   | "annotationProcessor"
        Language.KOTLIN | "kapt"
        Language.GROOVY | "compileOnly"
    }

    void 'test maven jax-rs feature'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features([JaxRs.NAME])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.jaxrs</groupId>
      <artifactId>micronaut-jaxrs-server</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
            <path>
              <groupId>io.micronaut.jaxrs</groupId>
              <artifactId>micronaut-jaxrs-processor</artifactId>
              <version>\${micronaut.jaxrs.version}</version>
              <exclusions>
                <exclusion>
                  <groupId>io.micronaut</groupId>
                  <artifactId>micronaut-inject</artifactId>
                </exclusion>
              </exclusions>
            </path>
""")

        when:
        template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(Language.KOTLIN)
                .features([JaxRs.NAME])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.jaxrs</groupId>
      <artifactId>micronaut-jaxrs-server</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.count('''\
               <annotationProcessorPath>
                 <groupId>io.micronaut.jaxrs</groupId>
                 <artifactId>micronaut-jaxrs-processor</artifactId>
                 <version>${micronaut.jaxrs.version}</version>
               </annotationProcessorPath>
''') == 2

        when:
        template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(Language.GROOVY)
                .features([JaxRs.NAME])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.jaxrs</groupId>
      <artifactId>micronaut-jaxrs-server</artifactId>
      <scope>compile</scope>
    </dependency>
""")

    }
}
