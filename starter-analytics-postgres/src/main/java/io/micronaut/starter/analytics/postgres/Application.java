/*
 * Copyright 2017-2020 original authors
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
package io.micronaut.starter.analytics.postgres;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.annotation.Creator;
import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.JdkVersion;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * Models a generated application.
 *
 * @author graemerocher
 * @since 1.0.0
 */
@MappedEntity
public class Application {
    @Id
    @GeneratedValue
    private Long id;
    @Relation(value = Relation.Kind.ONE_TO_MANY, mappedBy = "application")
    private Set<Feature> features = Collections.emptySet();
    private final ApplicationType type;
    private final Language language;
    private final BuildTool buildTool;
    private final TestFramework testFramework;
    private final JdkVersion jdkVersion;
    private final String micronautVersion;
    @DateCreated
    private LocalDateTime dateCreated;

    @Creator
    public Application(
            @NonNull ApplicationType type,
            @NonNull Language language,
            @NonNull BuildTool buildTool,
            @NonNull TestFramework testFramework,
            @NonNull JdkVersion jdkVersion,
            @NonNull @NotBlank String micronautVersion) {
        this.type = Objects.requireNonNull(type, "Type cannot be null");
        this.language = Objects.requireNonNull(language, "Language cannot be null");
        this.buildTool = Objects.requireNonNull(buildTool, "Build tool cannot be null");
        this.testFramework = Objects.requireNonNull(testFramework, "Test framework cannot be null");
        this.jdkVersion = Objects.requireNonNull(jdkVersion, "JDK version cannot be null");
        this.micronautVersion = Objects.requireNonNull(micronautVersion, "Micronaut version cannot be null");
    }

    /**
     * @return The type
     */
    public @NonNull ApplicationType getType() {
        return type;
    }

    /**
     * @return The language
     */
    public @NonNull Language getLanguage() {
        return language;
    }

    /**
     * @return The build tool
     */
    public @NonNull BuildTool getBuildTool() {
        return buildTool;
    }

    /**
     * @return The test framework
     */
    public @NonNull TestFramework getTestFramework() {
        return testFramework;
    }

    /**
     * @return The JDK version
     */
    public @NonNull JdkVersion getJdkVersion() {
        return jdkVersion;
    }

    /**
     * @return The id
     */
    public @Nullable Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return The features
     */
    public @NonNull Set<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(Set<Feature> features) {
        if (features != null) {
            this.features = features;
        }
    }

    /**
     * @return The date created
     */
    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * @return The Micronaut version
     */
    public @NonNull @NotBlank String getMicronautVersion() {
        return micronautVersion;
    }
}
