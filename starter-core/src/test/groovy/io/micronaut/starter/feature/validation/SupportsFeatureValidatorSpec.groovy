package io.micronaut.starter.feature.validation

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.ContextFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework

class SupportsFeatureValidatorSpec extends BeanContextSpec implements ContextFixture {

    void "python requires pyronaut and pytest"() {
        when:
        buildFeatureContext([], new Options(Language.PYTHON, TestFramework.JUNIT, BuildTool.PYRONAUT))

        then:
        IllegalArgumentException ex = thrown()
        ex.message == "python applications must use the pytest test framework. "

        when:
        buildFeatureContext([], new Options(Language.PYTHON, TestFramework.PYTEST, BuildTool.GRADLE))

        then:
        ex = thrown(IllegalArgumentException)
        ex.message == "python applications must use the pyronaut build tool. "
    }

    void "pyronaut and pytest cannot be selected for non-python languages"() {
        when:
        buildFeatureContext([], new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.PYRONAUT))

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "You can only use pyronaut build tool with python. "

        when:
        buildFeatureContext([], new Options(Language.JAVA, TestFramework.PYTEST, BuildTool.GRADLE))

        then:
        ex = thrown(IllegalArgumentException)
        ex.message == "You can only use pytest testing framework with python. "
    }

    void "python rejects unsupported configuration logging and bootstrap features"() {
        when:
        buildGeneratorContext(["yaml"], new Options(Language.PYTHON))

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "Feature yaml does not support language python. "

        when:
        buildGeneratorContext(["log4j2"], new Options(Language.PYTHON))

        then:
        ex = thrown(IllegalArgumentException)
        ex.message == "Feature log4j2 does not support language python. "

        when:
        buildGeneratorContext(["aws-secrets-manager"], new Options(Language.PYTHON, BuildTool.PYRONAUT), ApplicationType.DEFAULT)

        then:
        ex = thrown(IllegalArgumentException)
        ex.message.contains("Feature aws-secrets-manager does not support language python. ")
    }

    void "python supports graalvm but rejects CI features"() {
        when:
        buildGeneratorContext(["graalvm"], new Options(Language.PYTHON))

        then:
        noExceptionThrown()

        when:
        buildGeneratorContext(["github-workflow-ci"], new Options(Language.PYTHON, BuildTool.PYRONAUT))

        then:
        IllegalArgumentException ex = thrown()
        ex.message.contains("Feature github-workflow-ci does not support language python. ")
    }

    void "python rejects jvm-specific HTTP client features"() {
        when:
        buildGeneratorContext(["http-client-jdk"], new Options(Language.PYTHON, BuildTool.PYRONAUT))

        then:
        IllegalArgumentException ex = thrown()
        ex.message.contains("Feature http-client-jdk does not support language python. ")
    }

    void "python rejects jvm-specific management features"() {
        when:
        buildGeneratorContext(["jmx"], new Options(Language.PYTHON, BuildTool.PYRONAUT))

        then:
        IllegalArgumentException ex = thrown()
        ex.message.contains("Feature jmx does not support language python. ")
    }

    void "python rejects alternate server features"() {
        when:
        buildGeneratorContext(["jetty-server"], new Options(Language.PYTHON, BuildTool.PYRONAUT))

        then:
        IllegalArgumentException ex = thrown()
        ex.message.contains("Feature jetty-server does not support language python. ")

        when:
        buildGeneratorContext(["json-path"], new Options(Language.PYTHON, BuildTool.PYRONAUT))

        then:
        noExceptionThrown()
    }

    void "python rejects features that require java reflection"(String feature) {
        when:
        buildGeneratorContext([feature], new Options(Language.PYTHON, BuildTool.PYRONAUT))

        then:
        IllegalArgumentException ex = thrown()
        ex.message.contains("Feature ${feature} does not support language python. ")

        where:
        feature << ["data-hibernate-reactive", "hibernate-jpa", "hibernate-reactive-jpa", "hibernate-validator", "jackson-databind"]
    }
}
