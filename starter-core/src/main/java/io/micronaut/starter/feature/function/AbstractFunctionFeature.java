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
package io.micronaut.starter.feature.function;

import com.fizzed.rocker.RockerModel;
import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.MicronautRuntimeFeature;
import io.micronaut.starter.feature.function.template.http.httpFunctionGroovyController;
import io.micronaut.starter.feature.function.template.http.httpFunctionJavaController;
import io.micronaut.starter.feature.function.template.http.httpFunctionKotlinController;
import io.micronaut.starter.feature.json.SerializationFeature;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.DefaultTestRockerModelProvider;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestRockerModelProvider;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.RockerWritable;

import java.util.Optional;

/**
 * Abstract function implementation.
 *
 * @author graemerocher
 * @since 1.0.0
 */
public abstract class AbstractFunctionFeature implements FunctionFeature, MicronautRuntimeFeature {

    @Override
    public void apply(GeneratorContext generatorContext) {
        applyFunction(generatorContext, generatorContext.getApplicationType());
        addMicronautRuntimeBuildProperty(generatorContext);
    }

    protected RockerModel javaControllerTemplate(Project project, boolean useSerde) {
        return httpFunctionJavaController.template(project, useSerde);
    }

    protected RockerModel kotlinControllerTemplate(Project project, boolean useSerde) {
        return httpFunctionKotlinController.template(project, useSerde);
    }

    protected RockerModel groovyControllerTemplate(Project project, boolean useSerde) {
        return httpFunctionGroovyController.template(project, useSerde);
    }

    protected void applyFunction(GeneratorContext generatorContext, ApplicationType type) {
        BuildTool buildTool = generatorContext.getBuildTool();

        readmeTemplate(generatorContext, generatorContext.getProject(), buildTool)
            .ifPresent(rockerModel -> generatorContext.addHelpTemplate(new RockerWritable(rockerModel)));

        if (type == ApplicationType.DEFAULT) {

            final String className = StringUtils.capitalize(generatorContext.getProject().getPropertyName());
            Project project = generatorContext.getProject().withClassName(className);

            Language language = generatorContext.getLanguage();
            String sourceFile = generatorContext.getSourcePath("/{packagePath}/" + className + "Controller");

            boolean serdeFeaturePresent = generatorContext.isFeaturePresent(SerializationFeature.class);
            switch (language) {
                case GROOVY:
                    generatorContext.addTemplate("function", new RockerTemplate(
                            sourceFile,
                            groovyControllerTemplate(project, serdeFeaturePresent)));
                    break;
                case KOTLIN:
                    generatorContext.addTemplate("function", new RockerTemplate(
                            sourceFile,
                            kotlinControllerTemplate(project, serdeFeaturePresent)));
                    break;
                case JAVA:
                default:
                    generatorContext.addTemplate("function", new RockerTemplate(
                            sourceFile,
                            javaControllerTemplate(project, serdeFeaturePresent)));
                    break;
            }

            applyTestTemplate(generatorContext, project, className + getTestSuffix(type));
        }
    }

    protected String getTestSuffix(ApplicationType type) {
        return "Function";
    }

    protected void applyTestTemplate(GeneratorContext generatorContext, Project project, String name) {
        String testSource =  generatorContext.getTestSourcePath("/{packagePath}/" + name);
        TestRockerModelProvider provider = new DefaultTestRockerModelProvider(spockTemplate(project),
                javaJUnitTemplate(project),
                groovyJUnitTemplate(project),
                kotlinJUnitTemplate(project),
                koTestTemplate(project));
        generatorContext.addTemplate("testFunction", testSource, provider);
    }

    protected Optional<RockerModel> readmeTemplate(GeneratorContext generatorContext, Project project, BuildTool buildTool) {
        return Optional.empty();
    }

    protected abstract String getRunCommand(BuildTool buildTool);

    protected abstract String getBuildCommand(BuildTool buildTool);

    protected abstract RockerModel javaJUnitTemplate(Project project);

    protected abstract RockerModel kotlinJUnitTemplate(Project project);

    protected abstract RockerModel groovyJUnitTemplate(Project project);

    protected abstract RockerModel koTestTemplate(Project project);

    public abstract RockerModel spockTemplate(Project project);
}
