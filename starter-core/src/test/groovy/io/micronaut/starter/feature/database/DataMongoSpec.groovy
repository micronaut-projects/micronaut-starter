package io.micronaut.starter.feature.database

import groovy.xml.XmlParser
import groovy.yaml.YamlSlurper
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options

class DataMongoSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void "test add data-mongodb feature for Gradle"() {
        when:
        def output = generate(['data-mongodb'])
        def readme = output["README.md"]
        def build = output['build.gradle']

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-mongodb/latest/guide/index.html")
        readme.contains("https://micronaut-projects.github.io/micronaut-data/latest/guide/#mongo")
        readme.contains("https://docs.mongodb.com")
        build.contains('implementation("io.micronaut.data:micronaut-data-mongodb')
        build.contains('annotationProcessor("io.micronaut.data:micronaut-data-document-processor')
        // By default, we grab the sync drivers
        build.contains('implementation("io.micronaut.mongodb:micronaut-mongo-sync')

        // We only add this to maven projects
        !build.contains('annotationProcessor("io.micronaut.data:micronaut-data-processor')
    }

    void "test add data-mongodb feature for Maven"() {
        when:
        def output = generate(ApplicationType.DEFAULT, new Options(Language.JAVA, BuildTool.MAVEN), ['data-mongodb'])
        def readme = output["README.md"]
        def project = new XmlParser().parseText(output['pom.xml'])

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-mongodb/latest/guide/index.html")
        readme.contains("https://micronaut-projects.github.io/micronaut-data/latest/guide/#mongo")
        readme.contains("https://docs.mongodb.com")

        with(project.dependencies.dependency.find { it.artifactId.text() == "micronaut-data-mongodb" }) {
            scope.text() == 'compile'
            groupId.text() == 'io.micronaut.data'
        }

        with(project.dependencies.dependency.find { it.artifactId.text() == "micronaut-mongo-sync" }) {
            scope.text() == 'compile'
            groupId.text() == 'io.micronaut.mongodb'
        }

        with(project.build.plugins.plugin.find { it.artifactId.text() == "maven-compiler-plugin" }) {
            def artifacts = configuration.annotationProcessorPaths.path.artifactId*.text()
            artifacts.contains("micronaut-data-document-processor")
            artifacts.contains("micronaut-data-processor")
            // data processor must come before the document processor because Maven
            artifacts.indexOf("micronaut-data-document-processor") > artifacts.indexOf("micronaut-data-processor")
        }
    }

    void "dependencies are present for gradle with data-mongodb and both mongo-reactive and mongo-sync"() {
        when:
        def output = generate(ApplicationType.DEFAULT, new Options(Language.JAVA, BuildTool.GRADLE), ['data-mongodb', 'mongo-reactive', 'mongo-sync'])
        def build = output.'build.gradle'
        def application = new YamlSlurper().parseText(output.'src/main/resources/application.yml')

        then:
        build.contains('implementation("io.micronaut.mongodb:micronaut-mongo-reactive")')
        build.contains('implementation("io.micronaut.mongodb:micronaut-mongo-sync")')
        build.contains('testImplementation("org.testcontainers:mongodb")')

        and: 'both were chosen, but we choose reactive for the user'
        application.micronaut.data.mongodb.'driver-type' == 'reactive'
    }

    void "dependencies are present for gradle with data-mongodb and just mongo-reactive"() {
        when:
        def output = generate(ApplicationType.DEFAULT, new Options(Language.JAVA, BuildTool.GRADLE), ['data-mongodb', 'mongo-reactive'])
        def build = output.'build.gradle'
        def application = new YamlSlurper().parseText(output.'src/main/resources/application.yml')

        then:
        build.contains('implementation("io.micronaut.mongodb:micronaut-mongo-reactive")')
        // Sync driver is omitted
        !build.contains('implementation("io.micronaut.mongodb:micronaut-mongo-sync")')
        build.contains('testImplementation("org.testcontainers:mongodb")')

        and: 'no need to set the driver type'
        application.micronaut?.data?.mongodb?.'driver-type' == null
    }

    void "adding mongo-sync to a gradle build makes no difference to the build script"() {
        when:
        def noSync = generate(ApplicationType.DEFAULT, new Options(Language.JAVA, BuildTool.GRADLE), ['data-mongodb'])
        def sync = generate(ApplicationType.DEFAULT, new Options(Language.JAVA, BuildTool.GRADLE), ['data-mongodb', 'mongo-sync'])

        then:
        noSync.'build.gradle'
        noSync.'build.gradle' == sync.'build.gradle'
        noSync.'src/main/resources/application.yml'
        noSync.'src/main/resources/application.yml' == sync.'src/main/resources/application.yml'
    }

    void "dependencies are present for maven with data-mongodb and both mongo-reactive and mongo-sync"() {
        when:
        def output = generate(ApplicationType.DEFAULT, new Options(Language.JAVA, BuildTool.MAVEN), ['data-mongodb', 'mongo-reactive', 'mongo-sync'])
        def project = new XmlParser().parseText(output.'pom.xml')
        def application = new YamlSlurper().parseText(output.'src/main/resources/application.yml')

        then:
        with(project.dependencies.dependency.find { it.artifactId.text() == "micronaut-mongo-reactive" }) {
            scope.text() == 'compile'
            groupId.text() == 'io.micronaut.mongodb'
        }
        with(project.dependencies.dependency.find { it.artifactId.text() == "mongodb" }) {
            scope.text() == 'test'
            groupId.text() == 'org.testcontainers'
        }

        and: 'mongo-sync is pulled in, as it was requested as a feature'
        with(project.dependencies.dependency.find { it.artifactId.text() == "micronaut-mongo-sync" }) {
            scope.text() == 'compile'
            groupId.text() == 'io.micronaut.mongodb'
        }

        and: 'both were chosen, but we choose reactive for the user'
        application.micronaut.data.mongodb.'driver-type' == 'reactive'
    }

    void "dependencies are present for maven with data-mongodb and just mongo-reactive"() {
        when:
        def output = generate(ApplicationType.DEFAULT, new Options(Language.JAVA, BuildTool.MAVEN), ['data-mongodb', 'mongo-reactive'])
        def project = new XmlParser().parseText(output.'pom.xml')
        def application = new YamlSlurper().parseText(output.'src/main/resources/application.yml')

        then:
        with(project.dependencies.dependency.find { it.artifactId.text() == "micronaut-mongo-reactive" }) {
            scope.text() == 'compile'
            groupId.text() == 'io.micronaut.mongodb'
        }
        with(project.dependencies.dependency.find { it.artifactId.text() == "mongodb" }) {
            scope.text() == 'test'
            groupId.text() == 'org.testcontainers'
        }

        and: 'mongo-sync is excluded as we chose reactive'
        project.dependencies.dependency.find { it.artifactId.text() == "micronaut-mongo-sync" } == null

        and: 'no need to set the driver type'
        application.micronaut?.data?.mongodb?.'driver-type' == null
    }

    void "adding mongo-sync to a maven build makes no difference to the pom"() {
        when:
        def noSync = generate(ApplicationType.DEFAULT, new Options(Language.JAVA, BuildTool.MAVEN), ['data-mongodb'])
        def sync = generate(ApplicationType.DEFAULT, new Options(Language.JAVA, BuildTool.MAVEN), ['data-mongodb', 'mongo-sync'])

        then:
        noSync.'pom.xml'
        noSync.'pom.xml' == sync.'pom.xml'
        noSync.'src/main/resources/application.yml'
        noSync.'src/main/resources/application.yml' == sync.'src/main/resources/application.yml'
    }

    void "test config for #features"() {
        when:
        GeneratorContext ctx = buildGeneratorContext(featureList)

        then:
        with(ctx.getConfiguration()) {
            get("mongodb.uri") == 'mongodb://${MONGO_HOST:localhost}:${MONGO_PORT:27017}/mydb'
            get('micronaut.data.mongodb.driver-type') == driverType
        }

        where:
        featureList                                      | driverType
        ['data-mongodb', 'mongo-sync']                   | null
        ['data-mongodb', 'mongo-reactive']               | null
        ['data-mongodb', 'mongo-sync', 'mongo-reactive'] | 'reactive'

        features = featureList.dropRight(1).join(", ") + (featureList.size() > 1 ? " and " : '') + featureList.last()
    }
}
