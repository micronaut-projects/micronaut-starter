package io.micronaut.starter.feature.discovery

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.httpclient.HttpClientJdk
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.util.LanguageUtils

class DiscoveryClientSpec extends BeanContextSpec {
    void "dependencies for discovery client feature"(BuildTool buildTool, Language language) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features([DiscoveryClient.NAME])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        verifier.hasDependency("io.micronaut.discovery", "micronaut-discovery-client", Scope.COMPILE)
        verifier.hasDependency("io.micronaut", "micronaut-http-client", Scope.COMPILE)
        !verifier.hasDependency("io.micronaut", "micronaut-http-client", Scope.TEST)

        where:
        [buildTool, language] << [BuildTool.values(), Language.values()].combinations().findAll { supportedLanguages(it[0]).contains(it[1]) }
    }

    void "dependencies for discovery client and http-client-jdk"(BuildTool buildTool, Language language) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features([DiscoveryClient.NAME, HttpClientJdk.NAME])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        verifier.hasDependency("io.micronaut.discovery", "micronaut-discovery-client", Scope.COMPILE)
        verifier.hasDependency("io.micronaut", HttpClientJdk.ARTIFACT_ID_MICRONAUT_HTTP_CLIENT_JDK, Scope.COMPILE)
        !verifier.hasDependency("io.micronaut", "micronaut-http-client", Scope.COMPILE)
        !verifier.hasDependency("io.micronaut", "micronaut-http-client", Scope.TEST)

        where:
        [buildTool, language] << [BuildTool.values(), LanguageUtils.JVM_LANGUAGES].combinations().findAll { supportedLanguages(it[0]).contains(it[1]) }
    }
}
