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
package io.micronaut.starter.api.analytics;

import io.micronaut.context.annotation.Requires;
import io.micronaut.runtime.event.annotation.EventListener;
import io.micronaut.starter.analytics.Generated;
import io.micronaut.starter.analytics.SelectedFeature;
import io.micronaut.starter.api.event.ApplicationGeneratingEvent;
import io.micronaut.starter.application.generator.GeneratorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
@Requires(beans = AnalyticsOperations.class)
public class GenerationListener {
    private static final Logger LOG = LoggerFactory.getLogger(GenerationListener.class);
    private final AnalyticsOperations analyticsOperations;

    public GenerationListener(AnalyticsOperations analyticsOperations) {
        this.analyticsOperations = analyticsOperations;
    }

    @EventListener
    void onApplicationGenerated(ApplicationGeneratingEvent event) {
        GeneratorContext context = event.getSource();
        List<SelectedFeature> features = context.getFeatures().stream()
                .map(SelectedFeature::new).collect(Collectors.toList());
        Generated generated = new Generated(
                context.getApplicationType(),
                context.getLanguage(),
                context.getBuildTool(),
                context.getTestFramework(),
                context.getJdkVersion()
        );
        generated.setSelectedFeatures(features);
        if (analyticsOperations != null) {
            analyticsOperations.applicationGenerated(generated)
                .whenComplete((httpStatus, throwable) -> {
                    if (throwable != null) {
                        if (LOG.isErrorEnabled()) {
                            LOG.error("Error occurred reporting analytics: {}", throwable.getMessage(), throwable);
                        }
                    }
                });
        }
    }
}
