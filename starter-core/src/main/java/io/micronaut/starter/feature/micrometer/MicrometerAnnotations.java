package io.micronaut.starter.feature.micrometer;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.other.Management;
import io.micronaut.starter.feature.server.MicronautServerDependent;
import io.micronaut.starter.util.NameUtils;
import jakarta.inject.Singleton;

@Singleton
public class MicrometerAnnotations implements Feature, MicronautServerDependent {

    private final Core core;
    private final Management management;

    public MicrometerAnnotations(Core core, Management management) {
        this.core = core;
        this.management = management;
    }

    @Override
    public String getName() {
        return "micrometer-annotation";
    }

    @Override
    public String getTitle() {
        return NameUtils.getNaturalName(io.micronaut.core.naming.NameUtils.dehyphenate(getName()));
    }

    @Override
    public String getDescription() {
        return "Adds support for Micrometer annotations (@Timed and @Counted)";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(Core.class)) {
            featureContext.addFeature(core);
        }
        if (!featureContext.isPresent(Management.class)) {
            featureContext.addFeature(management);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.micrometer")
                .artifactId("micronaut-micrometer-annotation")
                .versionProperty("micronaut.micrometer.version")
                .annotationProcessor());
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT;
    }

    @Override
    public String getCategory() {
        return Category.MANAGEMENT;
    }
}
