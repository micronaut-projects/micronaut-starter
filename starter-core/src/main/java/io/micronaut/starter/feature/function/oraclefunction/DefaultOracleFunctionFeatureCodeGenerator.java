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
package io.micronaut.starter.feature.function.oraclefunction;

import com.fizzed.rocker.RockerModel;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.feature.function.FunctionFeatureCodeGenerator;
import io.micronaut.starter.feature.server.template.groovyJunit;
import io.micronaut.starter.feature.server.template.javaJunit;
import io.micronaut.starter.feature.server.template.koTest;
import io.micronaut.starter.feature.server.template.kotlinJunit;
import io.micronaut.starter.feature.server.template.spock;

public class DefaultOracleFunctionFeatureCodeGenerator implements FunctionFeatureCodeGenerator {
    @Override
    public RockerModel javaJUnitTemplate(Project project) {
        return javaJunit.template(project, true);
    }

    @Override
    public RockerModel kotlinJUnitTemplate(Project project) {
        return kotlinJunit.template(project, true);
    }

    @Override
    public RockerModel groovyJUnitTemplate(Project project) {
        return groovyJunit.template(project, true);
    }

    @Override
    public RockerModel koTestTemplate(Project project) {
        return koTest.template(project, true);
    }

    @Override
    public RockerModel spockTemplate(Project project) {
        return spock.template(project, true);
    }
}
