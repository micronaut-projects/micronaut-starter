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

        where:
        module                                       | impl
        'io.micronaut:micronaut-jackson-databind'    | 'jackson-databind'
        'io.micronaut.serde:micronaut-serde-jackson' | 'serialization-jackson'
        'io.micronaut.serde:micronaut-serde-jsonp'   | 'serialization-jsonp'
        'io.micronaut.serde:micronaut-serde-bson'    | 'serialization-bson'
        
        
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

        where:
        module                                 | impl
        'micronaut-jackson-databind'           | 'jackson-databind'
        'micronaut-serde-jackson'              | 'serialization-jackson'
        'micronaut-serde-jsonp'                | 'serialization-jsonp'
        'micronaut-serde-bson'                 | 'serialization-bson'
    }
}
