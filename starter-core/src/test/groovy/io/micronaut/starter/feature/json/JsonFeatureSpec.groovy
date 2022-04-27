package io.micronaut.starter.feature.json

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.options.BuildTool
import spock.lang.Unroll

class JsonFeatureSpec extends ApplicationContextSpec {

    @Unroll
    void "test selected JSON feature for Gradle: #impl"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features([impl])
                .render()

        then:
        template.contains("implementation(\"$module")
        impl == 'jackson-databind' || template.contains('annotationProcessor("io.micronaut.serde:micronaut-serde-processor")')
        impl == 'jackson-databind' || template.contains('substitute(module("io.micronaut:micronaut-jackson-databind"))')
        impl == 'jackson-databind' || template.contains(".using(module(\"$substitution1")
        impl == 'jackson-databind' || impl == 'serialization-jackson' || template.contains('substitute(module("io.micronaut:micronaut-jackson-core"))')
        impl == 'jackson-databind' || impl == 'serialization-jackson' || template.contains(".using(module(\"$substitution2")

        where:
        module                                       | impl                    | substitution1                                | substitution2
        'io.micronaut:micronaut-jackson-databind'    | 'jackson-databind'      | null                                         | null
        'io.micronaut.serde:micronaut-serde-jackson' | 'serialization-jackson' | 'io.micronaut.serde:micronaut-serde-jackson' | null
        'io.micronaut.serde:micronaut-serde-jsonp'   | 'serialization-jsonp'   | 'jakarta.json.bind:jakarta.json.bind-api'    | 'io.micronaut.serde:micronaut-serde-jsonp'
        'io.micronaut.serde:micronaut-serde-bson'    | 'serialization-bson'    | 'jakarta.json.bind:jakarta.json.bind-api'    | 'io.micronaut.serde:micronaut-serde-bson'
    }

    @Unroll
    void "test selected JSON feature for Maven: #impl"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features([impl])
                .render()

        then:
        template.contains("<artifactId>$module</artifactId>")
        impl == 'jackson-databind' || template.contains('<artifactId>micronaut-serde-processor</artifactId>')
        impl == 'jackson-databind' || template.contains("""
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-runtime</artifactId>
      <exclusions>
        <exclusion>
          <groupId>io.micronaut</groupId>
          <artifactId>micronaut-jackson-databind</artifactId>
        </exclusion>
      </exclusions>
      <scope>compile</scope>
    </dependency>
""")
        impl == 'jackson-databind' || template.contains("""
    <dependency>
      <groupId>io.micronaut.serde</groupId>
      <artifactId>$module</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        impl == 'jackson-databind' || impl == 'serialization-jackson' || template.contains("""
    <dependency>
      <groupId>jakarta.json.bind</groupId>
      <artifactId>jakarta.json.bind-api</artifactId>
""")
        where:
        module                                 | impl
        'micronaut-jackson-databind'           | 'jackson-databind'
        'micronaut-serde-jackson'              | 'serialization-jackson'
        'micronaut-serde-jsonp'                | 'serialization-jsonp'
        'micronaut-serde-bson'                 | 'serialization-bson'
    }
}
