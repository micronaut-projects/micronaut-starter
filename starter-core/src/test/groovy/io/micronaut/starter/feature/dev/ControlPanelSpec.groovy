package io.micronaut.starter.feature.dev

import io.micronaut.context.env.Environment
import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.projectgen.core.generator.GeneratorContext
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.projectgen.core.buildtools.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.projectgen.core.buildtools.BuildTool
import spock.lang.Subject

class ControlPanelSpec extends ApplicationContextSpec implements CommandOutputFixture {
    @Subject
    ControlPanel controlPanel = beanContext.getBean(ControlPanel)

    void 'test control-panel feature supports default application type'() {

        expect:
        controlPanel.supports(MicronautOptions.builder().applicationType(ApplicationType.DEFAULT).build())

        and:
        !controlPanel.supports(MicronautOptions.builder().applicationType(ApplicationType.FUNCTION).build())
        !controlPanel.supports(MicronautOptions.builder().applicationType(ApplicationType.CLI).build())
        !controlPanel.supports(MicronautOptions.builder().applicationType(ApplicationType.MESSAGING).build())
        !controlPanel.supports(MicronautOptions.builder().applicationType(ApplicationType.GRPC).build())
    }

    void 'test readme.md with feature control-panel contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate([ControlPanel.NAME])
        String readme = output["README.md"]

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
