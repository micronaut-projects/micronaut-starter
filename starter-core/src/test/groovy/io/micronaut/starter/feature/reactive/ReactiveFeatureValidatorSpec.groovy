package io.micronaut.starter.feature.reactive

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import spock.lang.Unroll

class ReactiveFeatureValidatorSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Unroll("#description")
    void 'more than one reactive feature is not allowed'(List<String> featureNames) {
        when:
        new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(featureNames)
                .render()

        then:
        IllegalArgumentException e = thrown()
        e.message.startsWith("You cannot select more than one reactive library")

        where:
        featureNames << ['rxjava1','reactor','rxjava3']
                .permutations()
                .collect { it.collate(2, false).flatten() }
        description = featureNames.join(',') + " not allowed together"
    }
}
