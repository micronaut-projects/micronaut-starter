package io.micronaut.starter.feature.dependencies;

import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.context.annotation.Requires;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.test.TestFeature;
import org.jetbrains.annotations.NotNull;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Requires(property = "spec.name", value = "DependenciesFeatureSpec")
@Singleton
public class GebFeature implements Feature, DependenciesFeature {

    private final MavenCoordinate gebJunit;
    private final MavenCoordinate gebSpock;
    private final MavenCoordinate seleniumFirefoxDriver;
    private final MavenCoordinate seleniumSupport;

    public GebFeature(@Named("geb-spock") MavenCoordinate gebSpock,
                      @Named("geb-junit5") MavenCoordinate gebJunit,
                      @Named("selenium-firefox-driver") MavenCoordinate seleniumFirefoxDriver,
                      @Named("selenium-support") MavenCoordinate seleniumSupport) {
        this.gebSpock = gebSpock;
        this.gebJunit = gebJunit;
        this.seleniumFirefoxDriver = seleniumFirefoxDriver;
        this.seleniumSupport = seleniumSupport;
    }

    @NotNull
    @Override
    public String getName() {
        return "geb";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public List<GradleDependency> getGradleDependencies(@Nullable TestFeature testFeature) {
        List<GradleDependency> coordinates = new ArrayList<>();
        coordinates.add(new GradleDependency(GradleConfiguration.TEST_RUNTIME_ONLY, seleniumFirefoxDriver));
        coordinates.add(new GradleDependency(GradleConfiguration.TEST_RUNTIME_ONLY,seleniumSupport));
        if (testFeature != null) {
            if (testFeature.isSpock()) {
                coordinates.add(new GradleDependency(GradleConfiguration.TEST_IMPLEMENTATION, gebSpock));
            } else if (testFeature.isJunit()) {
                coordinates.add(new GradleDependency(GradleConfiguration.TEST_IMPLEMENTATION, gebJunit));
            }
        }
        return coordinates;
    }
}
