package io.micronaut.starter.options

import com.fizzed.rocker.RockerModel
import spock.lang.Specification
import spock.lang.Unroll

class TestRockerModelProviderSpec extends Specification {

    @Unroll
    void "a delegate method is defined for language: #language and test framework: #testFramework "(Language language,
                                                                                                    TestFramework testFramework) {
        given:
        TestRockerModelProvider provider = new TestRockerModelProvider(null) {

            @Override
            RockerModel spock() {
                return null
            }

            @Override
            RockerModel kotlinTest() {
                return null
            }

            @Override
            RockerModel javaJunit() {
                return null
            }

            @Override
            RockerModel groovyJunit() {
                return null
            }

            @Override
            RockerModel kotlinJunit() {
                return null
            }
        }

        when:
        provider.findModel(language, testFramework)

        then:
        noExceptionThrown()

        where:
        [language, testFramework] << [Language.values(), TestFramework.values()].combinations()
    }
}

