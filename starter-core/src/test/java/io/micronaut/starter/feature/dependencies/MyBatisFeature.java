package io.micronaut.starter.feature.dependencies;

import io.micronaut.context.annotation.Requires;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.feature.Feature;

import jakarta.inject.Singleton;

@Requires(property = "spec.name", value = "DependenciesFeatureSpec")
@Singleton
public class MyBatisFeature implements Feature {

    @Override
    public String getName() {
        return "mybatis";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder().lookupArtifactId("mybatis").compile());
    }
}
