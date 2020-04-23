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

import com.fizzed.rocker.RockerModel;
import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.starter.feature.SourceTemplateProvider;
import io.micronaut.starter.feature.TestTemplateProvider;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.Features;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.Template;
import io.micronaut.starter.util.VersionInfo;
import io.micronaut.starter.build.BuildProperties;

import java.util.*;

/**
 * A context object used when generating projects.
 *
 * @author graemerocher
 * @since 1.0.0
 */
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
                            ApplicationType type,
                            Options options,
                            Set<Feature> features) {
        this.command = type;
        this.project = project;
        this.features = new Features(type, features, options);
        this.options = options;
        String micronautVersion = VersionInfo.getMicronautVersion();
        if (options.getBuildTool() == BuildTool.GRADLE) {
            buildProperties.put("micronautVersion", micronautVersion);
        } else if (options.getBuildTool() == BuildTool.MAVEN) {
            buildProperties.put("micronaut.version", micronautVersion);
        }
    }

    /**
     * Adds a template.
     * @param name The name of the template
     * @param template The template
     */
    public void addTemplate(String name, Template template) {
        templates.put(name, template);
    }

    /**
     * Adds a template.
     * @param name The name of the template
     */
    public void removeTemplate(String name) {
        templates.remove(name);
    }

    /**
     * @return The build properties
     */
    @NonNull public BuildProperties getBuildProperties() {
        return buildProperties;
    }

    /**
     * @return The configuration
     */
    @NonNull public Map<String, Object> getConfiguration() {
        return configuration;
    }

    /**
     * @return The bootstrap config
     */
    @NonNull public Map<String, Object> getBootstrapConfig() {
        return bootstrapConfig;
    }

    /**
     * @return The templates
     */
    @NonNull public Map<String, Template> getTemplates() {
        return Collections.unmodifiableMap(templates);
    }

    /**
     * @return The language
     */
    @NonNull public Language getLanguage() {
        return options.getLanguage();
    }

    /**
     * @return The test framework
     */
    @NonNull public TestFramework getTestFramework() {
        return options.getTestFramework();
    }

    /**
     * @return The build tool
     */
    @NonNull public BuildTool getBuildTool() {
        return options.getBuildTool();
    }

    /**
     * @return The project
     */
    @NonNull public Project getProject() {
        return project;
    }

    /**
     * @return The application type
     */
    @NonNull public ApplicationType getApplicationType() {
        return command;
    }

    /**
     * @return The selected features
     */
    @NonNull public Features getFeatures() {
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
        Objects.requireNonNull(feature, "The feature class cannot be null");
        return features.getFeatures().stream()
                .map(Feature::getClass)
                .anyMatch(feature::isAssignableFrom);
    }

    public String getSourcePath(String path) {
        return getLanguage().getSourcePath(path);
    }

    public String getTestSourcePath(String path) {
        return getTestFramework().getSourcePath(path, getLanguage());
    }

    RockerModel parseModel(Project project,
                                  SourceTemplateProvider sourceTemplateProvider) {
        switch (getLanguage()) {
            case GROOVY:
                return sourceTemplateProvider.groovyTemplate(project);
            case KOTLIN:
                return sourceTemplateProvider.kotlinTemplate(project);
            case JAVA:
            default:
                return sourceTemplateProvider.javaTemplate(project);
        }
    }

    public void addTestTemplate(Project project,
                                       String templateName,
                                       String testSource,
                                       TestTemplateProvider testTemplateProvider) {
        RockerModel testTemplate = testTemplateProvider.getTestTemplate(project, this);
        if (testTemplate != null) {
            addTemplate(templateName, new RockerTemplate(testSource, testTemplate));
        }
    }

    public void addTemplate(Project project,
                            String templateName,
                            String triggerFile,
                            SourceTemplateProvider sourceTemplateProvider) {
        RockerModel rockerModel = parseModel(project, sourceTemplateProvider);
        addTemplate(templateName, new RockerTemplate(triggerFile, rockerModel));
    }
}
