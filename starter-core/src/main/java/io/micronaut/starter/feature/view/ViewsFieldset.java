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

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.template.URLTemplate;
import jakarta.inject.Singleton;

import java.util.Arrays;
import java.util.List;

@Singleton
public class ViewsFieldset implements Feature {
    public static final String NAME = "views-fieldset";
    private static final String ARTIFACT_ID_MICRONAUT_VIEWS_FIELDSET = "micronaut-views-fieldset";
    private static final Dependency DEPENDENCY_VIEWS_FIELDSET =
            MicronautDependencyUtils.viewsDependency().artifactId(ARTIFACT_ID_MICRONAUT_VIEWS_FIELDSET)
                    .compile()
                    .build();
    private static final String ERRORS_HTML = "errors.html";
    private static final String FIELDSET_HTML = "fieldset.html";
    private static final String FORM_HTML = "form.html";
    private static final String INPUTCHECKBOX_HTML = "inputcheckbox.html";
    private static final String INPUTDATE_HTML = "inputdate.html";
    private static final String INPUTDATETIMELOCAL_HTML = "inputdatetimelocal.html";
    private static final String INPUTEMAIL_HTML = "inputemail.html";
    private static final String INPUTHIDDEN_HTML = "inputhidden.html";
    private static final String INPUTNUMBER_HTML = "inputnumber.html";
    private static final String INPUTPASSWORD_HTML = "inputpassword.html";
    private static final String INPUTRADIO_HTML = "inputradio.html";
    private static final String INPUTRADIOS_HTML = "inputradios.html";
    private static final String INPUTSTRING_HTML = "inputstring.html";
    private static final String INPUTSUBMIT_HTML = "inputsubmit.html";
    private static final String INPUTTEL_HTML = "inputtel.html";
    private static final String INPUTTEXT_HTML = "inputtext.html";
    private static final String INPUTTIME_HTML = "inputtime.html";
    private static final String INPUTURL_HTML = "inputurl.html";
    private static final String LABEL_HTML = "label.html";
    private static final String OPTION_HTML = "option.html";
    private static final String SELECT_HTML = "select.html";
    private static final String TEXTAREA_HTML = "textarea.html";
    private static final String TRIXEDITOR_HTML = "trixeditor.html";

    private static final List<String> THYMELEAF_FRAGMENTS = Arrays.asList(
            ERRORS_HTML,
            FIELDSET_HTML,
            FORM_HTML,
            INPUTCHECKBOX_HTML,
            INPUTDATE_HTML,
            INPUTDATETIMELOCAL_HTML,
            INPUTEMAIL_HTML,
            INPUTHIDDEN_HTML,
            INPUTNUMBER_HTML,
            INPUTPASSWORD_HTML,
            INPUTRADIO_HTML,
            INPUTRADIOS_HTML,
            INPUTSTRING_HTML,
            INPUTSUBMIT_HTML,
            INPUTTEL_HTML,
            INPUTTEXT_HTML,
            INPUTTIME_HTML,
            INPUTURL_HTML,
            LABEL_HTML,
            OPTION_HTML,
            SELECT_HTML,
            TEXTAREA_HTML,
            TRIXEDITOR_HTML
    );
    private static final String FIELDSET_PATH = "src/main/resources/views/fieldset/";
    private static final String RESOURCES_THYMELEAF_PATH = "views/thymeleaf/fieldset/";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Form & Fieldset Generator";
    }

    @Override
    public String getDescription() {
        return "Adds the views-fieldset dependency, which provides an API to simplify the generation of an HTML Fieldset representation for a given type or instance.";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT;
    }

    @Override
    public String getCategory() {
        return Category.VIEW;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.hasFeature(Thymeleaf.class)) {
            addThymeleafTemplates(generatorContext);
        }
        addDependencies(generatorContext);
    }

    private void addThymeleafTemplates(GeneratorContext generatorContext) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        for (String fileName : THYMELEAF_FRAGMENTS) {
            generatorContext.addTemplate(fileName, new URLTemplate(FIELDSET_PATH + fileName, classLoader.getResource(RESOURCES_THYMELEAF_PATH +  fileName)));
        }
    }

    private void addDependencies(GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_VIEWS_FIELDSET);
    }
}
