package io.micronaut.starter.feature.picocli;

import io.micronaut.context.annotation.Parameter;
import io.micronaut.context.annotation.Prototype;
import io.micronaut.starter.CodeGenConfig;
import io.micronaut.starter.Project;
import io.micronaut.starter.command.CodeGenCommand;
import io.micronaut.starter.command.MicronautCommand;
import io.micronaut.starter.feature.picocli.groovy.PicocliGroovyApplication;
import io.micronaut.starter.feature.picocli.java.PicocliJavaApplication;
import io.micronaut.starter.feature.picocli.kotlin.PicocliKotlinApplication;
import io.micronaut.starter.feature.test.Junit;
import io.micronaut.starter.feature.test.KotlinTest;
import io.micronaut.starter.feature.test.Spock;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.TemplateRenderer;
import picocli.CommandLine;

import javax.inject.Singleton;

@CommandLine.Command(name = "create-command", description = "Creates a CLI command")
@Prototype
public class CreateCommandCommand extends CodeGenCommand {

    private final PicocliJavaApplication javaApplication;
    private final Junit junit;
    private final PicocliGroovyApplication groovyApplication;
    private final Spock spock;
    private final PicocliKotlinApplication kotlinApplication;
    private final KotlinTest kotlinTest;
    @CommandLine.Parameters(paramLabel = "COMMAND-NAME", description = "The name of the command class to create")
    String name;

    public CreateCommandCommand(@Parameter CodeGenConfig config,
                                PicocliJavaApplication javaApplication,
                                Junit junit,
                                PicocliGroovyApplication groovyApplication,
                                Spock spock,
                                PicocliKotlinApplication kotlinApplication,
                                KotlinTest kotlinTest) {
        super(config);
        this.javaApplication = javaApplication;
        this.junit = junit;
        this.groovyApplication = groovyApplication;
        this.spock = spock;
        this.kotlinApplication = kotlinApplication;
        this.kotlinTest = kotlinTest;
    }

    @Override
    public boolean applies() {
        return config.getCommand() == MicronautCommand.CREATE_CLI;
    }

    @Override
    public Integer call() throws Exception {

        Project project = getProject(name, config);
        TemplateRenderer templateRenderer = getTemplateRenderer(project);

        if (config.getSourceLanguage() == Language.java) {
            templateRenderer.render(javaApplication.getTemplate(project), (path) -> {
                out("Rendered command to " + path);
            });
        }

        if (config.getTestFramework() == TestFramework.junit) {
            templateRenderer.render(junit.getPicocliTemplate(project));
        }
/*

        private SupportedLanguage sniffProjectLanguage() {
            if (file("src/main/groovy").exists()) {
                SupportedLanguage.groovy
            } else if (file("src/main/kotlin").exists()) {
                SupportedLanguage.kotlin
            } else {
                SupportedLanguage.java
            }
        }

        def model = model(commandName).forConvention("Command")
        String artifactPath = "${model.packagePath}/${model.className}"
        lang = lang ?: SupportedLanguage.findValue(config.sourceLanguage).orElse(sniffProjectLanguage())

        render template: template("${lang}/Command.${lang.extension}"),
                destination: file("src/main/${lang}/${artifactPath}.${lang.extension}"),
                model: model,
                overwrite: overwrite

        def testFramework = config.testFramework
        String testConvention = "Test"

        if (lang == SupportedLanguage.kotlin) {
            if (testFramework == "spek" || testFramework == "junit") {
                testConvention = testFramework.capitalize()
            } else if (testFramework == "spock") {
                lang = SupportedLanguage.groovy // allow the groovy block to handle
            }
        }

        if (lang == SupportedLanguage.java) {
            if (testFramework == "spock") {
                lang = SupportedLanguage.groovy // allow the groovy block to handle
            }
        }

        if (lang == SupportedLanguage.groovy) {
            if (testFramework != "junit") {
                testConvention = "Spec"
            }
        }

        render template: template("${lang}/Command${testConvention}.${lang.extension}"),
                destination: file("src/test/${lang}/${artifactPath}${testConvention}.${lang.extension}"),
                model: model,
                overwrite: overwrite
*/

        return 0;
    }
}
