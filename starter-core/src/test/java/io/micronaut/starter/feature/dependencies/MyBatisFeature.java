package io.micronaut.starter.feature.dependencies;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.context.annotation.Requires;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.test.TestFeature;
import io.micronaut.starter.options.BuildTool;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.List;

@Requires(property = "spec.name", value = "DependenciesFeatureSpec")
@Singleton
public class MyBatisFeature implements Feature, DependenciesFeature {
    private final MavenCoordinate mybatis;

    public MyBatisFeature(@Named("mybatis") MavenCoordinate mybatis) {
        this.mybatis = mybatis;
    }
    @Override
    public String getName() {
        return "mybatis";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public List<GradleDependency> getGradleDependencies(@Nullable TestFeature testFeature) {
        return Collections.singletonList(new GradleDependency(GradleConfiguration.IMPLEMENTATION, mybatis));
    }
}
