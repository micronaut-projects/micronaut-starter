/*
 * Copyright 2017-2026 original authors
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
package io.micronaut.starter.feature.mateu;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.CommunityFeature;
import io.micronaut.starter.feature.JavaSpecificFeature;
import io.micronaut.starter.feature.server.MicronautServerDependent;
import io.micronaut.starter.rocker.feature.mateu.template.mateuHomeJava;
import io.micronaut.starter.template.RockerTemplate;
import jakarta.inject.Singleton;

/**
 * Adds <a href="https://mateu.io">Mateu</a>: classes annotated with {@code @UI} are rendered as a
 * complete web UI (forms, CRUDs, wizards, the app shell) with the Oracle Redwood look, with no
 * frontend code. The Mateu annotation processor generates one Micronaut controller per {@code @UI}
 * class; the adapter serves the renderer's assets from the renderer jar, so no configuration is
 * needed.
 */
@Requires(property = "micronaut.starter.feature.mateu.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Mateu implements CommunityFeature, JavaSpecificFeature, MicronautServerDependent {

    public static final String NAME = "mateu";

    public static final String ARTIFACT_ID_MATEU_MICRONAUT = "mateu-micronaut";
    public static final String ARTIFACT_ID_MATEU_REDWOOD = "mateu-redwood";
    public static final String ARTIFACT_ID_MATEU_ANNOTATION_PROCESSOR = "mateu-annotation-processor-micronaut";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getCommunityFeatureName() {
        return NAME;
    }

    @Override
    public String getCommunityFeatureTitle() {
        return "Mateu";
    }

    @Override
    public String getCommunityContributor() {
        return "Mateu";
    }

    @Override
    public String getTitle() {
        return "Mateu";
    }

    @Override
    public String getDescription() {
        return "Declare screens as plain Java classes (@UI) and render them as a complete web UI (forms, CRUDs, wizards, the app shell) with Oracle Redwood, with no frontend code";
    }

    @Override
    public String getCategory() {
        return Category.VIEW;
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://mateu.io";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .lookupArtifactId(ARTIFACT_ID_MATEU_MICRONAUT)
                .compile());
        generatorContext.addDependency(Dependency.builder()
                .lookupArtifactId(ARTIFACT_ID_MATEU_REDWOOD)
                .runtime());
        generatorContext.addDependency(Dependency.builder()
                .lookupArtifactId(ARTIFACT_ID_MATEU_ANNOTATION_PROCESSOR)
                .annotationProcessor());

        String homeSource = generatorContext.getSourcePath("/{packagePath}/Home");
        generatorContext.addTemplate("mateuHome", new RockerTemplate(homeSource, mateuHomeJava.template(generatorContext.getProject())));
    }
}
