package io.micronaut.starter.feature.validation

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.ContextFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework

class PythonFeatureValidatorSpec extends BeanContextSpec implements ContextFixture {

    void "python requires pyronaut and pytest"() {
        when:
        buildFeatureContext([], new Options(Language.PYTHON, TestFramework.JUNIT, BuildTool.PYRONAUT))

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "Python applications must use the Pytest test framework"

        when:
        buildFeatureContext([], new Options(Language.PYTHON, TestFramework.PYTEST, BuildTool.GRADLE))

        then:
        ex = thrown(IllegalArgumentException)
        ex.message == "Python applications must use the Pyronaut build tool"
    }

    void "pyronaut and pytest cannot be selected for non-python languages"() {
        when:
        buildFeatureContext([], new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.PYRONAUT))

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "The Pyronaut build tool is only supported for Python applications"

        when:
        buildFeatureContext([], new Options(Language.JAVA, TestFramework.PYTEST, BuildTool.GRADLE))

        then:
        ex = thrown(IllegalArgumentException)
        ex.message == "The Pytest test framework is only supported for Python applications"
    }

    void "python rejects unsupported configuration logging and bootstrap features"() {
        when:
        buildGeneratorContext(["yaml"], new Options(Language.PYTHON))

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "Feature yaml is not supported for Python applications"

        when:
        buildGeneratorContext(["log4j2"], new Options(Language.PYTHON))

        then:
        ex = thrown(IllegalArgumentException)
        ex.message == "Feature log4j2 is not supported for Python applications"

        when:
        buildGeneratorContext(["aws-secrets-manager"], new Options(Language.PYTHON), ApplicationType.DEFAULT)

        then:
        ex = thrown(IllegalArgumentException)
        ex.message == "Feature aws-secrets-manager is not supported for Python because it requires bootstrap configuration"
    }

    void "python supports graalvm but rejects CI features"() {
        when:
        buildGeneratorContext(["graalvm"], new Options(Language.PYTHON))

        then:
        noExceptionThrown()

        when:
        buildGeneratorContext(["github-workflow-ci"], new Options(Language.PYTHON))

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "Feature github-workflow-ci is not supported for Python applications"
    }

    void "python supports google cloud http function applications"() {
        when:
        buildGeneratorContext(["google-cloud-function"], new Options(Language.PYTHON), ApplicationType.DEFAULT)

        then:
        noExceptionThrown()
    }

    void "python supports cli applications but rejects grpc applications"() {
        when:
        buildGeneratorContext([], new Options(Language.PYTHON), ApplicationType.CLI)

        then:
        noExceptionThrown()

        when:
        buildGeneratorContext([], new Options(Language.PYTHON), ApplicationType.GRPC)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "Python applications do not support the grpc application type"
    }

    void "python rejects jvm-specific HTTP client features"() {
        when:
        buildGeneratorContext(["http-client-jdk"], new Options(Language.PYTHON))

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "Feature http-client-jdk is not supported for Python applications"
    }

    void "python rejects jvm-specific management features"() {
        when:
        buildGeneratorContext(["jmx"], new Options(Language.PYTHON))

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "Feature jmx is not supported for Python applications"
    }

    void "python rejects alternate server and JVM test features"() {
        when:
        buildGeneratorContext(["jetty-server"], new Options(Language.PYTHON))

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "Feature jetty-server is not supported for Python applications"

        when:
        buildGeneratorContext(["json-path"], new Options(Language.PYTHON))

        then:
        ex = thrown(IllegalArgumentException)
        ex.message == "Feature json-path is not supported for Python applications"
    }

    void "python rejects features that require java reflection"(String feature) {
        when:
        buildGeneratorContext([feature], new Options(Language.PYTHON))

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "Feature ${feature} is not supported for Python because it requires Java reflection"

        where:
        feature << ["data-hibernate-reactive", "hibernate-jpa", "hibernate-reactive-jpa", "hibernate-validator", "jackson-databind"]
    }
}
