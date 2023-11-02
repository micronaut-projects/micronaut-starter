package io.micronaut.starter.feature.micrometer

import groovy.xml.XmlSlurper
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Features
import io.micronaut.starter.feature.database.r2dbc.DataR2dbc
import io.micronaut.starter.feature.database.r2dbc.R2dbc
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Issue
import spock.lang.Unroll

class MicrometerSpec extends ApplicationContextSpec {

    @Unroll
    void 'test gradle micrometer feature #micrometerFeature.name'(MicrometerFeature micrometerFeature) {
        given:
        String dependency = micrometerFeature.getArtifactId()
        String group = micrometerFeature.getGroupId()

        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features([micrometerFeature.name])
                .render()

        then:
        template.contains("implementation(\"${group}:${dependency}\")")

        where:
        micrometerFeature << beanContext.getBeansOfType(MicrometerFeature).iterator()
    }

    void 'test gradle micrometer feature oracle cloud #buildTool'(BuildTool buildTool) {
        given:
        var name = beanContext.getBean(OracleCloud).getName()

        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features([name])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency('io.micronaut.oraclecloud', 'micronaut-oraclecloud-bmc-monitoring', Scope.COMPILE)

        where:
        buildTool << BuildTool.values()
    }

    void "test gradle micrometer multiple features"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(["micrometer-atlas", "micrometer-influx"])
                .render()

