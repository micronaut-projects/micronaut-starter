package io.micronaut.starter.feature;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.core.io.socket.SocketUtils;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
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
