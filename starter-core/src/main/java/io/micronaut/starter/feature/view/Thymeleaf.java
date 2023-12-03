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
package io.micronaut.starter.feature.view;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.server.MicronautServerDependent;
import io.micronaut.starter.template.URLTemplate;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class Thymeleaf implements ViewFeature, MicronautServerDependent {

    private static final String ARTIFACT_ID_MICRONAUT_VIEWS_THYMELEAF = "micronaut-views-thymeleaf";
    private static final Dependency.Builder DEPENDENCY_MICRONAUT_VIEWS_THYMELEAF = MicronautDependencyUtils.viewsDependency()
            .artifactId(ARTIFACT_ID_MICRONAUT_VIEWS_THYMELEAF)
            .compile();
    private static final String LAYOUT_HTML = "layout.html";
    private static final String RESOURCES_THYMELEAF_PATH = "views/thymeleaf/";

    private final ViewsFieldset viewsFieldset;

    @Inject
    public Thymeleaf(ViewsFieldset viewsFieldset) {
        this.viewsFieldset = viewsFieldset;
    }

    /**
     *
     * @deprecated Use {@link Thymeleaf(ViewsFieldset)} instead.
     */
    @Deprecated(forRemoval = true, since = "4.2.0")
    public Thymeleaf() {
        this(new ViewsFieldset());
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.addFeatureIfNotPresent(ViewsFieldset.class, viewsFieldset);
    }

    @Override
    public String getName() {
        return "views-thymeleaf";
    }

    @Override
    public String getTitle() {
        return "Thymeleaf Views";
    }

    @Override
    public String getDescription() {
        return "Adds support for Server-Side View Rendering using Thymeleaf";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://www.thymeleaf.org/";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-views/latest/guide/index.html#thymeleaf";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addDependencies(generatorContext);
    }

    protected void addDependencies(GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_MICRONAUT_VIEWS_THYMELEAF);
        addLayout(generatorContext);
    }

    private void addLayout(GeneratorContext generatorContext) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        generatorContext.addTemplate(LAYOUT_HTML, new URLTemplate(VIEWS_PATH + LAYOUT_HTML, classLoader.getResource(RESOURCES_THYMELEAF_PATH +  LAYOUT_HTML)));
    }
}
