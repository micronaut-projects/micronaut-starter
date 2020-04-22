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
import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.context.annotation.EachProperty;
import io.micronaut.context.annotation.Parameter;

@EachProperty("features")
public class FeatureConfigurationProperties implements FeatureConfiguration {
    private static final boolean DEFAULT_VISIBLE = true;
    private static final boolean DEFAULT_ENABLED = true;
    private final String name;

    @Nullable
    private String title;

    @Nullable
    private String featureName;

    @Nullable
    private String description;

    private boolean visible = DEFAULT_VISIBLE;

    private boolean enabled = DEFAULT_ENABLED;

    public FeatureConfigurationProperties(@Parameter String name) {
        this.name = name;
    }

    @Nullable
    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(@Nullable String title) {
        this.title = title;
    }

    @Nullable
    @Override
    public String getFeatureName() {
        return this.featureName;
    }

    public void setFeatureName(@Nullable String featureName) {
        this.featureName = featureName;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    @NonNull
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
