package io.micronaut.starter.feature.oracecloud

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.config.Yaml
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.util.LanguageUtils
import spock.lang.Unroll

class OracleCloudSdkSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Unroll
    void 'test Oracle Cloud SDK feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['oracle-cloud-sdk'])
                .language(language)
                .render()
        then:
        template.contains('implementation("io.micronaut.oraclecloud:micronaut-oraclecloud-sdk")')

        where:
        language << LanguageUtils.JVM_LANGUAGES
    }

    @Unroll
    void 'test Oracle Cloud SDK feature for maven and language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['oracle-cloud-sdk'])
                .language(language)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.MAVEN, language, template)

        then:
        verifier.hasDependency("io.micronaut.oraclecloud", "micronaut-oraclecloud-sdk", Scope.COMPILE)

        where:
        language << supportedLanguages(BuildTool.MAVEN)
    }

    void 'test Oracle Cloud SDK config file'() {
        when:
        Map<String, String> output = generate(['oracle-cloud-sdk', Yaml.NAME])
        String config = output["src/main/resources/application.yml"]

        then:
        config.contains('oci.config.profile: DEFAULT')
    }

}
