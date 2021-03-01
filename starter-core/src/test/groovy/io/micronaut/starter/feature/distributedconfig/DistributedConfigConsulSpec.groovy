package io.micronaut.starter.feature.distributedconfig

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.dependencies.GradleBuild
import io.micronaut.starter.build.dependencies.GradleDsl
import io.micronaut.starter.build.dependencies.MavenBuild
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class DistributedConfigConsulSpec extends BeanContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature discovery-consul contains links to micronaut docs'() {
        when:
        def output = generate(['config-consul'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://www.consul.io")
        readme.contains("https://docs.micronaut.io/latest/guide/index.html#distributedConfigurationConsul")
    }

    @Unroll
    void 'test gradle config-consul feature for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['config-consul'], language), new GradleBuild()).render().toString()

        then:
        template.contains('implementation("io.micronaut.discovery:micronaut-discovery-client")')

        where:
        language << Language.values().toList()
    }

    void 'test gradle config-consul multiple features'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['config-consul', 'discovery-consul']), new GradleBuild()).render().toString()

        then:
        template.count('implementation("io.micronaut.discovery:micronaut-discovery-client")') == 1
    }

    @Unroll
    void 'test maven config-consul feature for language=#language'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['config-consul'], language), new MavenBuild()).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.discovery</groupId>
      <artifactId>micronaut-discovery-client</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

    void 'test maven config-consul multiple features'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['config-consul', 'discovery-consul']), new MavenBuild()).render().toString()

        then:
        template.count("""
    <dependency>
      <groupId>io.micronaut.discovery</groupId>
      <artifactId>micronaut-discovery-client</artifactId>
      <scope>compile</scope>
    </dependency>
""") == 1
    }

    void 'test config-consul configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['config-consul'])

        then:
        commandContext.bootstrapConfiguration.get('micronaut.application.name'.toString()) == 'foo'
        commandContext.bootstrapConfiguration.get('micronaut.config-client.enabled'.toString()) == true
        commandContext.bootstrapConfiguration.get('consul.client.defaultZone') == '${CONSUL_HOST:localhost}:${CONSUL_PORT:8500}'
    }

}
