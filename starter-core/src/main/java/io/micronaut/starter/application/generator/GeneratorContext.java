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
package io.micronaut.starter.application.generator;

import io.micronaut.starter.options.Options;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.Features;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.template.Template;
import io.micronaut.starter.util.VersionInfo;
import io.micronaut.starter.build.BuildProperties;

import java.util.*;

public class GeneratorContext {

    private final Project project;
    private final BuildProperties buildProperties = new BuildProperties();
    private final Map<String, Object> configuration = new LinkedHashMap<>();
    private final Map<String, Object> bootstrapConfig = new LinkedHashMap<>();
    private final Map<String, Template> templates = new LinkedHashMap<>();
    private final ApplicationType command;
    private final Features features;
    private final Options options;

    public GeneratorContext(Project project,
                            ApplicationType command,
                            Options options,
                            List<Feature> features) {
        this.command = command;
        this.project = project;
        this.features = new Features(features, options);
        this.options = options;
        String micronautVersion = VersionInfo.getMicronautVersion();
        if (options.getBuildTool() == BuildTool.GRADLE) {
            buildProperties.put("micronautVersion", micronautVersion);
        } else if (options.getBuildTool() == BuildTool.MAVEN) {
            buildProperties.put("micronaut.version", micronautVersion);
        }
    }

    public void addTemplate(String name, Template template) {
        templates.put(name, template);
    }

    public BuildProperties getBuildProperties() {
        return buildProperties;
    }

    public Map<String, Object> getConfiguration() {
        return configuration;
    }

    public Map<String, Object> getBootstrapConfig() {
        return bootstrapConfig;
    }

    public Map<String, Template> getTemplates() {
        return Collections.unmodifiableMap(templates);
    }

    public Language getLanguage() {
        return options.getLanguage();
    }

    public TestFramework getTestFramework() {
        return options.getTestFramework();
    }

    public BuildTool getBuildTool() {
        return options.getBuildTool();
    }

    public Project getProject() {
        return project;
    }

    public ApplicationType getCommand() {
        return command;
    }

    public Features getFeatures() {
        return features;
    }

    public void applyFeatures() {
        List<Feature> features = new ArrayList<>(this.features.getFeatures());
        features.sort(Comparator.comparingInt(Feature::getOrder));

        for (Feature feature: features) {
            feature.apply(this);
        }
    }

    public boolean isFeaturePresent(Class<? extends Feature> feature) {
        return features.getFeatures().stream()
                .map(Feature::getClass)
                .anyMatch(feature::isAssignableFrom);
    }
}
