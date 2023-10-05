package io.micronaut.starter.feature.dev

import io.micronaut.context.env.Environment
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.validator.MicronautHttpValidation
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Subject

class ControlPanelSpec extends ApplicationContextSpec implements CommandOutputFixture {
    @Subject
    ControlPanel controlPanel = beanContext.getBean(ControlPanel)

    void 'test control-panel feature supports default application type'() {

        expect:
        controlPanel.supports(ApplicationType.DEFAULT)

        and:
        !controlPanel.supports(ApplicationType.FUNCTION)
        !controlPanel.supports(ApplicationType.CLI)
        !controlPanel.supports(ApplicationType.MESSAGING)
        !controlPanel.supports(ApplicationType.GRPC)
    }

    void 'test readme.md with feature control-panel contains links to micronaut docs'() {
        when:
        def output = generate([ControlPanel.NAME])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-control-panel/latest/guide/index.html")
    }

    void 'test dependency added for control-panel feature'(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features([ControlPanel.NAME])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut.controlpanel", "micronaut-control-panel-ui", Scope.DEVELOPMENT_ONLY)

        where:
        buildTool << BuildTool.values()
    }

    void 'test dependency added for control-panel and management feature'(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features([ControlPanel.NAME])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut.controlpanel", "micronaut-control-panel-ui", Scope.DEVELOPMENT_ONLY)
        verifier.hasDependency("io.micronaut.controlpanel", "micronaut-control-panel-management", Scope.DEVELOPMENT_ONLY)

        where:
        buildTool << BuildTool.values()
    }

    void 'test control-panel with management configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext([ControlPanel.NAME])
        def cfg = commandContext.getConfiguration(Environment.DEVELOPMENT)

        then:
        cfg.get('endpoints.all.enabled') == true
        cfg.get('endpoints.all.sensitive') == false
        cfg.get('endpoints.health.details-visible') == 'ANONYMOUS'
        cfg.get('endpoints.loggers.write-sensitive') == false
    }

}
