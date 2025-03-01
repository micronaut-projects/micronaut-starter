package io.micronaut.starter.build

import groovy.transform.CompileStatic
import io.micronaut.starter.build.gradle.GradleBuildTestVerifier
import io.micronaut.starter.build.maven.MavenBuildTestVerifier
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.Language
import io.micronaut.projectgen.core.options.TestFramework

@CompileStatic
class BuildTestUtil {

    static BuildTestVerifier verifier(BuildTool buildTool,
                                      Language language,
                                      TestFramework testFramework,
                                      String template) {
        buildTool.isGradle() ? new GradleBuildTestVerifier(buildTool, template, language, testFramework) : new MavenBuildTestVerifier(template, language)
    }

    static BuildTestVerifier verifier(BuildTool buildTool,
                                      Language language,
                                      String template) {
        verifier(buildTool, language, language.getDefaults().getTest(), template)
    }

    static BuildTestVerifier verifier(BuildTool buildTool,
                                      String template) {
        Language language = Language.DEFAULT_OPTION
        verifier(buildTool, language, template)
    }
}
