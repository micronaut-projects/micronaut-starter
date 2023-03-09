package io.micronaut.starter

import io.micronaut.context.ApplicationContext
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.fixture.ContextFixture
import io.micronaut.starter.fixture.ProjectFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

abstract class BeanContextSpec extends Specification implements ProjectFixture, ContextFixture {

    @Shared
    @AutoCleanup
    ApplicationContext beanContext = ApplicationContext.run(getProperties())

    Map<String, String> getProperties() {
        return Collections.emptyMap();
    }

    BuildTestVerifier verifier(BuildTool buildTool,
                                       Language language,
                                       List<String> features,
                                       ApplicationType applicationType = ApplicationType.FUNCTION) {
        String template = template(buildTool, language, features, applicationType)
        BuildTestUtil.verifier(buildTool, template)
    }

    String template(BuildTool buildTool,
                            Language language,
                            List<String> features,
                            ApplicationType applicationType = ApplicationType.FUNCTION) {
        new BuildBuilder(beanContext, buildTool)
                .features(features)
                .language(language)
                .applicationType(applicationType)
                .render()
    }
}
