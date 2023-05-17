package io.micronaut.starter.springboot;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.feature.GradleSpecificFeature;
import jakarta.inject.Singleton;

@Singleton
public class SpringBootGradlePlugin implements GradleSpecificFeature {
    private static final String ARTIFACT_ID = "spring-boot-gradle-plugin";
    @Override
    public String getName() {
        return "springboot-gradle-plugin";
    }

    @Override
    public String getTitle() {
        return "Spring Boot Gradle Plugin";
    }

    @Override
    public String getDescription() {
        return "Adds the Spring Boot Gradle Plugin provides Spring Boot support in Gradle.";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.spring.io/spring-boot/docs/current/gradle-plugin/reference/htmlsingle/";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {

        if (generatorContext.getBuildTool().isGradle()) {
            generatorContext.addBuildPlugin(GradlePlugin.builder()
                    .id("org.springframework.boot")
                    .lookupArtifactId(ARTIFACT_ID)
                    .build());
        }
    }
}
