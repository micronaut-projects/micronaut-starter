package io.micronaut.starter.feature.other

import groovy.xml.XmlParser
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class ProjectLombokSpec extends ApplicationContextSpec implements CommandOutputFixture {
    @Shared
    @Subject
    ProjectLombok projectLombok = beanContext.getBean(ProjectLombok)

    void 'test readme.md with feature projectLombok contains links to micronaut docs'() {
        when:
        def output = generate(['lombok'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://projectlombok.org/features/all")
        readme.contains("https://docs.micronaut.io/latest/guide/index.html#lombok")
    }

    void "lombok belongs to Dev Tools category"() {
        expect:
        Category.DEV_TOOLS == projectLombok.category
    }

    @Unroll
    void 'test lombok with Gradle for Java'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['lombok'])
                .render()

        then:
        template.contains('annotationProcessor("org.projectlombok:lombok")')
        template.contains('compileOnly("org.projectlombok:lombok")')
    }

    @Unroll
    void 'test lombok with Gradle for only Java'() {
        when:
        new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['lombok'])
                .language(language)
                .render()

        then:
        def e = thrown(IllegalArgumentException)
        e.message.startsWith("The selected features are incompatible.")

        where:
        language << [Language.KOTLIN , Language.GROOVY]
    }

    void 'test maven lombok feature'() {
        when:
        BuildTool buildTool = BuildTool.MAVEN
        String template = new BuildBuilder(beanContext, buildTool)
                .features(['lombok'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("org.projectlombok", "lombok", Scope.ANNOTATION_PROCESSOR)
        verifier.hasDependency("io.micronaut", "micronaut-inject-java", Scope.ANNOTATION_PROCESSOR)

        Node project = new XmlParser().parseText(template)
        with(project.build.plugins.plugin.find { it.artifactId.text() == "maven-compiler-plugin" }) {
            def artifacts = configuration.annotationProcessorPaths.path.collect { "${it.groupId.text()}:${it.artifactId.text()}".toString() }
            // make sure lombok is before inject-java or it won't work
            artifacts.indexOf("org.projectlombok:lombok") < artifacts.indexOf("io.micronaut:micronaut-inject-java")
        }
        verifier.hasDependency("org.projectlombok", "lombok", "provided")
    }

    void 'micronaut-graal dependency is added because it is in the parent pom'() {
        when:
        BuildTool buildTool = BuildTool.MAVEN
        String template = new BuildBuilder(beanContext, buildTool)
            .features(['lombok'])
            .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut", "micronaut-graal", Scope.ANNOTATION_PROCESSOR)

    }
}
