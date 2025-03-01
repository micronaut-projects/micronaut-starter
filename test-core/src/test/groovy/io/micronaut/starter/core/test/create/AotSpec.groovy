package io.micronaut.starter.core.test.create

import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.starter.feature.build.MicronautAot
import io.micronaut.starter.feature.security.SecurityOAuth2
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.Language
import io.micronaut.starter.test.CommandSpec
import io.micronaut.starter.test.LanguageBuildCombinations

class AotSpec extends CommandSpec {

    void 'create-app with feature micronaut-aot for #lang and #buildTool starts successfully'(Language lang, BuildTool buildTool) {
        given:
        generateProject(lang, buildTool, [MicronautAot.FEATURE_NAME_AOT] as List<String>, ApplicationType.DEFAULT)

        when:
        String output = executeBuild(buildTool, "test")

        then:
        output.contains("BUILD SUCCESS")

        where:
        [lang, buildTool] << LanguageBuildCombinations.combinations()
    }

    void 'create-app with feature micronaut-aot and security-oauth2 for #lang and #buildTool starts successfully'(Language lang, BuildTool buildTool) {
        given:
        generateProject(lang, buildTool, [MicronautAot.FEATURE_NAME_AOT, SecurityOAuth2.NAME] as List<String>, ApplicationType.DEFAULT)

        when:
        String output = executeBuild(buildTool, "test")

        then:
        output.contains("BUILD SUCCESS")

        where:
        [lang, buildTool] << LanguageBuildCombinations.combinations()
    }

    @Override
    String getTempDirectoryPrefix() {
        'test-aot'
    }
}
