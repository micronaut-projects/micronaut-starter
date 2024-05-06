package io.micronaut.starter.cli.command;

import io.micronaut.context.ApplicationContext;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.OperatingSystem;
import io.micronaut.starter.application.generator.ProjectGenerator;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.architecture.Arm;
import io.micronaut.starter.feature.architecture.X86;
import io.micronaut.starter.feature.aws.LambdaFunctionUrl;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.util.NameUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CreateLambdaCommandTest {

    static ApplicationContext applicationContext;
    static CreateLambdaBuilderCommand command;

    @BeforeAll
    static void setUp() {
        applicationContext = ApplicationContext.run();
        command = applicationContext.getBean(CreateLambdaBuilderCommand.class);
    }

    @AfterAll
    static void tearDown() {
        if (applicationContext != null) {
            applicationContext.close();
        }
    }

    static Stream<CreateLambdaCommandCliOptions> provideCliOptions() {
        return Stream.of(CodingStyle.values())
                .flatMap(codingStyle -> Stream.of(LambdaDeployment.values())
                        .flatMap(lambdaDeployment -> Stream.of(applicationContext.getBean(Arm.class), applicationContext.getBean(X86.class))
                                .flatMap(cpuArchitecture -> Stream.of(false, true)
                                        .flatMap(cdk -> Stream.of(CreateLambdaBuilderCommand.languagesForDeployment(lambdaDeployment))
                                                .flatMap(language -> Stream.of(TestFramework.JUNIT, TestFramework.SPOCK, TestFramework.KOTEST)
                                                        .flatMap(testFramework -> Stream.of(BuildTool.GRADLE, BuildTool.GRADLE_KOTLIN, BuildTool.MAVEN)
                                                                .flatMap(buildTool -> getAllApiFeatures(codingStyle).stream()
                                                                        .flatMap(feature -> command.getJdkVersionCandidates().stream()
                                                                                .map(jdkVersion -> new CreateLambdaCommandCliOptions(
                                                                                        codingStyle,
                                                                                        feature,
                                                                                        getAllApiFeatures(codingStyle),
                                                                                        lambdaDeployment,
                                                                                        cpuArchitecture,
                                                                                        cdk,
                                                                                        language,
                                                                                        CreateLambdaBuilderCommand.languagesForDeployment(lambdaDeployment),
                                                                                        testFramework,
                                                                                        buildTool,
                                                                                        jdkVersion,
                                                                                        command.getJdkVersionCandidates(),
                                                                                        invalid(feature, cdk, buildTool, lambdaDeployment)
                                                                                ))
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                );
    }

    /**
     * We don't support GraalVM with Maven and CDK, though the tool does allow this combination.
     */
    private static String invalid(Feature feature, boolean cdk, BuildTool buildTool, LambdaDeployment lambdaDeployment) {
        boolean hasCdk = feature instanceof LambdaFunctionUrl || cdk;
        if (hasCdk && buildTool == BuildTool.MAVEN && lambdaDeployment == LambdaDeployment.NATIVE_EXECUTABLE) {
            return "Maven, CDK and GraalVM are not yet supported";
        }
        return null;
    }

    private static List<Feature> getAllApiFeatures(CodingStyle codingStyle) {
        return codingStyle == CodingStyle.CONTROLLERS ?
                CreateLambdaBuilderCommand.apiTriggerFeatures(ApplicationType.DEFAULT, applicationContext.getBeansOfType(Feature.class)) :
                CreateLambdaBuilderCommand.triggerFeatures(applicationContext.getBeansOfType(Feature.class));
    }

    @ParameterizedTest
    @MethodSource("provideCliOptions")
    void testCliOptions(CreateLambdaCommandCliOptions cliOptions) {
        CreateLambdaBuilderCommand command = applicationContext.getBean(CreateLambdaBuilderCommand.class);
        ProjectGenerator projectGenerator = applicationContext.getBean(ProjectGenerator.class);

        GenerateOptions options = command.createGenerateOptions(new StubbedLineReader(cliOptions));

        assertEquals(cliOptions.getExpectedApplicationType(), options.getApplicationType());
        assertEquals(new HashSet<>(cliOptions.getFeatures()), options.getFeatures());
        assertEquals(cliOptions.language, options.getOptions().getLanguage());
        assertEquals(cliOptions.buildTool, options.getOptions().getBuildTool());
        assertEquals(cliOptions.testFramework, options.getOptions().getTestFramework());
        assertEquals(cliOptions.javaVersion, options.getOptions().getJavaVersion().asString());

        if (cliOptions.expectedExceptionMessage != null) {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> generate(projectGenerator, options));
            assertEquals(cliOptions.expectedExceptionMessage, ex.getMessage());
        } else {
            assertDoesNotThrow(() -> generate(projectGenerator, options)
            );
        }
    }

    private static void generate(ProjectGenerator projectGenerator, GenerateOptions options) throws Exception {
        projectGenerator.generate(
                options.getApplicationType(),
                NameUtils.parse("foo"),
                options.getOptions(),
                OperatingSystem.LINUX,
                options.getFeatures().stream().toList(),
                new TemplateResolvingOutputHandler(),
                new LoggingConsoleOutput()
        );
    }
}
