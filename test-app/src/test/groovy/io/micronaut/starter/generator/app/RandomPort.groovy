package io.micronaut.starter.generator.app

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.Feature

import javax.inject.Singleton

@Singleton
class RandomPort implements Feature {

    @Override
    String getName() {
        return "random-port"
    }

    @Override
    String getCategory() {
        return Category.CONFIGURATION
    }

    @Override
    boolean supports(ApplicationType applicationType) {
        return true
    }

    @Override
    void apply(GeneratorContext generatorContext) {
        generatorContext.configuration["micronaut.server.port"] = -1
    }
}
