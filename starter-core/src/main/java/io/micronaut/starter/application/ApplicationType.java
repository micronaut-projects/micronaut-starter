/*
 * Copyright 2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.application;

import io.micronaut.core.naming.Named;
import io.micronaut.starter.feature.AvailableFeatures;

import javax.annotation.Nonnull;
import java.util.Locale;

public enum ApplicationType implements Named {

    DEFAULT("Micronaut Application", "A Micronaut application", DefaultAvailableFeatures.class),
    CLI("Micronaut CLI Application", "A Command Line Application", CliAvailableFeatures.class),
    FUNCTION("Micronaut Function", "A Function Application for Serverless", FunctionAvailableFeatures.class),
    GRPC("Micronaut GRPC Application", "A GRPC Application", GrpcAvailableFeatures.class),
    MESSAGING("Micronaut Messaging Application", "A Messaging-Driven Application", MessagingAvailableFeatures.class);

    private final String title;
    private final String description;
    private final Class<? extends AvailableFeatures> availableFeaturesClass;

    ApplicationType(String title,
                    String description,
                    Class<? extends AvailableFeatures> availableFeaturesClass) {
        this.title = title;
        this.description = description;
        this.availableFeaturesClass = availableFeaturesClass;
    }

    public Class<? extends AvailableFeatures> getAvailableFeaturesClass() {
        return availableFeaturesClass;
    }

    /**
     * @return The title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return The description.
     */
    public String getDescription() {
        return description;
    }

    @Nonnull
    @Override
    public String getName() {
        return name().toLowerCase(Locale.ENGLISH);
    }
}
