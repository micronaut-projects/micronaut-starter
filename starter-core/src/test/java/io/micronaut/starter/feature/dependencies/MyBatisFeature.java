package io.micronaut.starter.feature.dependencies;

import io.micronaut.context.annotation.Requires;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Feature;

import jakarta.inject.Singleton;

@Requires(property = "spec.name", value = "DependenciesFeatureSpec")
@Singleton
public class MyBatisFeature implements Feature {

    @Override
    public String getName() {
        return "mybatis";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder().lookupArtifactId("mybatis").compile());
    }
}
