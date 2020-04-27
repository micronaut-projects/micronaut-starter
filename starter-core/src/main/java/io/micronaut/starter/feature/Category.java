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
package io.micronaut.starter.feature;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.core.naming.Named;

/**
 * Category to which a feature belongs to.
 *
 * @author Álvaro Sánchez-Mariscal
 * @since 2.0.0
 */
public enum Category implements Named {

    AWS                 ("Amazon Web Services"),
    CACHE               ("Cache"),
    CIRCUIT_BREAKER     ("Circuit Breaker"),
    CONFIGURATION       ("Configuration"),
    DATABASE            ("Database"),
    DEV_TOOLS           ("Development Tools"),
    DISTRIBUTED_CONFIG  ("Distributed Configuration"),
    DOCUMENTATION       ("Documentation"),
    FUNCTION            ("Cloud Function"),
    HTTP_CLIENT         ("HTTP Client"),
    HTTP_SERVER         ("HTTP Server"),
    LOGGING             ("Logging"),
    MANAGEMENT          ("Management"),
    MESSAGING           ("Messaging"),
    PACKAGING           ("Packaging"),
    RSS                 ("RSS Feeds"),
    SEARCH              ("Search Engine"),
    SECURITY            ("Security"),
    SERVICE_DISCOVERY   ("Service Discovery"),
    TRACING             ("Distributed Tracing"),
    VALIDATION          ("Validation"),
    VIEW                ("View Rendering"),

    OTHER               ("Other");

    private final String name;

    Category(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String getName() {
        return name;
    }

}
