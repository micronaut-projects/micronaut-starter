package io.micronaut.starter.core.test.create;

import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.starter.test.CommandSpec;
import spock.lang.Unroll;

class KspSpec extends CommandSpec {

    @Unroll
    void 'create-app with feature ksp for #lang and #buildTool starts successfully'(Language lang, BuildTool buildTool) {
        given:
        generateProject(lang, buildTool, ['ksp'] as List<String>, ApplicationType.DEFAULT)

        when:
        String output = executeBuild(buildTool, "test")

        then:
        output.contains("BUILD SUCCESS")

        where:
        [lang, buildTool] << [[Language.KOTLIN], BuildTool.valuesGradle()].combinations()
    }

    @Override
    String getTempDirectoryPrefix() {
        'test-ksp'
    }
}
