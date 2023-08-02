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
package io.micronaut.starter.cli;

import io.micronaut.context.BeanContext;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.beans.BeanIntrospection;
import io.micronaut.inject.qualifiers.Qualifiers;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.AvailableFeatures;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.io.ConsoleOutput;
import io.micronaut.starter.io.FileSystemOutputHandler;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.util.VersionInfo;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Introspected
public class CodeGenConfig {

    private String framework = Options.FRAMEWORK_MICRONAUT;
    private ApplicationType applicationType;
    private String defaultPackage;
    private TestFramework testFramework;
    private Language sourceLanguage;
    private BuildTool buildTool;
    private List<String> features;
    private boolean legacy;

    public String getFramework() {
        return framework;
    }

    public void setFramework(String framework) {
        this.framework = framework;
    }

    public ApplicationType getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(ApplicationType applicationType) {
        this.applicationType = applicationType;
    }

    public String getDefaultPackage() {
        return defaultPackage;
    }

    public void setDefaultPackage(String defaultPackage) {
        this.defaultPackage = defaultPackage;
    }

    public TestFramework getTestFramework() {
        return testFramework;
    }

    public void setTestFramework(TestFramework testFramework) {
        this.testFramework = testFramework;
    }

    public Language getSourceLanguage() {
        return sourceLanguage;
    }

    public void setSourceLanguage(Language sourceLanguage) {
        this.sourceLanguage = sourceLanguage;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

    public BuildTool getBuildTool() {
        return buildTool;
    }

    public void setBuildTool(BuildTool buildTool) {
        this.buildTool = buildTool;
    }

    public boolean isLegacy() {
        return legacy;
    }

    public static CodeGenConfig load(BeanContext beanContext, ConsoleOutput consoleOutput) {
        try {
            return load(beanContext, FileSystemOutputHandler.getDefaultBaseDirectory(), consoleOutput);
        } catch (IOException e) {
            return null;
        }
    }

    public static CodeGenConfig load(BeanContext beanContext, File directory, ConsoleOutput consoleOutput) {

        File micronautCli = new File(directory, "micronaut-cli.yml");
        if (micronautCli.exists()) {
            try (InputStream inputStream = Files.newInputStream(micronautCli.toPath())) {
                Yaml yaml = new Yaml();
                Map<String, Object> map = new LinkedHashMap<>();
                Iterable<Object> objects = yaml.loadAll(inputStream);
                Iterator<Object> i = objects.iterator();
                if (i.hasNext()) {
                    while (i.hasNext()) {
                        Object object = i.next();
                        if (object instanceof Map) {
                            map.putAll((Map) object);
                        }
                    }
                }
                BeanIntrospection<CodeGenConfig> introspection = BeanIntrospection.getIntrospection(CodeGenConfig.class);
                CodeGenConfig codeGenConfig = introspection.instantiate();
                introspection.getBeanProperties().forEach(bp -> {
                    Object value = map.get(bp.getName());
                    if (value != null) {
                        bp.convertAndSet(codeGenConfig, value);
                    }
                });

                if (map.containsKey("profile")) {
                    codeGenConfig.legacy = true;
                    String profile = map.get("profile").toString();
                    if ("service".equals(profile)) {
                        codeGenConfig.setApplicationType(ApplicationType.DEFAULT);
                    } else if ("cli".equals(profile)) {
                        codeGenConfig.setApplicationType(ApplicationType.CLI);
                    } else if ("function-aws".equals(profile) || "function-aws-alexa".equals(profile)) {
                        codeGenConfig.setApplicationType(ApplicationType.FUNCTION);
                    } else if ("grpc".equals(profile)) {
                        codeGenConfig.setApplicationType(ApplicationType.GRPC);
                    } else if ("kafka".equals(profile) || "rabbitmq".equals(profile)) {
                        codeGenConfig.setApplicationType(ApplicationType.MESSAGING);
                    } else {
                        return null;
                    }

                    AvailableFeatures availableFeatures = beanContext.getBean(AvailableFeatures.class, Qualifiers.byName(codeGenConfig.getApplicationType().getName()));

                    if (new File(directory, "build.gradle").exists()) {
                        codeGenConfig.setBuildTool(BuildTool.GRADLE);
                    } else if (new File(directory, "build.gradle.kts").exists()) {
                        codeGenConfig.setBuildTool(BuildTool.GRADLE_KOTLIN);
                    } else if (new File(directory, "pom.xml").exists()) {
                        codeGenConfig.setBuildTool(BuildTool.MAVEN);
                    } else {
                        return null;
                    }


                    List<Feature> featureList = new ArrayList<>();
                    Options options = new Options(codeGenConfig.getSourceLanguage(), codeGenConfig.getTestFramework(), codeGenConfig.getBuildTool(), VersionInfo.getJavaVersion(), Collections.emptyMap(), codeGenConfig.getFramework());
                    ApplicationType applicationType = codeGenConfig.getApplicationType();
                    DefaultFeature.forEach(availableFeatures.getAllFeatures(), applicationType, options, new HashSet<>(), featureList::add);
                    codeGenConfig.setFeatures(featureList.stream().map(Feature::getName).toList());


                    consoleOutput.warning("This project is using Micronaut CLI v2 but is still using the v1 micronaut-cli.yml format");
                    consoleOutput.warning("To replace the configuration with the new format, run `mn update-cli-config`");
                }

                return codeGenConfig;
            } catch (IOException e) { }
        }
        return null;
    }
}
