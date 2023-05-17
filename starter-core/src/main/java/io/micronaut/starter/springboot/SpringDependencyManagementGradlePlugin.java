package io.micronaut.starter.springboot;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.feature.GradleSpecificFeature;
import jakarta.inject.Singleton;

@Singleton
public class SpringDependencyManagementGradlePlugin implements GradleSpecificFeature {
    private static final String ARTIFACT_ID = "dependency-management-plugin";

    @Override
    public String getName() {
        return "spring-dependency-management-gradle-plugin";
    }

    @Override
    public String getTitle() {
        return "Spring Dependency Management Plugin Gradle Plugin";
    }

    @Override
    public String getDescription() {
        return "Adds the Spring Dependency Management Plugin Gradle Plugin that provides Maven-like dependency management and exclusions.";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.spring.io/dependency-management-plugin/docs/current-SNAPSHOT/reference/html/";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {

        if (generatorContext.getBuildTool().isGradle()) {
            generatorContext.addBuildPlugin(GradlePlugin.builder()
                    .id("io.spring.dependency-management")
                    .lookupArtifactId(ARTIFACT_ID)
                    .build());
        }
    }
}
