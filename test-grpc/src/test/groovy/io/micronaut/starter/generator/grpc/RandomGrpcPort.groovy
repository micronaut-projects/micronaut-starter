package io.micronaut.starter.generator.grpc

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.Feature

import javax.inject.Singleton

@Singleton
class RandomGrpcPort implements Feature {

    static final String NAME = "random-grpc-port"

    @Override
    String getName() {
        return NAME
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
        generatorContext.configuration["grpc.server.port"] = '${random.port}'

    }
}
