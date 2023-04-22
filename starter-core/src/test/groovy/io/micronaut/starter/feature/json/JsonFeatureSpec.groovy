package io.micronaut.starter.feature.json

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.options.BuildTool
import spock.lang.Unroll

class JsonFeatureSpec extends ApplicationContextSpec {

    @Unroll
    void "test selected Json feature is no longer preview: #feature.name"(SerializationFeature feature) {
        expect:
        !feature.isPreview()

        where:
        feature << beanContext.getBeansOfType(SerializationFeature).iterator()
    }

    @Unroll
    void "test selected JSON feature for Gradle: #feature"(String feature, String coordinate) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features([feature])
                .render()

        then:
        template.contains("implementation(\"$coordinate")

        where:
        coordinate                                | feature
        'io.micronaut:micronaut-jackson-databind' | 'jackson-databind'
    }

    void "test selected JSON feature for Gradle: #feature and coordinate #module"(String module,
                                                                                  String feature) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features([feature])
                .render()

        then:
        template.contains("implementation(\"$module")
        template.contains('annotationProcessor("io.micronaut.serde:micronaut-serde-processor")')

        where:
        module                                       | feature
        'io.micronaut.serde:micronaut-serde-jackson' | 'serialization-jackson'
        'io.micronaut.serde:micronaut-serde-jsonp'   | 'serialization-jsonp'
        'io.micronaut.serde:micronaut-serde-bson'    | 'serialization-bson'
        'io.micronaut.serde:micronaut-serde-jackson' | 'serialization-jackson'
        'io.micronaut.serde:micronaut-serde-jsonp'   | 'serialization-jsonp'
        'io.micronaut.serde:micronaut-serde-bson'    | 'serialization-bson'
    }

    @Unroll
    void "feature #feature for Maven adds dependency with artifact id: #artifactId"(String artifactId, String feature) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features([feature])
                .render()

        then:
        template.contains("<artifactId>$artifactId</artifactId>")

        where:
        artifactId                   | feature
        'micronaut-jackson-databind' | 'jackson-databind'
    }

    @Unroll
    void "test selected JSON feature for Maven: #feature with test-resources #hasTestResources"(String artifactId, String feature) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features([feature] + (hasTestResources ? ['test-resources'] : []))
                .render()

        then:
        template.contains("<artifactId>$artifactId</artifactId>")
        template.contains('<artifactId>micronaut-serde-processor</artifactId>')

        and: "runtime is included and doesn't exclude jackson if test-resources is selected"
        if (hasTestResources) {
            assert template.contains("""\
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-runtime</artifactId>
      <scope>compile</scope>
    </dependency>""")
        } else {
            assert template.contains("""\
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-runtime</artifactId>
      <scope>compile</scope>
      <exclusions>
          <exclusion>
            <groupId>io.micronaut</groupId>
            <artifactId>micronaut-jackson-databind</artifactId>
          </exclusion>
        </exclusions>
    </dependency>""")
        }
        template.contains("""
    <dependency>
      <groupId>io.micronaut.serde</groupId>
      <artifactId>$artifactId</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        if (feature == 'serialization-jsonp') {
            assert template.contains("""\
    <dependency>
      <groupId>jakarta.json.bind</groupId>
      <artifactId>jakarta.json.bind-api</artifactId>""")
        }
        where:
        artifactId                | feature                 | hasTestResources
        'micronaut-serde-jackson' | 'serialization-jackson' | false
        'micronaut-serde-jsonp'   | 'serialization-jsonp'   | false
        'micronaut-serde-bson'    | 'serialization-bson'    | false
        'micronaut-serde-jackson' | 'serialization-jackson' | true
        'micronaut-serde-jsonp'   | 'serialization-jsonp'   | true
        'micronaut-serde-bson'    | 'serialization-bson'    | true
    }
}
