package io.micronaut.starter.feature.json

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class JsonFeatureSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Unroll
    void "test selected Json feature is no longer preview: #feature.name"(SerializationFeature feature) {
        expect:
        !feature.isPreview()

        where:
        feature << beanContext.getBeansOfType(SerializationFeature).iterator()
    }

    @Unroll
    void "test jackson-databind feature adds micronaut-jackson-databind-dependency for #buildTool "(BuildTool buildTool) {
        given:
        String groupId = 'io.micronaut'
        String artifactId = 'micronaut-jackson-databind'
        String feature = 'jackson-databind'
        Language language = Language.JAVA

        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features([feature])
                .language(language)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        verifier.hasDependency(groupId, artifactId)
        !verifier.hasDependency("micronaut-runtime")

        where:
        buildTool << BuildTool.values()
    }

    @Unroll
    void "test #feature feature adds serde processor and #artifactId dependency for #buildTool "(String feature, String artifactId, BuildTool buildTool) {
        given:
        String groupId = 'io.micronaut.serde'
        Language language = Language.JAVA
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features([feature])
                .language(language)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        verifier.hasDependency("io.micronaut.serde", "micronaut-serde-processor", Scope.ANNOTATION_PROCESSOR)
        verifier.hasDependency(groupId, artifactId)
        !verifier.hasDependency("micronaut-runtime")

        where:
        feature                 | artifactId                | buildTool
        'serialization-jackson' | 'micronaut-serde-jackson' | BuildTool.GRADLE_KOTLIN
        'serialization-jackson' | 'micronaut-serde-jackson' | BuildTool.GRADLE
        'serialization-jackson' | 'micronaut-serde-jackson' | BuildTool.MAVEN
        'serialization-jsonp'   | 'micronaut-serde-jsonp'   | BuildTool.GRADLE_KOTLIN
        'serialization-jsonp'   | 'micronaut-serde-jsonp'   | BuildTool.GRADLE
        'serialization-jsonp'   | 'micronaut-serde-jsonp'   | BuildTool.MAVEN
        'serialization-bson'    | 'micronaut-serde-bson'    | BuildTool.GRADLE_KOTLIN
        'serialization-bson'    | 'micronaut-serde-bson'    | BuildTool.GRADLE
        'serialization-bson'    | 'micronaut-serde-bson'    | BuildTool.MAVEN
    }
}
