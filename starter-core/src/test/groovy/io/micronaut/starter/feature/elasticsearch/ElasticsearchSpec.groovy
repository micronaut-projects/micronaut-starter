package io.micronaut.starter.feature.elasticsearch

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.graalvm.GraalVMFeatureValidator
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class ElasticsearchSpec extends ApplicationContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature elasticsearch contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate(['elasticsearch'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-elasticsearch/latest/guide/index.html")
    }

    @Unroll
    void 'test gradle elasticsearch feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['elasticsearch'])
                .language(language)
                .render()

        then:
        template.contains('implementation("io.micronaut.elasticsearch:micronaut-elasticsearch")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven elasticsearch feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['elasticsearch'])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.elasticsearch</groupId>
      <artifactId>micronaut-elasticsearch</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

    void 'test elasticsearch configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['elasticsearch'])

        then:
        commandContext.configuration.get('elasticsearch.httpHosts'.toString()) == 'http://localhost:9200,http://127.0.0.2:9200'
    }

    @Unroll
    void 'test gradle elasticsearch and graalvm features for language=#language'() {
        BuildTool buildTool = BuildTool.GRADLE
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['elasticsearch', 'graalvm'])
                .language(language)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        verifier.hasDependency("org.slf4j", "log4j-over-slf4j", Scope.RUNTIME)
        template.contains('implementation("org.apache.logging.log4j:log4j-api:')
        template.contains('implementation("org.apache.logging.log4j:log4j-core:')

        where:
        language << GraalVMFeatureValidator.supportedLanguages()
    }

    @Unroll
    void 'test maven elasticsearch and graalvm features for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['elasticsearch', 'graalvm'])
                .render()

        then:
        template.contains('''\
    <dependency>
      <groupId>org.slf4j</groupId>
      <artifactId>log4j-over-slf4j</artifactId>''')
        template.contains('''\
    <dependency>
      <groupId>org.apache.logging.log4j</groupId>
      <artifactId>log4j-api</artifactId>''')
        template.contains('''\
    <dependency>
      <groupId>org.apache.logging.log4j</groupId>
      <artifactId>log4j-core</artifactId>
      <version>''')

        where:
        language << GraalVMFeatureValidator.supportedLanguages()
    }
}
