package io.micronaut.starter.cli;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.core.io.socket.SocketUtils;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.options.Options;

import javax.inject.Singleton;
import java.util.Set;

@Singleton
public class GrpcRandomPort implements DefaultFeature {

    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
        return applicationType == ApplicationType.GRPC;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put("grpc.server.port", SocketUtils.findAvailableTcpPort());
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @NonNull
    @Override
    public String getName() {
        return "grpc-random-port";
    }

    @Override
    public boolean isVisible() {
        return false;
    }
}
