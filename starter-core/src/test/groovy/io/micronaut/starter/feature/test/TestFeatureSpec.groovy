package io.micronaut.starter.feature.test

import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.options.TestFramework
import spock.lang.Specification
import spock.lang.Unroll

class TestFeatureSpec extends Specification {

    @Unroll("for test framework: #testFramework isKotlinTestFramework return #expected")
    void "isKotlinTestFramework returns true for test framework which require Kotlin dependencies"(TestFramework testFramework) {
        given:
        TestFeature feature = new TestFeature() {

            @Override
            void doApply(GeneratorContext generatorContext) {

            }

            @Override
            TestFramework getTestFramework() {
                return testFramework
            }

            @Override
            String getName() {
                return null
            }
        }
        expect:
        expected == feature.isKotlinTestFramework()

        where:
        expected | testFramework
        false    | TestFramework.JUNIT
        true     | TestFramework.KOTEST
        false    | TestFramework.SPOCK

    }
}
