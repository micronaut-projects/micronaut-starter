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
package io.micronaut.starter.feature.picocli.test.spock;

import io.micronaut.core.naming.NameUtils;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.template.RockerTemplate;

import javax.inject.Singleton;

@Singleton
public class PicocliSpock implements Feature {

    @Override
    public String getName() {
        return "picocli-spock";
    }

    @Override
    public String getTitle() {
        return NameUtils.camelCase(getName());
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addTemplate("picocliSpock", getTemplate(generatorContext.getProject()));
    }

    public RockerTemplate getTemplate(Project project) {
        return new RockerTemplate("src/test/groovy/{packagePath}/{className}CommandSpec.groovy", picocliSpockTest.template(project));
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.CLI;
    }

    @Override
    public boolean isVisible() {
        return false;
    }
}
