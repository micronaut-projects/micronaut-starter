package io.micronaut.starter.feature.dependencies;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.options.TestFramework;

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
    public boolean supports(ApplicationType applicationType) {
        return true;
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
