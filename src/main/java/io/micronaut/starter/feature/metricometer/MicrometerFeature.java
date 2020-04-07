package io.micronaut.starter.feature.metricometer;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.OneOfFeature;
import io.micronaut.starter.feature.other.Management;

public abstract class MicrometerFeature implements OneOfFeature {

    private final Core core;
    private final Management management;

    protected final String EXPORT_PREFIX = "micronaut.metrics.export";

    public MicrometerFeature(Core core, Management management) {
        this.core = core;
        this.management = management;
    }

    @Override
    public Class<?> getFeatureClass() {
        return MicrometerFeature.class;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.addFeature(core);
        if (featureContext.getFeatures().stream().noneMatch(f -> f instanceof Management)) {
            featureContext.addFeature(management);
        }
    }

    protected void addExportEnabled(CommandContext ctx, String registry, boolean enabled) {
        ctx.getConfiguration().put("micronaut.metrics.export." + registry + ".enabled", enabled);
    }
}
