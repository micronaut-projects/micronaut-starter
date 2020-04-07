package io.micronaut.starter.command;

import io.micronaut.starter.OutputHandler;
import io.micronaut.starter.Project;
import io.micronaut.starter.feature.*;
import io.micronaut.starter.feature.validation.FeatureValidator;
import io.micronaut.starter.io.FileSystemOutputHandler;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.Template;
import io.micronaut.starter.util.NameUtils;
import picocli.CommandLine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class CreateCommand extends BaseCommand implements Callable<Integer> {

    protected final AvailableFeatures availableFeatures;
    protected final FeatureValidator featureValidator;
    private final MicronautCommand command;

    @CommandLine.Parameters(arity = "0..1", paramLabel = "NAME", description = "The name of the application to create.")
    String name;

    @CommandLine.Option(names = {"-l", "--lang"}, paramLabel = "LANG", description = "Which language to use. Possible values: ${COMPLETION-CANDIDATES}.")
    Language lang;

    @CommandLine.Option(names = {"-t", "--test"}, paramLabel = "TEST", description = "Which test framework to use. Possible values: ${COMPLETION-CANDIDATES}.")
    TestFramework test;

    @CommandLine.Option(names = {"-b", "--build"}, paramLabel = "BUILD-TOOL", description = "Which build tool to configure. Possible values: ${COMPLETION-CANDIDATES}.")
    BuildTool build = BuildTool.gradle;

    @CommandLine.Option(names = {"-i", "--inplace"}, description = "Create a service using the current directory")
    boolean inplace;

    public CreateCommand(AvailableFeatures availableFeatures,
                         FeatureValidator featureValidator,
                         MicronautCommand command) {
        this.availableFeatures = availableFeatures;
        this.featureValidator = featureValidator;
        this.command = command;
    }

    protected abstract List<String> getSelectedFeatures();

    @Override
    public Integer call() throws Exception {
        Project project = NameUtils.parse(name);

        OutputHandler outputHandler = new FileSystemOutputHandler(project, inplace, this);

        generate(project, outputHandler);

        out("created " + name);
        return 0;
    }

    public void generate(OutputHandler outputHandler) throws IOException {
        generate(NameUtils.parse(name), outputHandler);
    }

    public void generate(Project project, OutputHandler outputHandler) throws IOException {
        if (project == null) {
            project = NameUtils.parse(name);
        }

        final List<Feature> features = new ArrayList<>(8);
        for (String name: getSelectedFeatures()) {
            Feature feature = availableFeatures.findFeature(name).orElse(null);
            if (feature != null) {
                features.add(feature);
            } else {
                throw new IllegalArgumentException("The requested feature does not exist: " + name);
            }
        }

        availableFeatures.getFeatures()
                .filter(f -> f instanceof DefaultFeature)
                .filter(f -> ((DefaultFeature) f).shouldApply(command, lang, features))
                .forEach(features::add);

        featureValidator.validate(lang, features);

        FeatureContext featureContext = new FeatureContext(lang, test, build, command, availableFeatures, features);

        featureContext.processSelectedFeatures();

        List<Feature> featureList = featureContext.getFinalFeatures(this);

        featureValidator.validate(lang, featureList);

        CommandContext commandContext = new CommandContext(featureContext, project);
        commandContext.getConfiguration().put("micronaut.application.name", project.getAppName());
        commandContext.addTemplate("micronautCli",
                new RockerTemplate("micronaut-cli.yml",
                        cli.template(commandContext.getLanguage(),
                                commandContext.getTestFramework(),
                                commandContext.getProject(),
                                commandContext.getFeatures())));

        for (Feature feature: featureList) {
            feature.apply(commandContext);
        }

        Map<String, String> replacements = project.getProperties();

        for (Template template: commandContext.getTemplates().values()) {
            String path = replaceVariables(template.getPath(), replacements);
            outputHandler.write(path, template);
        }

        outputHandler.close();
    }

    private String replaceVariables(String path, Map<String, String> replacements) {
        Pattern pattern = Pattern.compile("\\{(.+?)\\}");
        Matcher matcher = pattern.matcher(path);
        StringBuilder builder = new StringBuilder();
        int i = 0;
        while (matcher.find()) {
            String replacement = replacements.get(matcher.group(1));
            builder.append(path, i, matcher.start());
            if (replacement == null)
                builder.append(matcher.group(0));
            else
                builder.append(replacement);
            i = matcher.end();
        }
        builder.append(path.substring(i));
        return builder.toString();
    }
}
