package io.micronaut.starter.feature.database

import io.micronaut.core.version.SemanticVersion
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Features
import io.micronaut.starter.feature.config.Yaml
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import spock.lang.Issue

import static io.micronaut.starter.build.dependencies.MicronautDependencyUtils.GROUP_ID_MICRONAUT_DATA
import static io.micronaut.starter.build.dependencies.MicronautDependencyUtils.GROUP_ID_MICRONAUT_SQL
import static io.micronaut.starter.feature.database.DataFeature.MICRONAUT_DATA_PROCESSOR_ARTIFACT
import static io.micronaut.starter.feature.database.DataJdbc.MICRONAUT_DATA_JDBC_ARTIFACT
import static io.micronaut.starter.feature.database.jdbc.Hikari.MICRONAUT_JDBC_HIKARI_ARTIFACT

class DataJdbcSpec extends ApplicationContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature data-jdbc contains links to micronaut docs'() {
        when:
        def output = generate(['data-jdbc'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-data/latest/guide/index.html#jdbc")
    }

    void "test data jdbc features"() {
        when:
        Features features = getFeatures(['data-jdbc'])

        then:
        features.contains("data")
        features.contains("h2")
        features.contains("jdbc-hikari")
        features.contains("data-jdbc")
    }

    void "test dependencies are present for gradle"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(["data-jdbc"])
                .render()

        then:
        template.contains('annotationProcessor("io.micronaut.data:micronaut-data-processor")')
        template.contains('implementation("io.micronaut.data:micronaut-data-jdbc")')
        template.contains('implementation("io.micronaut.sql:micronaut-jdbc-hikari")')
        template.contains('runtimeOnly("com.h2database:h2")')
    }

    void "test dependencies are present for maven"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['data-jdbc'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.MAVEN, template)

        then:
        verifier.hasDependency(GROUP_ID_MICRONAUT_DATA, MICRONAUT_DATA_PROCESSOR_ARTIFACT, Scope.ANNOTATION_PROCESSOR )
        verifier.hasDependency(GROUP_ID_MICRONAUT_DATA, MICRONAUT_DATA_JDBC_ARTIFACT, Scope.COMPILE )
        verifier.hasDependency(GROUP_ID_MICRONAUT_SQL, MICRONAUT_JDBC_HIKARI_ARTIFACT, Scope.COMPILE )
        verifier.hasDependency("com.h2database", "h2", Scope.RUNTIME )

        when:
        Optional<SemanticVersion> semanticVersionOptional = parsePropertySemanticVersion(template, "micronaut.data.version")

        then:
        noExceptionThrown()
        semanticVersionOptional.isPresent()
    }

    void "test config for #buildTool for features #features"(BuildTool buildTool,
                                      String dialect,
                                      List<String> features) {
        given:
        Options options = new Options(null, null, buildTool)

        when:
        GeneratorContext ctx = buildGeneratorContext(features, options)

        then:
        if (features.size() == 1) {
            assert ctx.configuration.containsKey("datasources.default.url")
        }
        ctx.configuration.get("datasources.default.dialect") == dialect

        where:
        buildTool               | dialect       | features
        BuildTool.GRADLE        | 'SQL_SERVER'  | ['data-jdbc', 'sqlserver']
        BuildTool.GRADLE_KOTLIN | 'SQL_SERVER'  | ['data-jdbc', 'sqlserver']
        BuildTool.MAVEN         | 'SQL_SERVER'  | ['data-jdbc', 'sqlserver']
        BuildTool.GRADLE        | 'ORACLE'      | ['data-jdbc', 'oracle']
        BuildTool.GRADLE_KOTLIN | 'ORACLE'      | ['data-jdbc', 'oracle']
        BuildTool.MAVEN         | 'ORACLE'      | ['data-jdbc', 'oracle']
        BuildTool.GRADLE        | 'MYSQL'       | ['data-jdbc', 'mariadb']
        BuildTool.GRADLE_KOTLIN | 'MYSQL'       | ['data-jdbc', 'mariadb']
        BuildTool.MAVEN         | 'MYSQL'       | ['data-jdbc', 'mariadb']
        BuildTool.GRADLE        | 'MYSQL'       | ['data-jdbc', 'mysql']
        BuildTool.GRADLE_KOTLIN | 'MYSQL'       | ['data-jdbc', 'mysql']
        BuildTool.MAVEN         | 'MYSQL'       | ['data-jdbc', 'mysql']
        BuildTool.GRADLE        | 'POSTGRES'    | ['data-jdbc', 'postgres']
        BuildTool.GRADLE_KOTLIN | 'POSTGRES'    | ['data-jdbc', 'postgres']
        BuildTool.MAVEN         | 'POSTGRES'    | ['data-jdbc', 'postgres']
        BuildTool.GRADLE        | 'H2'          | ['data-jdbc']
        BuildTool.GRADLE_KOTLIN | 'H2'          | ['data-jdbc']
        BuildTool.MAVEN         | 'H2'          | ['data-jdbc']
    }

    void "test render config"() {
        when:
        Map<String, String> output = generate([Yaml.NAME, 'data-jdbc'])
        String config = output["src/main/resources/application.yml"]

        then:
        config
        config.contains('''\
    schema-generate: CREATE_DROP
    dialect: H2''')
    }

    @Issue("https://github.com/micronaut-projects/micronaut-starter/issues/686")
    void 'test data-processor dependency is in provided scope for Groovy and Maven'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
            .language(Language.GROOVY)
            .features(["data-jdbc"])
            .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.MAVEN, template)

        then:

        verifier.hasDependency(GROUP_ID_MICRONAUT_DATA, MICRONAUT_DATA_PROCESSOR_ARTIFACT, Scope.COMPILE_ONLY )
    }
}
