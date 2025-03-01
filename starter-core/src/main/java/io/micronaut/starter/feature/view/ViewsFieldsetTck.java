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
package io.micronaut.starter.feature.view;

import com.fizzed.rocker.RockerModel;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.micronaut.MicronautOptions;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.starter.feature.test.Junit;
import io.micronaut.starter.feature.test.JunitPlatformSuiteEngine;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.rocker.RockerTemplate;
import jakarta.inject.Singleton;

@Requires(property = "micronaut.starter.feature.views.fieldset.tck.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class ViewsFieldsetTck implements Feature {

    public static final String NAME = "views-fieldset-tck";
    private static final String ARTIFACT_ID_MICRONAUT_VIEWS_FIELDSET_TCK = "micronaut-views-fieldset-tck";
    private static final Dependency DEPENDENCY_VIEWS_FIELDSET_TCK =
            MicronautDependencyUtils.viewsDependency().artifactId(ARTIFACT_ID_MICRONAUT_VIEWS_FIELDSET_TCK)
                    .test()
                    .build();

    private final JunitPlatformSuiteEngine junitPlatformSuiteEngine;

    public ViewsFieldsetTck(JunitPlatformSuiteEngine junitPlatformSuiteEngine) {
        this.junitPlatformSuiteEngine = junitPlatformSuiteEngine;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (featureContext.isPresent(Junit.class)) {
            featureContext.addFeatureIfNotPresent(JunitPlatformSuiteEngine.class, junitPlatformSuiteEngine);
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Fieldset TCK";
    }

    @Override
    public String getDescription() {
        return "Form & Fieldset Generator (TCK) Test Compatibility Kit.";
    }

    @Override
    public boolean supports(Options options) {
        return options instanceof MicronautOptions mnOptions && mnOptions.applicationType() == ApplicationType.DEFAULT;
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public String getCategory() {
        return Category.VIEW;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addThymeleafSuite(generatorContext);
        addDependencies(generatorContext);
    }

    private void addThymeleafSuite(GeneratorContext generatorContext) {
        if (generatorContext.getLanguage() == Language.JAVA) {
            RockerModel rockerModel = thymeleafSuite.template(generatorContext.getProject());
            String templateName = "thymeleafSuite";
            String extension = generatorContext.getLanguage().getExtension();
            String srcDir = generatorContext.getLanguage().getTestSrcDir();
            generatorContext.addTemplate(templateName,
                    new RockerTemplate(srcDir + "/{packagePath}/ThymeleafSuite." + extension, rockerModel));
        }
    }

    private void addDependencies(GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_VIEWS_FIELDSET_TCK);
    }
}
