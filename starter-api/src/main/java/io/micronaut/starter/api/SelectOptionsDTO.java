package io.micronaut.starter.api;

import io.micronaut.context.MessageSource;
import io.micronaut.core.annotation.Creator;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.JdkVersion;
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
@Schema(name = "SupportedOptions")
@Introspected
public class SelectOptionsDTO {

    SelectOptionDTO<ApplicationTypeDTO> types;
    SelectOptionDTO<JdkVersionDTO> jdkVersions;
    SelectOptionDTO<LanguageDTO> languages;
    SelectOptionDTO<TestFrameworkDTO> testFrameworks;
    SelectOptionDTO<BuildToolDTO> buildTools;

    public SelectOptionsDTO() { }

    @Creator
    public SelectOptionsDTO(SelectOptionDTO<ApplicationTypeDTO> types, SelectOptionDTO<JdkVersionDTO> jdkVersions, SelectOptionDTO<LanguageDTO> languages, SelectOptionDTO<TestFrameworkDTO> testFrameworks, SelectOptionDTO<BuildToolDTO> buildTools) {
        this.types = types;
        this.jdkVersions = jdkVersions;
        this.languages = languages;
        this.testFrameworks = testFrameworks;
        this.buildTools = buildTools;
    }

    @Schema(description = "supported options for application type")
    public SelectOptionDTO<ApplicationTypeDTO> getTypes() {
        return types;
    }

    @Schema(description = "supported options for jdk versions")
    public SelectOptionDTO<JdkVersionDTO> getJdkVersions() {
        return jdkVersions;
    }

    @Schema(description = "supported options for code languages")
    public SelectOptionDTO<LanguageDTO> getLanguages() {
        return languages;
    }

    @Schema(description = "supported options for test frameworks")
    public SelectOptionDTO<TestFrameworkDTO> getTestFrameworks() {
        return testFrameworks;
    }

    @Schema(description = "supported options for build tools")
    public SelectOptionDTO<BuildToolDTO> getBuildTools() {
        return buildTools;
    }

    /**
     * Build the options
     * @return the supported options
     */
    public static SelectOptionsDTO make(MessageSource messageSource, MessageSource.MessageContext messageContext) {

        List<ApplicationTypeDTO> applications = Arrays.stream(ApplicationType.values())
                .map(it-> new ApplicationTypeDTO(it, null, messageSource, messageContext))
                .collect(Collectors.toList());

        SelectOptionDTO<ApplicationTypeDTO> applicationOpts = new SelectOptionDTO<ApplicationTypeDTO>(
                applications,
                new ApplicationTypeDTO(ApplicationType.DEFAULT_OPTION, null, messageSource, messageContext)
        );

        List<JdkVersionDTO> jdkVersions = Arrays.stream(JdkVersion.values())
                .map(it -> new JdkVersionDTO(it, messageSource, messageContext))
                .collect(Collectors.toList());

        SelectOptionDTO<JdkVersionDTO> jdkVersionOpts = new SelectOptionDTO<JdkVersionDTO>(
                jdkVersions,
                new JdkVersionDTO(JdkVersion.DEFAULT_OPTION, messageSource, messageContext)
        );

        List<LanguageDTO> languages = Arrays.stream(Language.values())
                .map(it->new LanguageDTO(it, messageSource, messageContext))
                .collect(Collectors.toList());

        SelectOptionDTO<LanguageDTO> languageOpts = new SelectOptionDTO<LanguageDTO>(
                languages,
                new LanguageDTO(Language.DEFAULT_OPTION, messageSource, messageContext)
        );

        List<TestFrameworkDTO> testFrameworks = Arrays.stream(TestFramework.values())
                .map(it->new TestFrameworkDTO(it, messageSource, messageContext))
                .collect(Collectors.toList());

       SelectOptionDTO<TestFrameworkDTO> testFrameworkOpts = new SelectOptionDTO<TestFrameworkDTO>(
                testFrameworks,
                new TestFrameworkDTO(TestFramework.DEFAULT_OPTION, messageSource, messageContext)
        );

       List<BuildToolDTO> buildTools = Arrays.stream(BuildTool.values())
               .map(it->new BuildToolDTO(it, messageSource, messageContext))
               .collect(Collectors.toList());

       SelectOptionDTO<BuildToolDTO> buildToolOpts = new SelectOptionDTO<BuildToolDTO>(
               buildTools,
               new BuildToolDTO(BuildTool.DEFAULT_OPTION, messageSource, messageContext)
       );

       return new SelectOptionsDTO(applicationOpts, jdkVersionOpts, languageOpts, testFrameworkOpts, buildToolOpts);

    }
}
