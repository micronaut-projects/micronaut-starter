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
package io.micronaut.starter.command;


import io.micronaut.starter.Project;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.Features;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.template.Template;
import io.micronaut.starter.util.VersionInfo;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class CommandContext {

    private final Project project;
    private final Map<String, String> projectProperties = new LinkedHashMap<>();
    private final Map<String, Object> configuration = new LinkedHashMap<>();
    private final Map<String, Template> templates = new LinkedHashMap<>();
    private final MicronautCommand command;
    private final Features features;
    private final Language language;
    private final TestFramework testFramework;
    private final BuildTool buildTool;

    public CommandContext(FeatureContext featureContext, Project project) {
        this.command = featureContext.getCommand();
        this.project = project;
        this.features = new Features(featureContext.getFeatures());
        this.language = featureContext.getLanguage();
        this.testFramework = featureContext.getTestFramework();
        this.buildTool = featureContext.getBuildTool();
        String micronautVersion = VersionInfo.getVersion();
        if (buildTool == BuildTool.gradle) {
            projectProperties.put("micronautVersion", micronautVersion);
        } else if (buildTool == BuildTool.maven) {
            projectProperties.put("micronaut.version", micronautVersion);
        }
    }

    public void addTemplate(String name, Template template) {
        templates.put(name, template);
    }

    public Map<String, String> getProjectProperties() {
        return projectProperties;
    }

    public Map<String, Object> getConfiguration() {
        return configuration;
    }

    public Map<String, Template> getTemplates() {
        return Collections.unmodifiableMap(templates);
    }

    public Features getFeatures() {
        return features;
    }

    public Language getLanguage() {
        return language;
    }

    public TestFramework getTestFramework() {
        return testFramework;
    }

    public BuildTool getBuildTool() {
        return buildTool;
    }

    public Project getProject() {
        return project;
    }

    public MicronautCommand getCommand() {
        return command;
    }
}
