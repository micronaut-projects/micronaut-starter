package io.micronaut.starter.feature.build.pyronaut

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.util.VersionInfo

class PyronautSpec extends BeanContextSpec implements CommandOutputFixture {

    void "python generates a pyronaut project"() {
        when:
        Map<String, String> output = generate(
                ApplicationType.DEFAULT,
                new Options(Language.PYTHON, null, null),
                []
        )

        then:
        output.containsKey("pyproject.toml")
        output.containsKey("config/application.toml")
        output.containsKey("src/main.py")
        !output.containsKey("src/example/micronaut/controller.py")
        !output.containsKey("src/example/micronaut/services.py")
        output.containsKey("tests/test_application.py")
        output.containsKey(".gitignore")
        output.containsKey("README.md")
        output.containsKey(".agents/skills/pyronaut-project/SKILL.md")
        output.containsKey(".agents/skills/pyronaut-cli/SKILL.md")
        output.containsKey(".agents/skills/pyronaut-coding/SKILL.md")

        and:
        !output.containsKey("build.gradle")
        !output.containsKey("build.gradle.kts")
        !output.containsKey("pom.xml")
        !output.containsKey("src/main/resources/logback.xml")
        !output["tests/test_application.py"].contains("MessageService")

        and:
        output["micronaut-cli.yml"].contains("sourceLanguage: python")
        output["micronaut-cli.yml"].contains("testFramework: pytest")
        output["micronaut-cli.yml"].contains("buildTool: pyronaut")
        !output["micronaut-cli.yml"].contains("micronaut-configuration-validation-gradle-plugin")

        and:
        String pyproject = output["pyproject.toml"]
        pyproject.contains("[tool.pyronaut]")
        pyproject.contains('[tool.setuptools]\npackage-dir = {"" = "src"}')
        pyproject.contains('[tool.setuptools.packages.find]\nwhere = ["src"]')
        !pyproject.contains("[tool.pyronaut]\nversion =")
        pyproject.contains("[tool.pyronaut.core]\nversion = \"${VersionInfo.micronautCoreVersion}\"")
        pyproject.contains("[tool.pyronaut.platform]\nversion = \"${VersionInfo.micronautVersion}\"")
        pyproject.contains('[tool.pyronaut.toolchain]\ntype = "jvm"')
        pyproject.contains('[tool.pyronaut.processor]\nincremental = true\ndaemon = true\npython-incremental-mode = "optimistic"')
        pyproject.contains('repositories = ["https://central.sonatype.com/repository/maven-snapshots/", "mavenCentral"')
        !pyproject.contains("mavenLocal")
        pyproject.contains('"io.micronaut:micronaut-http-server-netty"')
        pyproject.contains('"io.micronaut.serde:micronaut-serde-processor"')
        pyproject.contains('"io.micronaut.pyronaut:micronaut-pyronaut-logback"')
        pyproject.contains('"io.micronaut.pyronaut:micronaut-pyronaut-pytest"')
        pyproject.contains('"io.micronaut.pyronaut:micronaut-pyronaut-requests"')
        pyproject.contains("[tool.pyronaut.test-resources]\nenabled = false\ninfer-classpath = false")

        and:
        output["config/application.toml"].contains("[micronaut.application]\nname = 'foo'")
        output["config/application.toml"].contains("[micronaut.executors.blocking]\ntype = 'CACHED'\nvirtual = false")
        output["README.md"].contains("graalpy -m venv .venv")
        output["README.md"].contains("python -m pip install --upgrade pip pytest")
        output["README.md"].contains("pyronaut install")
        output["README.md"].contains("schema directives used by IDEs")
        !output["README.md"].contains("--project-dir .")
        output[".gitignore"].contains("__pyronaut__/")

        and:
        String projectSkill = output[".agents/skills/pyronaut-project/SKILL.md"]
        projectSkill.contains("name: pyronaut-project")
        projectSkill.contains("`src/`: application Python sources")
        projectSkill.contains("[tool.pyronaut.dependencies]")
        projectSkill.contains("Avoid `bootstrap.properties` and `bootstrap.toml`")

        and:
        String cliSkill = output[".agents/skills/pyronaut-cli/SKILL.md"]
        cliSkill.contains("name: pyronaut-cli")
        cliSkill.contains("`--project-dir .` is unnecessary")
        cliSkill.contains("pyronaut validate-config --scenario run|test|production")
        cliSkill.contains("PYRONAUT_TRACE_DELEGATION=true")

        and:
        String codingSkill = output[".agents/skills/pyronaut-coding/SKILL.md"]
        codingSkill.contains("name: pyronaut-coding")
        codingSkill.contains("Prefer classless route modules")
        codingSkill.contains("from micronaut.http.annotation import Get")
        codingSkill.contains("not `from io.micronaut.http.annotation import Get`")
        codingSkill.contains("from micronaut.core.async_.annotation import SingleResult")
        codingSkill.contains("do not use `micronaut-jackson-databind`, `hibernate-jpa`, or `hibernate-validator`")
    }

