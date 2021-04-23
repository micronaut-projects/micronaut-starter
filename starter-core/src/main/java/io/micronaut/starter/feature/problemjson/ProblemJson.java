package io.micronaut.starter.feature.problemjson;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;

import javax.inject.Singleton;

@Singleton
public class ProblemJson implements Feature {

    @NonNull
    @Override
    public String getName() {
        return "problem-json";
    }

    @Override
    public String getTitle() {
        return "Problem JSON";
    }

    @Override
    public String getDescription() {
        return "Produce application/problem+json responses from a Micronaut application";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-problem-json/latest/guide/index.html";
    }

    @Override
    public boolean isPreview() {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.problem")
                .artifactId("micronaut-problem-json")
                .compile());
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.VALIDATION;
    }
}
