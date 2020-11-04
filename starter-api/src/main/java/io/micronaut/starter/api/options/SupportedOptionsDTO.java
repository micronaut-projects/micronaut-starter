package io.micronaut.starter.api.options;

import io.micronaut.core.annotation.Creator;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.starter.api.*;
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
 * Aggregator for {@link SupportedOptionDTO}.
 *
 * @since 2.2.0
 */
@Schema(name = "SupportedOptionsInfo")
@Introspected
public class SupportedOptionsDTO {
    
    SupportedOptionDTO<ApplicationTypeDTO>types;
    SupportedOptionDTO<JdkVersionDTO>jdkVersions;
    SupportedOptionDTO<LanguageDTO>languages;
    SupportedOptionDTO<TestFrameworkDTO>testFrameworks;
    SupportedOptionDTO<BuildToolDTO>buildTools;

    public SupportedOptionsDTO() { }

    @Creator
    public SupportedOptionsDTO(SupportedOptionDTO<ApplicationTypeDTO> types, SupportedOptionDTO<JdkVersionDTO> jdkVersions, SupportedOptionDTO<LanguageDTO> languages, SupportedOptionDTO<TestFrameworkDTO>testFrameworks, SupportedOptionDTO<BuildToolDTO>buildTools) {
        this.types = types;
        this.jdkVersions = jdkVersions;
        this.languages = languages;
        this.testFrameworks = testFrameworks;
        this.buildTools = buildTools;
    }

    @Schema(description = "supported options for application type")
    public SupportedOptionDTO<ApplicationTypeDTO> getTypes() {
        return types;
    }

    @Schema(description = "supported options for jdk versions")
    public SupportedOptionDTO<JdkVersionDTO> getJdkVersions() {
        return jdkVersions;
    }

    @Schema(description = "supported options for code languages")
    public SupportedOptionDTO<LanguageDTO> getLanguages() {
        return languages;
    }

    @Schema(description = "supported options for test frameworks")
    public SupportedOptionDTO<TestFrameworkDTO> getTestFrameworks() {
        return testFrameworks;
    }

    @Schema(description = "supported options for build tools")
    public SupportedOptionDTO<BuildToolDTO> getBuildTools() {
        return buildTools;
    }

    /**
     * Build the options
     * @return the supported options
     */
    public static SupportedOptionsDTO make() {

        List<ApplicationTypeDTO> applications = Arrays.stream(ApplicationType.values())
                .map(it-> new ApplicationTypeDTO(it, null))
                .collect(Collectors.toList());

        SupportedOptionDTO<ApplicationTypeDTO> applicationOpts = new SupportedOptionDTO<ApplicationTypeDTO>(
                applications,
                new ApplicationTypeDTO(ApplicationType.DEFAULT_OPTION, null)
        );

        List<JdkVersionDTO> jdkVersions = Arrays.stream(JdkVersion.values())
                .map(JdkVersionDTO::new)
                .collect(Collectors.toList());

        SupportedOptionDTO<JdkVersionDTO> jdkVersionOpts = new SupportedOptionDTO<JdkVersionDTO>(
                jdkVersions,
                new JdkVersionDTO(JdkVersion.DEFAULT_OPTION)
        );

        List<LanguageDTO> languages = Arrays.stream(Language.values())
                .map(LanguageDTO::new)
                .collect(Collectors.toList());

        SupportedOptionDTO<LanguageDTO> languageOpts = new SupportedOptionDTO<LanguageDTO>(
                languages,
                new LanguageDTO(Language.DEFAULT_OPTION)
        );

        List<TestFrameworkDTO> testFrameworks = Arrays.stream(TestFramework.values())
                .map(TestFrameworkDTO::new)
                .collect(Collectors.toList());

       SupportedOptionDTO<TestFrameworkDTO> testFrameworkOpts = new SupportedOptionDTO<TestFrameworkDTO>(
                testFrameworks,
                new TestFrameworkDTO(TestFramework.DEFAULT_OPTION)
        );

       List<BuildToolDTO> buildTools = Arrays.stream(BuildTool.values())
               .map(BuildToolDTO::new)
               .collect(Collectors.toList());

       SupportedOptionDTO<BuildToolDTO> buildToolOpts = new SupportedOptionDTO<BuildToolDTO>(
               buildTools,
               new BuildToolDTO(BuildTool.DEFAULT_OPTION)
       );

       return new SupportedOptionsDTO(applicationOpts, jdkVersionOpts, languageOpts, testFrameworkOpts, buildToolOpts);

    }
}
