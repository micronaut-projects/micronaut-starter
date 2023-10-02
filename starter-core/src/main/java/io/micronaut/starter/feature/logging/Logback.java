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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.OperatingSystem;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.function.awslambda.AwsLambda;
import io.micronaut.starter.feature.logging.template.logback;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.template.RockerTemplate;
import jakarta.inject.Singleton;

import java.util.Set;

@Singleton
public class Logback implements LoggingFeature, DefaultFeature {
    public static final boolean DEFAULT_COLORING = true;
    private static final boolean USE_JANSI = false;
    private static final Dependency LOGBACK_CLASSIC = Dependency.builder()
            .groupId("ch.qos.logback")
            .artifactId("logback-classic")
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
        addConfig(generatorContext);
        generatorContext.addDependency(LOGBACK_CLASSIC);
    }

    protected void addConfig(GeneratorContext generatorContext) {
        generatorContext.addTemplate("loggingConfig", new RockerTemplate("src/main/resources/logback.xml",
                logback.template(useJansi(generatorContext), DEFAULT_COLORING)));
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

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }
}
