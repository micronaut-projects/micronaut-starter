package io.micronaut.starter.feature.json

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import spock.lang.Unroll
import io.micronaut.starter.options.BuildTool

class JsonFeatureSpec extends ApplicationContextSpec {

    @Unroll
    void "test selected JSON feature: #impl"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features([impl])
                .render()

        then:
        template.contains("implementation(\"$module")

        where:
        module                                       | impl
        'io.micronaut:micronaut-jackson-databind'    | 'jackson-databind'
        'io.micronaut.serde:micronaut-serde-jackson' | 'serialization-jackson'
        'io.micronaut.serde:micronaut-serde-jsonb'   | 'serialization-jsonb'
        'io.micronaut.serde:micronaut-serde-bson'    | 'serialization-bson'
        
        
    }
}
