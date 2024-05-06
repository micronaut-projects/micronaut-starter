package io.micronaut.starter.cli.command;

import io.micronaut.context.ApplicationContext;
import io.micronaut.starter.application.OperatingSystem;
import io.micronaut.starter.application.generator.ProjectGenerator;
import io.micronaut.starter.feature.architecture.Arm;
import io.micronaut.starter.feature.architecture.X86;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.JdkVersion;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.util.NameUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashSet;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateChatBotBuilderCommandTest {

    static ApplicationContext applicationContext;

    @BeforeAll
    static void setUp() {
        applicationContext = ApplicationContext.run();
    }

    @AfterAll
    static void tearDown() {
        if (applicationContext != null) {
            applicationContext.close();
        }
    }

    static Stream<CreateChatbotCommandCliOptions> provideCliOptions() {
        return Stream.of(CreateChatBotBuilderCommand.ChatBotType.values())
                .flatMap(chatBotType -> Stream.of(CreateChatBotBuilderCommand.ChatBotDeployment.values())
                        .flatMap(applicationType -> Stream.of(applicationContext.getBean(Arm.class), applicationContext.getBean(X86.class))
                                .flatMap(cpuArchitecture -> Stream.of(false, true)
                                        .flatMap(cdk -> Stream.of(Language.JAVA, Language.GROOVY, Language.KOTLIN)
                                                .flatMap(language -> Stream.of(TestFramework.JUNIT, TestFramework.SPOCK, TestFramework.KOTEST)
                                                        .flatMap(testFramework -> Stream.of(BuildTool.GRADLE, BuildTool.GRADLE_KOTLIN, BuildTool.MAVEN)
                                                                .flatMap(buildTool ->
                                                                        (applicationType == CreateChatBotBuilderCommand.ChatBotDeployment.AZURE ? Stream.of(JdkVersion.JDK_17) : Stream.of(JdkVersion.JDK_17, JdkVersion.JDK_21))
                                                                                .map(javaVersion ->
                                                                                        new CreateChatbotCommandCliOptions(
                                                                                                chatBotType,
                                                                                                applicationType,
                                                                                                cpuArchitecture,
                                                                                                cdk,
                                                                                                language,
                                                                                                testFramework,
                                                                                                buildTool,
                                                                                                javaVersion
                                                                                        )
                                                                                )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                );
    }

    @ParameterizedTest
    @MethodSource("provideCliOptions")
    void testCliOptions(CreateChatbotCommandCliOptions cliOptions) {
        CreateChatBotBuilderCommand command = applicationContext.getBean(CreateChatBotBuilderCommand.class);
        ProjectGenerator projectGenerator = applicationContext.getBean(ProjectGenerator.class);

        GenerateOptions options = command.createGenerateOptions(new StubbedLineReader(cliOptions));

        assertEquals(cliOptions.getExpectedApplicationType(), options.getApplicationType());
        assertEquals(new HashSet<>(cliOptions.getFeatures()), options.getFeatures());
        assertEquals(cliOptions.language, options.getOptions().getLanguage());
        assertEquals(cliOptions.buildTool, options.getOptions().getBuildTool());
        assertEquals(cliOptions.testFramework, options.getOptions().getTestFramework());
        assertEquals(cliOptions.javaVersion, options.getOptions().getJavaVersion());

        assertDoesNotThrow(() -> projectGenerator.generate(
                        options.getApplicationType(),
                        NameUtils.parse("foo"),
                        options.getOptions(),
                        OperatingSystem.LINUX,
                        cliOptions.getFeatures(),
                        new TemplateResolvingOutputHandler(),
                        new LoggingConsoleOutput()
                )
        );
    }

}
