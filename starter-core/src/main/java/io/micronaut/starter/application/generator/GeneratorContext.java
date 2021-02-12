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
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.OperatingSystem;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.build.BuildProperties;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.Features;
import io.micronaut.starter.feature.config.ApplicationConfiguration;
import io.micronaut.starter.feature.config.BootstrapConfiguration;
import io.micronaut.starter.feature.config.Configuration;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.JdkVersion;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.options.TestRockerModelProvider;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.Template;
import io.micronaut.starter.template.Writable;
import io.micronaut.starter.util.VersionInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * A context object used when generating projects.
 *
 * @author graemerocher
 * @since 1.0.0
 */
public class GeneratorContext {

    private final Project project;
    private final OperatingSystem operatingSystem;
    private final BuildProperties buildProperties = new BuildProperties();
    private final ApplicationConfiguration configuration = new ApplicationConfiguration();
    private final Map<String, ApplicationConfiguration> applicationEnvironmentConfiguration = new LinkedHashMap<>();
    private final Map<String, BootstrapConfiguration> bootstrapEnvironmentConfiguration = new LinkedHashMap<>();
    private final BootstrapConfiguration bootstrapConfiguration = new BootstrapConfiguration();
    private final Set<Configuration> otherConfiguration = new HashSet<>();

    private final Map<String, Template> templates = new LinkedHashMap<>();
    private final List<Writable> helpTemplates = new ArrayList<>(8);
    private final ApplicationType command;
    private final Features features;
    private final Options options;

    public GeneratorContext(Project project,
                            ApplicationType type,
                            Options options,
                            @Nullable OperatingSystem operatingSystem,
                            Set<Feature> features) {
        this.command = type;
        this.project = project;
        this.operatingSystem = operatingSystem;
        this.features = new Features(this, features, options);
        this.options = options;
        String micronautVersion = VersionInfo.getMicronautVersion();
        if (options.getBuildTool().isGradle()) {
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
     * Adds a template which will be consolidated into a single help file.
     *
     * @param writable The template
     */
    public void addHelpTemplate(Writable writable) {
        helpTemplates.add(writable);
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
    @NonNull public ApplicationConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * @return The configuration
     */
    @Nullable public ApplicationConfiguration getConfiguration(String env) {
        return applicationEnvironmentConfiguration.get(env);
    }

    @NonNull public ApplicationConfiguration getConfiguration(String env, ApplicationConfiguration defaultConfig) {
        return applicationEnvironmentConfiguration.computeIfAbsent(env, (key) -> defaultConfig);
    }

    /**
     * @return The configuration
     */
    @Nullable public BootstrapConfiguration getBootstrapConfiguration(String env) {
        return bootstrapEnvironmentConfiguration.get(env);
    }

    @NonNull public BootstrapConfiguration getBootstrapConfiguration(String env, BootstrapConfiguration defaultConfig) {
        return bootstrapEnvironmentConfiguration.computeIfAbsent(env, (key) -> defaultConfig);
    }

    /**
     * @return The bootstrap config
     */
    @NonNull public BootstrapConfiguration getBootstrapConfiguration() {
        return bootstrapConfiguration;
    }

    public void addConfiguration(@NonNull Configuration configuration) {
        otherConfiguration.add(configuration);
    }

    @NonNull public Set<Configuration> getAllConfigurations() {
        Set<Configuration> allConfigurations = new HashSet<>();
        allConfigurations.add(configuration);
        allConfigurations.add(bootstrapConfiguration);
        allConfigurations.addAll(applicationEnvironmentConfiguration.values());
        allConfigurations.addAll(bootstrapEnvironmentConfiguration.values());
        allConfigurations.addAll(otherConfiguration);
        return allConfigurations;
    }

    /**
     * @return The templates
     */
    @NonNull public Map<String, Template> getTemplates() {
        return Collections.unmodifiableMap(templates);
    }

    /**
     * @return The templates
     */
    @NonNull public List<Writable> getHelpTemplates() {
        return Collections.unmodifiableList(helpTemplates);
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

    /**
     * @return The JDK version
     */
    @NonNull public JdkVersion getJdkVersion() {
        return options.getJavaVersion();
    }

    /**
     * @return The current OS
     */
    @Nullable public OperatingSystem getOperatingSystem() {
        return operatingSystem;
    }

    public void applyFeatures() {
        List<Feature> features = new ArrayList<>(this.features.getFeatures());
        features.sort(Comparator.comparingInt(Feature::getOrder));

        for (Feature feature: features) {
            feature.apply(this);
        }
    }

    public boolean isFeaturePresent(Class<? extends Feature> feature) {
        return features.isFeaturePresent(feature);
    }

    public <T extends Feature> Optional<T> getFeature(Class<T> feature) {
        return features.getFeature(feature);
    }

    public <T extends Feature> T getRequiredFeature(Class<T> feature) {
        return features.getRequiredFeature(feature);
    }

    public String getSourcePath(String path) {
        return getLanguage().getSourcePath(path);
    }

    public String getTestSourcePath(String path) {
        return getTestFramework().getSourcePath(path, getLanguage());
    }

    RockerModel parseModel(RockerModel javaTemplate,
                           RockerModel kotlinTemplate,
                           RockerModel groovyTemplate) {
        switch (getLanguage()) {
            case GROOVY:
                return groovyTemplate;
            case KOTLIN:
                return kotlinTemplate;
            case JAVA:
            default:
                return javaTemplate;
        }
    }

    public void addTemplate(String name, String path, TestRockerModelProvider testRockerModelProvider) {
        RockerModel rockerModel = testRockerModelProvider.findModel(getLanguage(), getTestFramework());
        if (rockerModel != null) {
            addTemplate(name, new RockerTemplate(path, rockerModel));
        }
    }

    public void addTemplate(String templateName,
                            String triggerFile,
                            RockerModel javaTemplate,
                            RockerModel kotlinTemplate,
                            RockerModel groovyTemplate) {
        RockerModel rockerModel = parseModel(javaTemplate, kotlinTemplate, groovyTemplate);
        addTemplate(templateName, new RockerTemplate(triggerFile, rockerModel));
    }

}
