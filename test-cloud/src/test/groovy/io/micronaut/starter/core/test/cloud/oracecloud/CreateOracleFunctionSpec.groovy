package io.micronaut.starter.core.test.cloud.oracecloud

import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.projectgen.core.options.OperatingSystem
import io.micronaut.projectgen.core.generator.ProjectGenerator
import io.micronaut.projectgen.core.io.ConsoleOutput
import io.micronaut.projectgen.core.io.FileSystemOutputHandler
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.JdkVersion
import io.micronaut.projectgen.core.options.Language
import io.micronaut.starter.options.MicronautJdkVersionConfiguration
import io.micronaut.projectgen.core.options.Options
import io.micronaut.projectgen.core.options.TestFramework
import io.micronaut.starter.test.ApplicationTypeCombinations
import io.micronaut.starter.test.BuildToolCombinations
import io.micronaut.starter.test.CommandSpec
import io.micronaut.projectgen.core.utils.NameUtils
import io.micronaut.starter.util.VersionInfo
import spock.lang.Retry

@Retry // can fail on CI due to port binding race condition, so retry
class CreateOracleFunctionSpec extends CommandSpec{
    @Override
    String getTempDirectoryPrefix() {
        "test-oraclefunction"
    }

    void 'create-#applicationType with features oracle-function #lang and #build and test framework: #testFramework'(ApplicationType applicationType,
                                                                                                                           Language lang,
                                                                                                                           BuildTool build,
                                                                                                                           TestFramework testFramework) {
        given:
        List<String> features = ['oracle-function']
        generateProject(lang, build, features, applicationType, testFramework)

        when:
        String output = executeBuild(build, "testClasses")

        then:
        output.contains("BUILD SUCCESS")

        where:
        [applicationType, lang, build, testFramework] << ApplicationTypeCombinations.combinations([ApplicationType.DEFAULT], Language.values() as List<Language>, BuildTool.valuesGradle())
    }

    void 'create-#applicationType with features oracle-function and "-" in the app name #lang and #build and test framework: #testFramework'(ApplicationType applicationType, Language lang, BuildTool build,
                                                                                                                     TestFramework testFramework) {
        given:
        List<String> features = ['oracle-function']
        JdkVersion jdkVersion = VersionInfo.getJavaVersion()
        if (jdkVersion.greaterThanEqual(MicronautJdkVersionConfiguration.DEFAULT_OPTION)) {
            jdkVersion = MicronautJdkVersionConfiguration.DEFAULT_OPTION
        }
        beanContext.getBean(ProjectGenerator).generate(applicationType,
                NameUtils.parse("example.micronaut.foo-test"),
                new Options(lang, testFramework, build, jdkVersion),
                OperatingSystem.LINUX,
                features,
                new FileSystemOutputHandler(dir, ConsoleOutput.NOOP),
                ConsoleOutput.NOOP
        )

        when:
        String output = executeBuild(build, "testClasses")

        then:
        output.contains("BUILD SUCCESS")

        where:
        [applicationType, lang, build, testFramework] << ApplicationTypeCombinations.combinations([ApplicationType.DEFAULT], Language.values() as List<Language>, BuildTool.valuesGradle())
    }


    void 'default application with features oracle-function, #serializationFeature, #lang and #build and test framework: #testFramework'(
            Language lang,
            String serializationFeature,
            BuildTool build,
            TestFramework testFramework
    ) {
        given:
        List<String> features = ['oracle-function'] + serializationFeature
        generateProject(lang, build, features, ApplicationType.DEFAULT, testFramework)

        when:
        String output = executeBuild(build, "test")

        then:
        output.contains("BUILD SUCCESS")

        where:
        [lang, serializationFeature, build, testFramework] << [
                Language.values(),
                ['serialization-jackson', 'serialization-bson', 'serialization-jsonp'],
                BuildToolCombinations.buildTools,
                TestFramework.values()
        ].combinations()
                .stream()
                .filter(it -> !(
                it[1] == 'serialization-jsonp' || (it[1] == 'serialization-bson' && it[0] == Language.KOTLIN)
                ))
    }
}