        then:
        template.contains("""
    implementation("io.micronaut.micrometer:micronaut-micrometer-core")
    implementation("io.micronaut.micrometer:micronaut-micrometer-registry-atlas")
    implementation("io.micronaut.micrometer:micronaut-micrometer-registry-influx")
""")
        template.count("io.micronaut.micrometer:micronaut-micrometer-core") == 1
    }

    @Unroll
    void 'test maven micrometer feature #micrometerFeature.name'(MicrometerFeature micrometerFeature) {
        given:
        String dependency = micrometerFeature.getArtifactId()
        String group = micrometerFeature.getGroupId()

        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features([micrometerFeature.name])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>$group</groupId>
      <artifactId>$dependency</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        micrometerFeature << beanContext.getBeansOfType(MicrometerFeature).iterator()
    }

    void "test maven micrometer multiple features"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(["micrometer-atlas", "micrometer-influx"])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.micrometer</groupId>
      <artifactId>micronaut-micrometer-core</artifactId>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>io.micronaut.micrometer</groupId>
      <artifactId>micronaut-micrometer-registry-atlas</artifactId>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>io.micronaut.micrometer</groupId>
      <artifactId>micronaut-micrometer-registry-influx</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.count("micronaut-micrometer-core") == 1
    }

    @Unroll
    void 'test micrometer configuration for feature=#micrometerFeature'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext([micrometerFeature])

        then: 'the micrometer configuration is enabled for the feature'
        commandContext.configuration.get("micronaut.metrics.export.${configKey}.enabled".toString()) == true
        commandContext.configuration.get('micronaut.metrics.enabled') == true

        where:
        micrometerFeature << beanContext.getBeansOfType(MetricsRegistryFeature)*.name.iterator()
        configKey = "${micrometerFeature - 'micrometer-'}".replace('-', '')
    }

    void 'test mandatory dependencies for micrometer features are added'() {
        when: 'a new feature context with one micrometer feature'
        Features features = getFeatures(["micrometer-appoptics"])

        then: 'the micrometer feature is added and also micrometer-core and management'
        features.containsAll(['micrometer-appoptics', 'micrometer', 'management'])
    }

    void 'test title conversion is done properly'() {
        when: 'a feature with one word'
        Atlas atlas = beanContext.getBean(Atlas)

        then: 'the title is correct'
        atlas.title == 'Micrometer Atlas'

        when: 'a feature with two words in the name'
        NewRelic newRelic = beanContext.getBean(NewRelic)

        then: 'the title is correct'
        newRelic.title == 'Micrometer New Relic'
    }

    void 'test gradle micrometer-annotation feature for #language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['micrometer-annotation', 'kapt'])
                .render()

        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.GRADLE, template)

        then:
        verifier.hasDependency("io.micronaut.micrometer", "micronaut-micrometer-annotation", scope)

        where:
        language        | scope
        Language.JAVA   | "annotationProcessor"
        Language.KOTLIN | "kapt"
        Language.GROOVY | "compileOnly"
    }

    void 'test maven micrometer-annotation feature for #language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['micrometer-annotation'])
                .render()
        def xml = new XmlSlurper().parseText(template).'**'.find{ it.artifactId == 'micronaut-micrometer-annotation' }

        then:
        xml.name()  == group
        xml.groupId == 'io.micronaut.micrometer'
        xml[versionOrScope] == expected

        and: "micronaut-core is excluded for java"
        language != Language.JAVA || xml.exclusions.exclusion.artifactId.text() == 'micronaut-core'

        where:
        language        | group                     | versionOrScope || expected
        Language.JAVA   | 'path'                    | 'version'      || '${micronaut.micrometer.version}'
        Language.KOTLIN | 'annotationProcessorPath' | 'version'      || '${micronaut.micrometer.version}'
        Language.GROOVY | 'dependency'              | 'scope'        || 'provided'
    }

    void 'test mandatory dependencies and configurations are added with micrometer-annotation'() {
        given:
        def feature = ['micrometer-annotation']

        when:
        Features features = getFeatures(feature)
        GeneratorContext commandContext = buildGeneratorContext(feature)

        then:
        features.containsAll(['micrometer-annotation', 'micrometer', 'management'])

        and:
        commandContext.configuration.get('micronaut.metrics.enabled') == true
    }

    void "test micrometer annotation and feature for buildTool=#buildTool"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(["micrometer-atlas", "micrometer-annotation"])
                .render()
        def deps = template.readLines().grep(~/.*io\.micronaut\.micrometer.*/).collect{ it.trim() }
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        deps.size() == 3
        verifier.hasDependency("io.micronaut.micrometer", "micronaut-micrometer-core", Scope.COMPILE)
        verifier.hasDependency("io.micronaut.micrometer", "micronaut-micrometer-registry-atlas", Scope.COMPILE)
        verifier.hasDependency("io.micronaut.micrometer", "micronaut-micrometer-annotation", Scope.ANNOTATION_PROCESSOR)

        where:
        buildTool << BuildTool.values()
    }

    void "test micrometer dependencies for Observation feature=#feature for buildTool=#buildTool"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(["micrometer-atlas", "micrometer-annotation"])
                .render()
        def deps = template.readLines().grep(~/.*io\.micronaut\.micrometer.*/).collect{ it.trim() }
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        deps.size() == 3
        verifier.hasDependency("io.micronaut.micrometer", "micronaut-micrometer-core", Scope.COMPILE)
        verifier.hasDependency("io.micronaut.micrometer", "micronaut-micrometer-registry-atlas", Scope.COMPILE)
        verifier.hasDependency("io.micronaut.micrometer", "micronaut-micrometer-annotation", Scope.ANNOTATION_PROCESSOR)

        where:
        [feature, buildTool] << [["micrometer-observation", "micrometer-observation-http"], BuildTool.values()].combinations()
    }

    void 'test configuration for feature micrometer-observation-http'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(["micrometer-observation-http"])

        then: 'the configuration is enabled for the feature'
        commandContext.configuration.get('micrometer.observation.http.server.enabled') == true
        commandContext.configuration.get('micrometer.observation.http.client.enabled') == true
    }

    @Issue("https://github.com/micronaut-projects/micronaut-starter/issues/1535")
    void "test #micrometerFeature includes r2dbc-pool runtime when combined with #r2dbcFeature "(
            Language language, BuildTool buildTool, String r2dbcFeature, String micrometerFeature
    ) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features([r2dbcFeature, micrometerFeature])
                .render()

        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        verifier.hasDependency("io.r2dbc", "r2dbc-pool", Scope.RUNTIME)

        where:
        [language, buildTool, r2dbcFeature, micrometerFeature] << [
                Language.values(),
                BuildTool.values(),
                [R2dbc.NAME, DataR2dbc.NAME],
                beanContext.getBeansOfType(MicrometerFeature)*.name.toList()
        ].combinations()
    }

    @Issue("https://github.com/micronaut-projects/micronaut-starter/issues/1535")
    void "test #micrometerFeature without r2dbc feature does not includes r2dbc-pool runtime"(
            Language language, BuildTool buildTool, String micrometerFeature
    ) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features([micrometerFeature])
                .render()

        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        !verifier.hasDependency("io.r2dbc", "r2dbc-pool", Scope.RUNTIME)

        where:
        [language, buildTool, micrometerFeature] << [
                Language.values(),
                BuildTool.values(),
                beanContext.getBeansOfType(MicrometerFeature)*.name.toList()
        ].combinations()
    }
}
