package io.micronaut.starter.springboot;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.BuildProperties;
import io.micronaut.starter.build.dependencies.CoordinateResolver;
import io.micronaut.starter.build.maven.MavenPlugin;
import io.micronaut.starter.build.maven.ParentPom;
import io.micronaut.starter.build.maven.ParentPomFeature;
import jakarta.inject.Singleton;

@Singleton
public class SpringBootMavenPlugin implements ParentPomFeature, SpringDefaultFeature {

    private final CoordinateResolver coordinateResolver;

    public SpringBootMavenPlugin(CoordinateResolver coordinateResolver) {
        this.coordinateResolver = coordinateResolver;
    }

    @Override
    public ParentPom getParentPom() {
        return coordinateResolver.resolve("spring-boot-starter-parent")
                .map(coordinate -> new ParentPom(coordinate.getGroupId(), coordinate.getArtifactId(), coordinate.getVersion(), true))
                .orElseThrow();
    }

    @Override
    public String getName() {
        return "spring-boot-maven-plugin";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addHelpLink("Official Apache Maven documentation", "https://maven.apache.org/guides/index.html");
        generatorContext.addHelpLink("Spring Boot Maven Plugin Reference", "https://docs.spring.io/spring-boot/docs/3.1.1/maven-plugin/reference/html/");
        generatorContext.addBuildPlugin(MavenPlugin.builder()
                        .groupId(SpringBootDependencies.GROUP_ID_ORG_SPRINGFRAMEWORK_BOOT)
                        .artifactId("spring-boot-maven-plugin")
                .build());
        BuildProperties buildProperties = generatorContext.getBuildProperties();
        buildProperties.put("java.version", "" + generatorContext.getJdkVersion().majorVersion());
    }
}
