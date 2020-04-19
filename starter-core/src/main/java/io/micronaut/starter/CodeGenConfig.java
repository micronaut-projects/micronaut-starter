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
package io.micronaut.starter;

import io.micronaut.context.BeanContext;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.beans.BeanIntrospection;
import io.micronaut.starter.command.*;
import io.micronaut.starter.feature.*;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.util.NameUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

@Introspected
public class CodeGenConfig {

    private MicronautCommand command;
    private String defaultPackage;
    private TestFramework testFramework;
    private Language sourceLanguage;
    private BuildTool buildTool;
    private List<String> features;

    public MicronautCommand getCommand() {
        return command;
    }

    public void setCommand(MicronautCommand command) {
        this.command = command;
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
            bp.convertAndSet(codeGenConfig, map.get(bp.getName()));
        });

        if (map.containsKey("profile")) {
            String profile = map.get("profile").toString();
            AvailableFeatures availableFeatures = null;
            List<Feature> features = new ArrayList<>();
            if (profile.equals("service")) {
                codeGenConfig.setCommand(MicronautCommand.CREATE_APP);
                availableFeatures = beanContext.getBean(CreateAppCommand.CreateAppFeatures.class);
            } else if (profile.equals("cli")) {
                codeGenConfig.setCommand(MicronautCommand.CREATE_CLI_APP);
                availableFeatures = beanContext.getBean(CreateCliCommand.CreateCliFeatures.class);
            } else if (profile.equals("function-aws") || profile.equals("function-aws-alexa")) {
                codeGenConfig.setCommand(MicronautCommand.CREATE_FUNCTION);
                availableFeatures = beanContext.getBean(CreateFunctionCommand.CreateFunctionFeatures.class);
            } else if (profile.equals("grpc")) {
                codeGenConfig.setCommand(MicronautCommand.CREATE_GRPC_APP);
                availableFeatures = beanContext.getBean(CreateGrpcCommand.CreateGrpcFeatures.class);
            } else if (profile.equals("kafka") || profile.equals("rabbitmq")) {
                codeGenConfig.setCommand(MicronautCommand.CREATE_MESSAGING_APP);
                availableFeatures = beanContext.getBean(CreateMessagingCommand.CreateMessagingFeatures.class);
            }

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
                            codeGenConfig.getCommand(),
                            new Options(codeGenConfig.getSourceLanguage(), codeGenConfig.getTestFramework(), codeGenConfig.getBuildTool()),
                            features))
                    .map(Feature::getName)
                    .collect(Collectors.toList()));

            consoleOutput.warning("This project is using Micronaut CLI v2 but is still using the v1 micronaut-cli.yml format");
            consoleOutput.warning("To prevent this warning in the future, modify micronaut-cli.yml to contain the following:");
            consoleOutput.out(cli.template(codeGenConfig.getSourceLanguage(),
                    codeGenConfig.getTestFramework(),
                    codeGenConfig.getBuildTool(),
                    NameUtils.parse(codeGenConfig.defaultPackage + ".Ignored"),
                    codeGenConfig.getFeatures(),
                    codeGenConfig.getCommand()).render().toString());
            String commandName = codeGenConfig.getCommand().name().toLowerCase().replaceAll("_", "-");
            consoleOutput.warning("In order to use code generation commands that are dependent on a feature, you may need to modify the feature list to include any features that are in use.");
            consoleOutput.warning(String.format("For a list of available features, run `mn %s --list-features`", commandName));
        }

        return codeGenConfig;
    }
}
