package io.micronaut.starter.build.dependencies

import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.projectgen.core.generator.GeneratorContext
import io.micronaut.projectgen.core.feature.Feature
import jakarta.inject.Singleton

@Singleton
class FeatureWithDuplicates implements Feature {

    private static final String GROUP = "org.gebish"
    private static final String ART_GEB_CORE = "geb-core"
    private static final String GROUP_SELENIUM = "org.seleniumhq.selenium"
    private static final String ART_SELENIUM_FIREFOX = "selenium-firefox-driver"



    @Override
    String getName() {
        return "feature-with-duplicates"
    }

    @Override
    boolean isVisible() {
        false
    }

    @Override
    void apply(GeneratorContext ctx) {
        ctx.addDependency(Dependency.builder().groupId(GROUP).artifactId(ART_GEB_CORE).runtime().build())
        ctx.addDependency(Dependency.builder().groupId(GROUP).artifactId(ART_GEB_CORE).compile().build())
        ctx.addDependency(Dependency.builder().groupId(GROUP).artifactId(ART_GEB_CORE).compileOnly().build())
        ctx.addDependency(Dependency.builder().groupId(GROUP).artifactId(ART_GEB_CORE).test().build())
        ctx.addDependency(Dependency.builder().groupId(GROUP).artifactId(ART_GEB_CORE).testCompileOnly().build())
        ctx.addDependency(Dependency.builder().groupId(GROUP).artifactId(ART_GEB_CORE).testRuntime().build())

        ctx.addDependency(Dependency.builder().groupId(GROUP_SELENIUM).artifactId(ART_SELENIUM_FIREFOX).testRuntime().build())
        ctx.addDependency(Dependency.builder().groupId(GROUP_SELENIUM).artifactId(ART_SELENIUM_FIREFOX).test().build())
        ctx.addDependency(Dependency.builder().groupId(GROUP_SELENIUM).artifactId(ART_SELENIUM_FIREFOX).runtime().build())
    }
}
