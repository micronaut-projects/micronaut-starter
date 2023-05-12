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
package io.micronaut.starter.api;

import io.micronaut.context.MessageSource;
import io.micronaut.core.annotation.Creator;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.starter.api.options.ApplicationTypeSelectOptions;
import io.micronaut.starter.api.options.BuildToolSelectOptions;
import io.micronaut.starter.api.options.JdkVersionSelectOptions;
import io.micronaut.starter.api.options.LanguageSelectOptions;
import io.micronaut.starter.api.options.TestFrameworkSelectOptions;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.JdkVersionConfiguration;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Aggregator for {@link SelectOptionDTO}.
 *
 * @since 2.2.0
 */
@Schema(name = "SelectOptions")
@Introspected
public class SelectOptionsDTO {

    private ApplicationTypeSelectOptions type;

    private JdkVersionSelectOptions jdkVersion;

    private LanguageSelectOptions lang;

    private TestFrameworkSelectOptions test;

    private BuildToolSelectOptions build;

    SelectOptionsDTO() { }

    @Creator
    public SelectOptionsDTO(ApplicationTypeSelectOptions type, JdkVersionSelectOptions jdkVersion, LanguageSelectOptions lang, TestFrameworkSelectOptions test, BuildToolSelectOptions build) {
        this.type = type;
        this.jdkVersion = jdkVersion;
        this.lang = lang;
        this.test = test;
        this.build = build;
    }

    @Schema(description = "supported options for application type")
    public ApplicationTypeSelectOptions getType() {
        return type;
    }

    @Schema(description = "supported options for jdk versions")
    public JdkVersionSelectOptions getJdkVersion() {
        return jdkVersion;
    }

    @Schema(description = "supported options for code languages")
    public LanguageSelectOptions getLang() {
        return lang;
    }

    @Schema(description = "supported options for test frameworks")
    public TestFrameworkSelectOptions getTest() {
        return test;
    }

    @Schema(description = "supported options for build tools")
    public BuildToolSelectOptions getBuild() {
        return build;
    }

    /**
     * Build the options
     * @return the supported options
     */
    public static SelectOptionsDTO make(MessageSource messageSource,
                                        MessageSource.MessageContext messageContext,
                                        JdkVersionConfiguration jdkVersionConfiguration) {

        List<ApplicationTypeDTO> applications = Arrays.stream(ApplicationType.values())
                .map(it -> new ApplicationTypeDTO(it, null, messageSource, messageContext))
                .collect(Collectors.toList());

        ApplicationTypeSelectOptions applicationOpts = new ApplicationTypeSelectOptions(
                applications,
                new ApplicationTypeDTO(ApplicationType.DEFAULT_OPTION, null, messageSource, messageContext)
        );

        List<JdkVersionDTO> jdkVersions = jdkVersionConfiguration.getSupportedJdkVersions().stream()
                .map(it -> new JdkVersionDTO(it, messageSource, messageContext))
                .collect(Collectors.toList());

        JdkVersionSelectOptions jdkVersionOpts = new JdkVersionSelectOptions(
                jdkVersions,
                new JdkVersionDTO(jdkVersionConfiguration.getDefaultJdkVersion(), messageSource, messageContext)
        );

        List<LanguageDTO> languages = Arrays.stream(Language.values())
                .map(it -> new LanguageDTO(it, messageSource, messageContext))
                .collect(Collectors.toList());

        LanguageSelectOptions languageOpts = new LanguageSelectOptions(
                languages,
                new LanguageDTO(Language.DEFAULT_OPTION, messageSource, messageContext)
        );

        List<TestFrameworkDTO> testFrameworks = Arrays.stream(TestFramework.values())
                .map(it -> new TestFrameworkDTO(it, messageSource, messageContext))
                .collect(Collectors.toList());

       TestFrameworkSelectOptions testFrameworkOpts = new TestFrameworkSelectOptions(
                testFrameworks,
                new TestFrameworkDTO(TestFramework.DEFAULT_OPTION, messageSource, messageContext)
        );

       List<BuildToolDTO> buildTools = Arrays.stream(BuildTool.values())
               .map(it -> new BuildToolDTO(it, messageSource, messageContext))
               .collect(Collectors.toList());

       BuildToolSelectOptions buildToolOpts = new BuildToolSelectOptions(
               buildTools,
               new BuildToolDTO(BuildTool.DEFAULT_OPTION, messageSource, messageContext)
       );

       return new SelectOptionsDTO(applicationOpts, jdkVersionOpts, languageOpts, testFrameworkOpts, buildToolOpts);

    }
}
