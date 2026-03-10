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
import org.apache.commons.codec.language.bm.Lang
import spock.lang.Unroll

class OracleCloudMicronautNettyClientSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Unroll
    void 'test Oracle Cloud SDK feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['oracle-cloud-httpclient-netty'])
                .language(language)
                .render()
        then:
        template.contains('implementation("io.micronaut.oraclecloud:micronaut-oraclecloud-httpclient-netty")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test Oracle Cloud SDK feature for maven and language=#language'(Language language) {
        when:
        BuildTool buildTool = BuildTool.MAVEN
        String template = new BuildBuilder(beanContext, buildTool)
                .features(['oracle-cloud-httpclient-netty'])
                .language(language)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut.oraclecloud", "micronaut-oraclecloud-httpclient-netty", Scope.COMPILE)

        where:
        language << Language.values()
    }

}
