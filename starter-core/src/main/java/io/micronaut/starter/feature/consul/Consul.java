package io.micronaut.starter.feature.consul;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.distributedconfig.DistributedConfigFeature;

import javax.inject.Singleton;
import java.util.Map;

@Singleton
public class Consul implements Feature {

    @NonNull
    @Override
    public String getName() {
        return "consul";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return false;
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        Map<String, Object> config;
        if (generatorContext.isFeaturePresent(DistributedConfigFeature.class)) {
            config = generatorContext.getBootstrapConfig();
        } else {
            config = generatorContext.getConfiguration();
        }

        config.put("consul.client.defaultZone", "${CONSUL_HOST:localhost}:${CONSUL_PORT:8500}");
    }
}
