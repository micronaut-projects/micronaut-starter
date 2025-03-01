package io.micronaut.starter.feature.dependencies;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.options.TestFramework;

import jakarta.inject.Singleton;

@Requires(property = "spec.name", value = "DependenciesFeatureSpec")
@Singleton
public class GebFeature implements Feature {

    public GebFeature() {
    }

    @NonNull
    @Override
    public String getName() {
        return "geb";
    }

    @Override
    public void apply(GeneratorContext context) {
        if (context.getTestFramework() == TestFramework.JUNIT) {
            context.addDependency(Dependency.builder().lookupArtifactId("geb-junit5").test());
        } else if (context.getTestFramework() == TestFramework.SPOCK) {
            context.addDependency(Dependency.builder().lookupArtifactId("geb-spock").test());
        }
        context.addDependency(Dependency.builder().lookupArtifactId("selenium-firefox-driver").testRuntime());
        context.addDependency(Dependency.builder().lookupArtifactId("selenium-support").testRuntime());
    }
}
