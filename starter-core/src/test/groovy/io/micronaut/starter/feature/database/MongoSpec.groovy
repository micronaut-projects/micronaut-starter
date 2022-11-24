package io.micronaut.starter.feature.database

import groovy.xml.XmlParser
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.Features
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import spock.lang.Unroll

class MongoSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Unroll
    void 'test readme.md with feature #feature contains links to micronaut and 3rd party docs'(String feature) {
        when:
        def output = generate([feature])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-mongodb/latest/guide/index.html")
        readme.contains("https://docs.mongodb.com")

        where:
        feature << ['mongo-sync', 'mongo-reactive']
    }

    void "test mongo sync features"() {
        when:
        Features features = getFeatures(['mongo-sync'])

        then:
        features.contains("mongo-sync")
    }

    void "test mongo sync dependencies are present for gradle"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(["mongo-sync"])
                .render()

        then:
        template.contains('implementation("io.micronaut.mongodb:micronaut-mongo-sync")')
        !template.contains('testImplementation("org.testcontainers:mongodb")')
    }

    void "test mongo sync dependencies are present for maven"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['mongo-sync'])
                .render()
        def project = new XmlParser().parseText(template)

        then:
        with(project.dependencies.dependency.find { it.artifactId.text() == "micronaut-mongo-sync" }) {
            scope.text() == 'compile'
            groupId.text() == 'io.micronaut.mongodb'
        }
    }

    void "test mongo reactive features"() {
        when:
        Features features = getFeatures(['mongo-reactive'])

        then:
        features.contains("mongo-reactive")
    }

    void "test dependencies are present for gradle"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(["mongo-reactive"])
                .render()
        then:
        template.contains('implementation("io.micronaut.mongodb:micronaut-mongo-reactive")')
        !template.contains('testImplementation("org.testcontainers:mongodb")')
    }

    void "test dependencies are present for maven"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['mongo-reactive'])
                .render()
        def project = new XmlParser().parseText(template)

        then:
        with(project.dependencies.dependency.find { it.artifactId.text() == "micronaut-mongo-reactive" }) {
            scope.text() == 'compile'
            groupId.text() == 'io.micronaut.mongodb'
        }
    }

    void "test config for #features"() {
        when:
        GeneratorContext ctx = buildGeneratorContext(featureList)

        then:
        with(ctx.getConfiguration()) {
            !get("mongodb.uri")
            get('micronaut.data.mongodb.driver-type') == driverType
        }

        where:
        featureList                                      | driverType
        ['mongo-reactive']                               | null
        ['mongo-sync']                                   | null
        ['mongo-reactive', 'mongo-sync']                 | null
        ['data-mongodb', 'test-resources']               | null
        ['data-mongodb-reactive', 'test-resources']      | null

        features = featureList.dropRight(1).join(", ") + (featureList.size() > 1 ? " and " : '') + featureList.last()
    }
}
