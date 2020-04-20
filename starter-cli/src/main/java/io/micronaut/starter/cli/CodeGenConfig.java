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
package io.micronaut.starter.cli;

import io.micronaut.context.BeanContext;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.beans.BeanIntrospection;
import io.micronaut.starter.Options;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.ConsoleOutput;
import io.micronaut.starter.feature.*;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

@Introspected
public class CodeGenConfig {

    private ApplicationType applicationType;
    private String defaultPackage;
    private TestFramework testFramework;
    private Language sourceLanguage;
    private BuildTool buildTool;
    private List<String> features;

    private boolean legacy;

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
        File micronautCli = new File("micronaut-cli.yml");
        if (micronautCli.exists()) {
            try (InputStream inputStream = Files.newInputStream(micronautCli.toPath())) {
                return load(beanContext, inputStream, consoleOutput);
            } catch (IOException e) { }
        }
        return null;
    }

    public static CodeGenConfig load(BeanContext beanContext, InputStream inputStream, ConsoleOutput consoleOutput) {
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
            AvailableFeatures availableFeatures = null;
            List<Feature> features = new ArrayList<>();
            if (profile.equals("service")) {
                codeGenConfig.setApplicationType(ApplicationType.DEFAULT);
            } else if (profile.equals("cli")) {
                codeGenConfig.setApplicationType(ApplicationType.CLI);
            } else if (profile.equals("function-aws") || profile.equals("function-aws-alexa")) {
                codeGenConfig.setApplicationType(ApplicationType.FUNCTION);
            } else if (profile.equals("grpc")) {
                codeGenConfig.setApplicationType(ApplicationType.GRPC);
            } else if (profile.equals("kafka") || profile.equals("rabbitmq")) {
                codeGenConfig.setApplicationType(ApplicationType.MESSAGING);
            } else {
                return null;
            }

            availableFeatures = beanContext.getBean(codeGenConfig.getApplicationType().getAvailableFeaturesClass());

            if (new File("build.gradle").exists()) {
                codeGenConfig.setBuildTool(BuildTool.gradle);
            } else if (new File("pom.xml").exists()) {
                codeGenConfig.setBuildTool(BuildTool.maven);
            } else {
                return null;
            }

            codeGenConfig.setFeatures(availableFeatures.getAllFeatures()
                    .filter(f -> f instanceof DefaultFeature)
                    .map(DefaultFeature.class::cast)
                    .filter(f -> f.shouldApply(
                            codeGenConfig.getApplicationType(),
                            new Options(codeGenConfig.getSourceLanguage(), codeGenConfig.getTestFramework(), codeGenConfig.getBuildTool()),
                            features))
                    .map(Feature::getName)
                    .collect(Collectors.toList()));

            consoleOutput.warning("This project is using Micronaut CLI v2 but is still using the v1 micronaut-cli.yml format");
            consoleOutput.warning("To replace the configuration with the new format, run `mn update-cli-config`");
        }

        return codeGenConfig;
    }
}
