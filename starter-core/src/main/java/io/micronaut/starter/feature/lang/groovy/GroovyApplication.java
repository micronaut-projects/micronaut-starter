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
package io.micronaut.starter.feature.lang.groovy;

import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.test.template.groovyJunit;
import io.micronaut.starter.feature.test.template.koTest;
import io.micronaut.starter.feature.test.template.spock;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.DefaultTestRockerModelProvider;
import io.micronaut.starter.options.TestRockerModelProvider;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.template.RockerTemplate;

import javax.inject.Singleton;

@Singleton
public class GroovyApplication implements GroovyApplicationFeature {

    @Override
    @Nullable
    public String mainClassName(GeneratorContext generatorContext) {
        return generatorContext.getProject().getPackageName() + ".Application";
    }

    @Override
    public String getName() {
        return "groovy-application";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType != ApplicationType.CLI && applicationType != ApplicationType.FUNCTION;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        GroovyApplicationFeature.super.apply(generatorContext);

        if (shouldGenerateApplicationFile(generatorContext)) {
            generatorContext.addTemplate("application", new RockerTemplate(getPath(),
                    application.template(generatorContext.getProject(), generatorContext.getFeatures())));
            TestFramework testFramework = generatorContext.getTestFramework();
            String testSourcePath = generatorContext.getTestSourcePath("/{packagePath}/{className}");
            Project project = generatorContext.getProject();
            TestRockerModelProvider provider = new DefaultTestRockerModelProvider(spock.template(project),
                    groovyJunit.template(project),
                    groovyJunit.template(project),
                    groovyJunit.template(project),
                    koTest.template(project));
            generatorContext.addTemplate("applicationTest",
                    new RockerTemplate(testSourcePath, provider.findModel(generatorContext.getLanguage(), testFramework))
            );
        }
    }

    protected boolean shouldGenerateApplicationFile(GeneratorContext generatorContext) {
        return (generatorContext.getApplicationType() == ApplicationType.DEFAULT && generatorContext.getBuildTool() == BuildTool.GRADLE)
                || !generatorContext.getFeatures().hasFunctionFeature();
    }

    protected String getPath() {
        return "src/main/groovy/{packagePath}/Application.groovy";
    }
}
