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
package io.micronaut.starter.api;

import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * Enum of application types.
 *
 * @author graemerocher
 * @since 1.0.0
 */
public enum ApplicationTypes implements ApplicationType {
    /**
     * Regular application.
     */
    app("Application", "A Micronaut application"),
    /**
     * A Serverless function.
     */
    function("Function", "A Function Application for Serverless"),
    /**
     * A CLI application.
     */
    cli("CLI", "A Command Line Application"),
    /**
     * A messaging application.
     */
    messaging("Messaging", "A Messaging-Driven Application"),
    /**
     * A GRPC application.
     */
    grpc("GRPC", "A GRPC Application");

    private final String title;
    private final String description;

    /**
     * Default constructor.
     * @param title The title
     * @param description The descriptor
     */
    ApplicationTypes(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @NonNull
    @Override
    public String getName() {
        return name();
    }
}
