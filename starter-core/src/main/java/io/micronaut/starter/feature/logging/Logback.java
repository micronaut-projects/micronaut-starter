/*
 * Copyright 2017-2022 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.feature.logging;

import io.micronaut.context.annotation.Requires;
import org.jspecify.annotations.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.OperatingSystem;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.RequiresStdErr;
import io.micronaut.starter.feature.function.awslambda.AwsLambda;
import io.micronaut.starter.rocker.feature.logging.template.logback;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.template.RockerTemplate;
import jakarta.inject.Singleton;

import java.util.Set;

import static io.micronaut.starter.feature.build.pyronaut.PyronautUtils.isPyronaut;

@Requires(property = "micronaut.starter.feature.logback.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Logback implements LoggingFeature, DefaultFeature {
    public static final boolean DEFAULT_COLORING = true;
    private static final boolean USE_JANSI = false;
    private static final Dependency LOGBACK_CLASSIC = Dependency.builder()
            .groupId("ch.qos.logback")
            .artifactId("logback-classic")
            .runtime()
            .build();
    private static final Dependency PYRONAUT_LOGBACK = Dependency.builder()
            .groupId("io.micronaut.pyronaut")
            .artifactId("micronaut-pyronaut-logback")
            .runtime()
            .build();

    @Override
    @NonNull
    public String getName() {
        return "logback";
    }

    @Override
    public String getTitle() {
        return "Logback Logging";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds Logback Logging";
    }

    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
        return selectedFeatures.stream().noneMatch(LoggingFeature.class::isInstance);
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (!isPyronaut(generatorContext)) {
            addConfig(generatorContext, generatorContext.hasFeature(Slf4jJulBridge.class));
        }
        addDependency(generatorContext);
    }

    protected void addConfig(GeneratorContext generatorContext, boolean useJul) {
        boolean stderr = generatorContext.hasFeature(RequiresStdErr.class);
        generatorContext.addTemplate("loggingConfig", new RockerTemplate("src/main/resources/logback.xml",
                logback.template(useJansi(generatorContext), DEFAULT_COLORING, useJul, stderr)));
    }

    protected boolean useJansi(@NonNull GeneratorContext generatorContext) {
        if (generatorContext.getOperatingSystem() == OperatingSystem.WINDOWS) {
            return false;
        }
        if (generatorContext.getFeatures().hasFeature(AwsLambda.class)) {
            return false;
        }
        return USE_JANSI;
    }

    protected void addDependency(GeneratorContext generatorContext) {
        generatorContext.addDependency(isPyronaut(generatorContext) ? PYRONAUT_LOGBACK : LOGBACK_CLASSIC);
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }
}
