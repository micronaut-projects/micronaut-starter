package io.micronaut.starter.feature.spring;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.util.VersionInfo;

import java.util.Map;

public abstract class SpringFeature implements Feature {

    protected final Spring spring;

    protected SpringFeature(Spring spring) {
        this.spring = spring;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(Spring.class)) {
            featureContext.addFeature(spring);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        Map.Entry<String, String> dependencyVersion = VersionInfo.getDependencyVersion("micronaut.spring");
        if (generatorContext.getBuildTool() == BuildTool.MAVEN) {
            generatorContext.getBuildProperties().put(
                    dependencyVersion.getKey(),
                    dependencyVersion.getValue()
            );
        }
    }

    @Override
    public String getCategory() {
        return Category.SPRING;
    }
}
