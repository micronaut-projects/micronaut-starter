package io.micronaut.starter.feature

import io.micronaut.core.annotation.NonNull
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.JdkVersion
import io.micronaut.projectgen.core.options.Language
import io.micronaut.projectgen.core.options.TestFramework
import jakarta.inject.Singleton
import spock.lang.Unroll

class MinJdkFeatureSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Unroll
    void 'it is possible to require a minimum jdk for a feature'(Language language,
                                                                 TestFramework testfw,
                                                                 JdkVersion jdk) {
        when:
        new BuildBuilder(beanContext, BuildTool.MAVEN)
                .jdkVersion(jdk)
                .language(language)
                .features(['min-jdk'])
                .testFramework(testfw)
                .render()

        then:
        IllegalArgumentException e = thrown()
        e.message == "The selected feature min-jdk requires at latest Java 11"

        where:
        language        | testfw                | jdk
        Language.KOTLIN | TestFramework.KOTEST  | JdkVersion.JDK_8
    }

    @Singleton
    static class MockMinJdkFeature implements MinJdkFeature {
        @Override
        String getName() {
            "min-jdk"
        }

        @Override
        @NonNull
        JdkVersion minJdk() {
            JdkVersion.JDK_11
        }

        @Override
        boolean isVisible() {
            false
        }
    }
}
