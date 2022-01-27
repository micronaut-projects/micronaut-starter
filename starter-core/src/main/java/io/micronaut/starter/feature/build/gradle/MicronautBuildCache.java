package io.micronaut.starter.feature.build.gradle;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.gradle.GradleDsl;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.build.gradle.templates.buildCache;
import io.micronaut.starter.template.RockerWritable;
import jakarta.inject.Singleton;

@Singleton
public class MicronautBuildCache implements Feature, BuildRemoteCacheConfiguration {

    private final BuildCacheCredentialsProvider buildCacheCredentialsProvider;
    public MicronautBuildCache(@Nullable BuildCacheCredentialsProvider buildCacheCredentialsProvider) {
        this.buildCacheCredentialsProvider = buildCacheCredentialsProvider;
    }

    @Override
    @NonNull
    public String getName() {
        return "micronaut-build-cache";
    }

    @Override
    public String getTitle() {
        return "Micronaut Build Cache";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Configures Build Cache for Micronaut Gradle Enterprise";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.DEV_TOOLS;
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.gradle.org/current/userguide/build_cache.html#sec:build_cache_configure";
    }

    @Override
    @NonNull
    public String getUrl() {
        return "https://ge.micronaut.io/cache/";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool().isGradle()) {
            generatorContext.addBuildPlugin(GradlePlugin.builder()
                    .settingsExtension(new RockerWritable(buildCache.template(generatorContext.getBuildTool().getGradleDsl().orElse(GradleDsl.GROOVY), this)))
                    .build());
        }
    }

    @Override
    @Nullable
    public Credentials getCredentials() {
        return buildCacheCredentialsProvider != null ? buildCacheCredentialsProvider.provideCredentails() : null;
    }

    @Override
    public boolean push() {
        return true;
    }
}