    void "pyronaut supports database dependencies and test resources metadata"() {
        when:
        Map<String, String> output = generate(
                ApplicationType.DEFAULT,
                new Options(Language.PYTHON, TestFramework.PYTEST, BuildTool.PYRONAUT),
                ["data-jdbc", "mysql"]
        )

        then:
        String pyproject = output["pyproject.toml"]
        pyproject.contains("[tool.pyronaut.test-resources]")
        pyproject.contains("enabled = true")
        pyproject.contains("infer-classpath = true")
        pyproject.contains('"io.micronaut.data:micronaut-data-jdbc"')
        pyproject.contains('"io.micronaut.data:micronaut-data-processor"')
        pyproject.contains('"io.micronaut.sql:micronaut-jdbc-hikari"')
        pyproject.contains('"com.mysql:mysql-connector-j"')

        and:
        output["config/application.toml"].contains("[datasources.default]")
        output["config/application.toml"].contains("db-type = 'mysql'")
    }

    void "pyronaut includes Micrometer's runtime registry classes"() {
        when:
        Map<String, String> output = generate(
                ApplicationType.DEFAULT,
                new Options(Language.PYTHON, TestFramework.PYTEST, BuildTool.PYRONAUT),
                ["micrometer-annotation"]
        )

        then:
        output["pyproject.toml"].contains('"io.micrometer:micrometer-core"')
    }

    void "pyronaut pins Flyway to the native-compatible version"() {
        when:
        Map<String, String> output = generate(
                ApplicationType.DEFAULT,
                new Options(Language.PYTHON, TestFramework.PYTEST, BuildTool.PYRONAUT),
                ["flyway"]
        )

        then:
        output["pyproject.toml"].contains('"io.micronaut.flyway:micronaut-flyway:8.1.1"')
    }

    void "pyronaut writes test resources additional modules for database drivers"() {
        when:
        Map<String, String> output = generate(
                ApplicationType.DEFAULT,
                new Options(Language.PYTHON, TestFramework.PYTEST, BuildTool.PYRONAUT),
                ["mysql"]
        )

        then:
        output["pyproject.toml"].contains("[tool.pyronaut.test-resources]")
        output["pyproject.toml"].contains("enabled = true")
        output["pyproject.toml"].contains('additional-modules = ["jdbc-mysql"]')
    }

    void "pyronaut suppresses r2dbc connection validation when test resources supplies the database"() {
        when:
        Map<String, String> output = generate(
                ApplicationType.DEFAULT,
                new Options(Language.PYTHON, TestFramework.PYTEST, BuildTool.PYRONAUT),
                ["data-r2dbc", "flyway", "mysql", "test-resources"]
        )

        then:
        String pyproject = output["pyproject.toml"]
        pyproject.contains("[tool.pyronaut.validation]")
        pyproject.contains('suppressions = ["micronaut.test-resources*", "datasources.*.dialect", "r2dbc.datasources.*"]')
    }

    void "pyronaut writes management endpoint validation suppressions"() {
        when:
        Map<String, String> output = generate(
                ApplicationType.DEFAULT,
                new Options(Language.PYTHON, TestFramework.PYTEST, BuildTool.PYRONAUT),
                ["management"]
        )

        then:
        String pyproject = output["pyproject.toml"]
        pyproject.contains("[tool.pyronaut.validation]")
        pyproject.contains('suppressions = ["endpoints.*.enabled", "endpoints.*.sensitive"]')
    }

    void "pyronaut writes micrometer cloudwatch validation suppressions"() {
        when:
        Map<String, String> output = generate(
                ApplicationType.DEFAULT,
                new Options(Language.PYTHON, TestFramework.PYTEST, BuildTool.PYRONAUT),
                ["micrometer-cloudwatch"]
        )

        then:
        String pyproject = output["pyproject.toml"]
        pyproject.contains("[tool.pyronaut.validation]")
        pyproject.contains('suppressions = ["endpoints.*.enabled", "endpoints.*.sensitive", "micronaut.metrics.enabled", "micronaut.metrics.binders.*", "aws.*"]')
    }

