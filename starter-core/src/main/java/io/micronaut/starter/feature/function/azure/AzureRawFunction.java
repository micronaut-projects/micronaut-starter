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
package io.micronaut.starter.feature.function.azure;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.other.ShadePlugin;
import io.micronaut.starter.options.DefaultTestRockerModelProvider;
import io.micronaut.starter.options.TestRockerModelProvider;

import javax.inject.Singleton;

@Singleton
public class AzureRawFunction extends AbstractAzureFunction {
    private final AzureHttpFunction httpFunction;

    public AzureRawFunction(AzureHttpFunction httpFunction) {
        this.httpFunction = httpFunction;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.exclude(feature -> feature instanceof ShadePlugin);
        if (featureContext.getApplicationType() == ApplicationType.DEFAULT) {
            featureContext.addFeature(
                    httpFunction
            );
        }
    }

    @Override
    protected void applyTestTemplate(GeneratorContext generatorContext, Project project, String name) {
        if (generatorContext.getApplicationType() == ApplicationType.FUNCTION) {
            super.applyTestTemplate(generatorContext, project, name);
        }
    }

    @Override
    protected void applyFunction(GeneratorContext generatorContext, ApplicationType type) {
        super.applyFunction(generatorContext, type);

        if (type == ApplicationType.FUNCTION) {
            Project project = generatorContext.getProject();

            String testSource =  generatorContext.getTestSourcePath("/{packagePath}/Function");

            TestRockerModelProvider provider = new DefaultTestRockerModelProvider(spockTemplate(project),
                    kotlinTestTemplate(project),
                    javaJUnitTemplate(project),
                    groovyJUnitTemplate(project),
                    kotlinJUnitTemplate(project),
                    koTestTemplate(project));
            generatorContext.addTemplate("testFunction", testSource, provider);
        }
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-azure/snapshot/guide/index.html#simpleAzureFunctions";
    }
}
