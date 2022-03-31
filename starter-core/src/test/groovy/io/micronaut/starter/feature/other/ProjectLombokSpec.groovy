package io.micronaut.starter.feature.other

import groovy.xml.XmlParser
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
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
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['lombok'])
                .render()
        def project = new XmlParser().parseText(template)

        then:
        with(project.build.plugins.plugin.find { it.artifactId.text() == "maven-compiler-plugin" }) {
            def artifacts = configuration.annotationProcessorPaths.path.collect { "${it.groupId.text()}:${it.artifactId.text()}".toString() }
            artifacts.contains("org.projectlombok:lombok")
            artifacts.contains("io.micronaut:micronaut-inject-java")

            // make sure lombok is before inject-java or it won't work
            artifacts.indexOf("org.projectlombok:lombok") < artifacts.indexOf("io.micronaut:micronaut-inject-java")
        }

        with(project.dependencies.dependency.find { it.artifactId.text() == "lombok" }) {
            scope.text() == 'provided'
            groupId.text() == 'org.projectlombok'
        }
    }

    void 'micronaut-graal dependency is added because it is in the parent pom'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
            .features(['lombok'])
            .render()

        then:
        template.contains('''
            <path>
              <groupId>io.micronaut</groupId>
              <artifactId>micronaut-graal</artifactId>
              <version>${micronaut.version}</version>
            </path>
''')
    }
}
