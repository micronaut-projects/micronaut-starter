/*
 * Copyright 2017-2023 original authors
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
package io.micronaut.starter.feature.function.azure;

import com.fizzed.rocker.RockerModel;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.feature.function.FunctionFeatureCodeGenerator;
import io.micronaut.starter.feature.function.azure.template.azureFunctionGroovyJunit;
import io.micronaut.starter.feature.function.azure.template.azureFunctionJavaJunit;
import io.micronaut.starter.feature.function.azure.template.azureFunctionKoTest;
import io.micronaut.starter.feature.function.azure.template.azureFunctionKotlinJunit;
import io.micronaut.starter.feature.function.azure.template.azureFunctionSpock;

public class DefaultAzureFunctionFeatureCodeGenerator implements FunctionFeatureCodeGenerator {

    @Override
    public RockerModel javaJUnitTemplate(Project project) {
        return azureFunctionJavaJunit.template(project);
    }

    @Override
    public RockerModel kotlinJUnitTemplate(Project project) {
        return azureFunctionKotlinJunit.template(project);
    }

    @Override
    public RockerModel groovyJUnitTemplate(Project project) {
        return azureFunctionGroovyJunit.template(project);
    }

    @Override
    public RockerModel koTestTemplate(Project project) {
        return azureFunctionKoTest.template(project);
    }

    @Override
    public RockerModel spockTemplate(Project project) {
        return azureFunctionSpock.template(project);
    }
}
