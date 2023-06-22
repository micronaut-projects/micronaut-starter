package io.micronaut.starter.feature.dev

import io.micronaut.context.env.Environment
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language

class ControlPanelSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature control-panel contains links to micronaut docs'() {
        when:
        def output = generate([ControlPanel.NAME])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-control-panel/latest/guide/index.html")
    }

    void 'test gradle control-panel feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features([ControlPanel.NAME])
                .render()

        then:
        template.contains('developmentOnly("io.micronaut.controlpanel:micronaut-control-panel-ui")')

        where:
        language << Language.values().toList()
    }

    void 'test gradle control-panel with management feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features([ControlPanel.NAME, "management"])
                .render()

        then:
        template.contains('developmentOnly("io.micronaut.controlpanel:micronaut-control-panel-ui")')
        template.contains('developmentOnly("io.micronaut.controlpanel:micronaut-control-panel-management")')

        where:
        language << Language.values().toList()
    }

    void 'test maven control-panel feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['control-panel'])
                .render()
        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.controlpanel</groupId>
      <artifactId>micronaut-control-panel-ui</artifactId>
      <scope>provided</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

    void 'test maven control-panel with management feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features([ControlPanel.NAME, "management"])
                .render()
        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.controlpanel</groupId>
      <artifactId>micronaut-control-panel-ui</artifactId>
      <scope>provided</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>io.micronaut.controlpanel</groupId>
      <artifactId>micronaut-control-panel-management</artifactId>
      <scope>provided</scope>
    </dependency>
""")
        where:
        language << Language.values().toList()
    }

    void 'test control-panel with management configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext([ControlPanel.NAME, "management"])
        def cfg = commandContext.getConfiguration(Environment.DEVELOPMENT)

        then:
        cfg.get('endpoints.all.enabled') == true
        cfg.get('endpoints.all.sensitive') == false
        cfg.get('endpoints.health.details-visible') == 'ANONYMOUS'
        cfg.get('endpoints.loggers.write-sensitive') == false
    }

    
}
