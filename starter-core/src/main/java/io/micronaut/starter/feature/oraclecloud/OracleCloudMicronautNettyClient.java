package io.micronaut.starter.feature.oraclecloud;


import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import jakarta.inject.Singleton;


@Singleton
public class OracleCloudMicronautNettyClient implements Feature {

    @Override
    public String getName() {
        return "oracle-cloud-httpclient-netty";
    }

    @Override
    public String getTitle() {
        return "Micronaut Netty Oracle Cloud Client";
    }

    @Override
    public String getDescription() {
        return "Provides the netty micronaut client for oraclecloud";
    }

    @Nullable
    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-oracle-cloud/latest/guide/";
    }

    @Override
    public String getCategory() {
        return Category.CLOUD;
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(MicronautDependencyUtils.oracleCloudDependency()
                .artifactId("micronaut-oraclecloud-httpclient-netty")
                .compile());
    }
}
