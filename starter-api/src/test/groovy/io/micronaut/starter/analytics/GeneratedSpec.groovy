package io.micronaut.starter.analytics

import io.micronaut.json.JsonMapper
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.JdkVersion
import io.micronaut.projectgen.core.options.Language
import io.micronaut.projectgen.core.options.TestFramework
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest
class GeneratedSpec extends Specification {

    @Inject
    JsonMapper jsonMapper

    void verifyGeneratedSerialization() {
        given:
        Generated generated = new Generated(ApplicationType.DEFAULT,
               Language.JAVA,
               BuildTool.GRADLE_KOTLIN,
               TestFramework.JUNIT,
               JdkVersion.JDK_17)
        when:
        String result = jsonMapper.writeValueAsString(generated)
        then:
        result.contains('"type":"DEFAULT"')
        result.contains('"language":"JAVA"')
        result.contains('"buildTool":"GRADLE_KOTLIN"')
        result.contains('"testFramework":"JUNIT"')
        result.contains('"jdkVersion":"JDK_17"')
        result.contains('"micronautVersion"')
    }
}
