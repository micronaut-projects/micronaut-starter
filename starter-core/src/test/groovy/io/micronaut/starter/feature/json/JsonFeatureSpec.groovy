package io.micronaut.starter.feature.json

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.options.BuildTool
import spock.lang.Unroll

class JsonFeatureSpec extends ApplicationContextSpec {

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

    void "test selected JSON feature for Gradle: #feature with test-resources #hasTestResources"(String module,
                                                                                                 String feature,
                                                                                                 String substitutiontarget1,
                                                                                                 String substitutionreplacement1,
                                                                                                 String substitutiontarget2,
                                                                                                 String substitutionreplacement2) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features([feature] + (hasTestResources ? ['test-resources'] : []))
                .render()

        then:
        template.contains("implementation(\"$module")
        template.contains('annotationProcessor("io.micronaut.serde:micronaut-serde-processor")')

        and: "If we do not have test resources included, we should have the substitution"
        !hasTestResources == template.contains('substitute(module("io.micronaut:micronaut-jackson-databind"))')
        !hasTestResources == template.contains("substitute(module(\"$substitutiontarget1")
        !hasTestResources == template.contains(".using(module(\"$substitutionreplacement1")
        if (substitutiontarget2 != null) {
            assert !hasTestResources == template.contains("substitute(module(\"$substitutiontarget2")
        }
        if (substitutionreplacement2 != null) {
            assert !hasTestResources == template.contains(".using(module(\"$substitutionreplacement2")
        }

        where:
        module                                       | feature                 | substitutiontarget1                       | substitutionreplacement1                     | substitutiontarget2                   | substitutionreplacement2                   | hasTestResources
        'io.micronaut.serde:micronaut-serde-jackson' | 'serialization-jackson' | 'io.micronaut:micronaut-jackson-databind' | 'io.micronaut.serde:micronaut-serde-jackson' | null                                  | null                                       | false
        'io.micronaut.serde:micronaut-serde-jsonp'   | 'serialization-jsonp'   | 'io.micronaut:micronaut-jackson-databind' | 'jakarta.json.bind:jakarta.json.bind-api'    | 'io.micronaut:micronaut-jackson-core' | 'io.micronaut.serde:micronaut-serde-jsonp' | false
        'io.micronaut.serde:micronaut-serde-bson'    | 'serialization-bson'    | 'io.micronaut:micronaut-jackson-databind' | 'io.micronaut.serde:micronaut-serde-bson'    | 'io.micronaut:micronaut-jackson-core' | 'io.micronaut.serde:micronaut-serde-bson'  | false
        'io.micronaut.serde:micronaut-serde-jackson' | 'serialization-jackson' | 'io.micronaut:micronaut-jackson-databind' | 'io.micronaut.serde:micronaut-serde-jackson' | null                                  | null                                       | true
        'io.micronaut.serde:micronaut-serde-jsonp'   | 'serialization-jsonp'   | 'io.micronaut:micronaut-jackson-databind' | 'jakarta.json.bind:jakarta.json.bind-api'    | 'io.micronaut:micronaut-jackson-core' | 'io.micronaut.serde:micronaut-serde-jsonp' | true
        'io.micronaut.serde:micronaut-serde-bson'    | 'serialization-bson'    | 'io.micronaut:micronaut-jackson-databind' | 'io.micronaut.serde:micronaut-serde-bson'    | 'io.micronaut:micronaut-jackson-core' | 'io.micronaut.serde:micronaut-serde-bson'  | true
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
    void "test selected JSON feature for Maven: #feature"(String artifactId, String feature) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features([feature])
                .render()

        then:
        template.contains("<artifactId>$artifactId</artifactId>")
        template.contains('<artifactId>micronaut-serde-processor</artifactId>')
        template.contains("""\
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
        artifactId                | feature
        'micronaut-serde-jackson' | 'serialization-jackson'
        'micronaut-serde-jsonp'   | 'serialization-jsonp'
        'micronaut-serde-bson'    | 'serialization-bson'
    }
}
