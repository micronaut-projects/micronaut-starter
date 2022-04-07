package io.micronaut.starter.feature.database

import groovy.xml.XmlParser
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options

class DataMongoSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void "test adding #feature results in the correct build for a Gradle app in #language"() {
        when:
        def output = generate(ApplicationType.DEFAULT, new Options(language, BuildTool.GRADLE), [feature])
        def readme = output["README.md"]
        def build = output['build.gradle']

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-data/latest/guide/#mongo")
        readme.contains("https://docs.mongodb.com")

        build.contains('implementation("io.micronaut.data:micronaut-data-mongodb')
        if (language == Language.KOTLIN) {
            assert build.contains('kapt("io.micronaut.data:micronaut-data-document-processor")')
        } else if (language == Language.JAVA) {
            assert build.contains('annotationProcessor("io.micronaut.data:micronaut-data-document-processor")')
        } else {
            assert build.contains('compileOnly("io.micronaut.data:micronaut-data-document-processor")')
        }

        build.contains($/${gradleConfiguration}("org.mongodb:${driver}")/$)

        // We only add this to maven projects
        !build.contains('annotationProcessor("io.micronaut.data:micronaut-data-processor')

        where:
        feature              | gradleConfiguration | driver                           | language
        'data-mongodb'       | 'runtimeOnly'       | 'mongodb-driver-sync'            | Language.JAVA
        'data-mongodb'       | 'runtimeOnly'       | 'mongodb-driver-sync'            | Language.KOTLIN
        'data-mongodb'       | 'implementation'    | 'mongodb-driver-sync'            | Language.GROOVY
        'data-mongodb-async' | 'runtimeOnly'       | 'mongodb-driver-reactivestreams' | Language.JAVA
        'data-mongodb-async' | 'runtimeOnly'       | 'mongodb-driver-reactivestreams' | Language.KOTLIN
        'data-mongodb-async' | 'implementation'    | 'mongodb-driver-reactivestreams' | Language.GROOVY
    }

    void "test adding #feature results in the correct build for a Maven app in #language"() {
        when:
        def output = generate(ApplicationType.DEFAULT, new Options(Language.JAVA, BuildTool.MAVEN), [feature])
        def readme = output["README.md"]
        def project = new XmlParser().parseText(output['pom.xml'])

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-data/latest/guide/#mongo")
        readme.contains("https://docs.mongodb.com")

        with(project.dependencies.dependency.find { it.artifactId.text() == "micronaut-data-mongodb" }) {
            scope.text() == 'compile'
            groupId.text() == 'io.micronaut.data'
        }

        with(project.dependencies.dependency.find { it.artifactId.text() == driver }) {
            scope.text() == 'runtime'
            groupId.text() == 'org.mongodb'
        }

        with(project.build.plugins.plugin.find { it.artifactId.text() == "maven-compiler-plugin" }) {
            def artifacts = configuration.annotationProcessorPaths.path.artifactId*.text()
            artifacts.contains("micronaut-data-document-processor")
            artifacts.contains("micronaut-data-processor")
            // data processor must come before the document processor because Maven
            artifacts.indexOf("micronaut-data-document-processor") > artifacts.indexOf("micronaut-data-processor")
        }

        where:
        feature              | driver                           | language
        'data-mongodb'       | 'mongodb-driver-sync'            | Language.JAVA
        'data-mongodb'       | 'mongodb-driver-sync'            | Language.KOTLIN
        'data-mongodb'       | 'mongodb-driver-sync'            | Language.GROOVY
        'data-mongodb-async' | 'mongodb-driver-reactivestreams' | Language.JAVA
        'data-mongodb-async' | 'mongodb-driver-reactivestreams' | Language.KOTLIN
        'data-mongodb-async' | 'mongodb-driver-reactivestreams' | Language.GROOVY
    }

    void "test config for #feature"() {
        when:
        GeneratorContext ctx = buildGeneratorContext([feature])

        then:
        with(ctx.getConfiguration()) {
            get("mongodb.uri") == 'mongodb://${MONGO_HOST:localhost}:${MONGO_PORT:27017}/mydb'
        }

        where:
        feature << ['data-mongodb', 'data-mongodb-async']
    }
}
