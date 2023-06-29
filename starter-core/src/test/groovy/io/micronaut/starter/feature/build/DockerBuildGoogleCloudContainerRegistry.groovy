package io.micronaut.starter.feature.build

import io.micronaut.context.annotation.Replaces
import io.micronaut.context.annotation.Requires
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.dependencies.CoordinateResolver
import io.micronaut.starter.feature.build.gradle.MicronautApplicationGradlePlugin
import jakarta.inject.Singleton

@Requires(property = "spec.name", value = 'MicronautBuildPluginDockerBuildSpec')
@Singleton
@Replaces(MicronautBuildPlugin)
class DockerBuildGoogleCloudContainerRegistry extends MicronautBuildPlugin {

    DockerBuildGoogleCloudContainerRegistry(CoordinateResolver coordinateResolver) {
        super(coordinateResolver)
    }

    @Override
    protected MicronautApplicationGradlePlugin.Builder micronautGradleApplicationPluginBuilder(GeneratorContext generatorContext) {
        MicronautApplicationGradlePlugin.Builder builder = super.micronautGradleApplicationPluginBuilder(generatorContext)
        builder.dockerBuildImage("gcr.io/micronaut-guides/micronautguide:latest")
        return builder
    }

}
