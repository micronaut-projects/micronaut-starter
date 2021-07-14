package io.micronaut.starter.feature.server;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;

public abstract class AbstractMicronautServerFeature implements ServerFeature {

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut")
                .artifactId("micronaut-http-validation")
                .annotationProcessor());
        doApply(generatorContext);
    }

    public abstract void doApply(GeneratorContext generatorContext);
}
