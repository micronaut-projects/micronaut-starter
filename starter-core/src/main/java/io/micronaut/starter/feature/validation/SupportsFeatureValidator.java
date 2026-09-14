/*
 * Copyright 2017-2026 original authors
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
package io.micronaut.starter.feature.validation;

import io.micronaut.core.annotation.Order;
import io.micronaut.core.order.Ordered;
import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.options.Options;
import jakarta.inject.Singleton;

import java.util.Set;

@Singleton
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SupportsFeatureValidator implements FeatureValidator {
    @Override
    public void validatePreProcessing(Options options, ApplicationType applicationType, Set<Feature> features) {
        StringBuilder sb = new StringBuilder();
        for (Feature feature : features) {
            if (!feature.supports(applicationType, options)) {
                if (!feature.supports(applicationType)) {
                    sb.append("Feature " + feature.getName() + " does not support application type " +  applicationType.getName() + ". ");
                }
                if (!feature.supports(options.getLanguage())) {
                    sb.append("Feature " + feature.getName() + " does not support language " +  options.getLanguage() + ". ");
                }
                if (!feature.supports(options.getBuildTool())) {
                    sb.append("Feature " + feature.getName() + " does not support build tool " +  options.getBuildTool() + ". ");
                }
            }
        }
        String message = sb.toString();
        if (StringUtils.isNotEmpty(message)) {
            throw new IllegalArgumentException(message);
        }
    }

    @Override
    public void validatePostProcessing(Options options, ApplicationType applicationType, Set<Feature> features) {
    }
}