    void "pyronaut writes aws cloud feature validation suppressions"() {
        when:
        Map<String, String> output = generate(
                ApplicationType.DEFAULT,
                new Options(Language.PYTHON, TestFramework.PYTEST, BuildTool.PYRONAUT),
                ["object-storage-aws"]
        )

        then:
        String pyproject = output["pyproject.toml"]
        pyproject.contains("[tool.pyronaut.validation]")
        pyproject.contains('suppressions = ["aws.*"]')
    }

    void "pyronaut writes opentelemetry validation suppressions"() {
        when:
        Map<String, String> output = generate(
                ApplicationType.DEFAULT,
                new Options(Language.PYTHON, TestFramework.PYTEST, BuildTool.PYRONAUT),
                ["tracing-opentelemetry-gcp"]
        )

        then:
        String pyproject = output["pyproject.toml"]
        pyproject.contains("[tool.pyronaut.validation]")
        pyproject.contains('suppressions = ["otel.*"]')
    }

    void "pyronaut writes security oauth2 validation suppressions"() {
        when:
        Map<String, String> output = generate(
                ApplicationType.DEFAULT,
                new Options(Language.PYTHON, TestFramework.PYTEST, BuildTool.PYRONAUT),
                ["security-oauth2"]
        )

        then:
        String pyproject = output["pyproject.toml"]
        pyproject.contains("[tool.pyronaut.validation]")
        pyproject.contains('suppressions = ["micronaut.security.oauth2.clients.*.token.auth-method"]')
    }

    void "pyronaut supports liquibase without the JUL bridge"() {
        when:
        Map<String, String> output = generate(
                ApplicationType.DEFAULT,
                new Options(Language.PYTHON, TestFramework.PYTEST, BuildTool.PYRONAUT),
                ["data-jdbc", "liquibase", "postgres"]
        )

        then:
        String pyproject = output["pyproject.toml"]
        pyproject.contains('"io.micronaut.liquibase:micronaut-liquibase"')
        pyproject.contains('"com.mattbertolini:liquibase-slf4j')
        !pyproject.contains("jul-to-slf4j")

        and:
        output["config/application.toml"].contains("change-log = 'classpath:db/liquibase-changelog.xml'")
        output.containsKey("config/db/liquibase-changelog.xml")
        output.containsKey("config/db/changelog/01-schema.xml")
        !output.containsKey("src/main/resources/db/liquibase-changelog.xml")
        !output.containsKey("src/main/resources/db/changelog/01-schema.xml")
        !output.containsKey("src/main/resources/logging.properties")
    }

    void "pyronaut supports cloud security messaging views and dependency only serverless features"() {
        when:
        Map<String, String> output = generate(
                ApplicationType.DEFAULT,
                new Options(Language.PYTHON, TestFramework.PYTEST, BuildTool.PYRONAUT),
                ["aws-v2-sdk", "security-jwt", "kafka", "views-thymeleaf", "aws-lambda-events-serde"]
        )

        then:
        String pyproject = output["pyproject.toml"]
        pyproject.contains('"io.micronaut.aws:micronaut-aws-sdk-v2"')
        pyproject.contains('"io.micronaut.security:micronaut-security-jwt"')
        pyproject.contains('"io.micronaut.security:micronaut-security-processor"')
        pyproject.contains('"io.micronaut.kafka:micronaut-kafka"')
        pyproject.contains('"io.micronaut.views:micronaut-views-thymeleaf"')
        pyproject.contains('"io.micronaut.aws:micronaut-aws-lambda-events-serde"')

        and:
        output.containsKey("src/main/resources/views/layout.html")
        output["config/application.toml"].contains("[micronaut.security]")
        output["config/application.toml"].contains("authentication = 'bearer'")
        output["config/application.toml"].contains("token.jwt.signatures.secret.generator.secret")
        output["pyproject.toml"].contains("[tool.pyronaut.test-resources]")
        output["pyproject.toml"].contains("enabled = true")
    }

    void "pyronaut supports spring security crypto"() {
        when:
        Map<String, String> output = generate(
                ApplicationType.DEFAULT,
                new Options(Language.PYTHON, TestFramework.PYTEST, BuildTool.PYRONAUT),
                ["spring-security-crypto"]
        )

        then:
        String pyproject = output["pyproject.toml"]
        pyproject.contains('"org.springframework.security:spring-security-crypto:6.2.0"')
        pyproject.contains('"org.slf4j:jcl-over-slf4j"')
    }
}
