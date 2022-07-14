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
package io.micronaut.starter.feature.database;

import io.micronaut.starter.feature.Feature;

/**
 * Marker interface for JPA features, such as DataJpa and HibernateJpa.
 */
public interface JpaFeature extends Feature {
    String JPA_HIBERNATE_PROPERTIES = "jpa.default.properties.hibernate";
    String JPA_HIBERNATE_PROPERTIES_HBM2DDL = JPA_HIBERNATE_PROPERTIES + ".hbm2ddl.auto";
    String JPA_HIBERNATE_PROPERTIES_CONNECTION = JPA_HIBERNATE_PROPERTIES + ".connection";
    String JPA_DEFAULT_PROPERTIES_HIBERNATE_CONNECTION_URL = JPA_HIBERNATE_PROPERTIES_CONNECTION + ".url";
    String JPA_DEFAULT_PROPERTIES_HIBERNATE_CONNECTION_USERNAME = JPA_HIBERNATE_PROPERTIES_CONNECTION + ".username";
    String JPA_DEFAULT_PROPERTIES_HIBERNATE_CONNECTION_PASSWORD = JPA_HIBERNATE_PROPERTIES_CONNECTION + ".password";
}
