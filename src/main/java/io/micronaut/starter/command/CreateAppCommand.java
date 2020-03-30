package io.micronaut.starter.command;

import io.micronaut.starter.OutputHandler;
import io.micronaut.starter.Project;
import io.micronaut.starter.feature.*;
import io.micronaut.starter.io.FileSystemOutputHandler;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.Template;
import io.micronaut.starter.template.YamlTemplate;
import io.micronaut.starter.util.NameUtils;
import org.yaml.snakeyaml.Yaml;
import picocli.CommandLine;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@CommandLine.Command(name = CreateAppCommand.NAME, description = "Creates an application")
public class CreateAppCommand extends BaseCommand implements Callable<Integer> {

    public static final String NAME = "create-app";

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

    @CommandLine.Option(names = {"-f", "--features"}, paramLabel = "FEATURE", split = ",", description = "The features to use. Possible values: ${COMPLETION-CANDIDATES}", completionCandidates = CreateAppFeatures.class)
    List<String> features = new ArrayList<>();


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

        AvailableFeatures availableFeatures = new CreateAppFeatures();
        final List<Feature> features = new ArrayList<>(8);
        for (String name: this.features) {
            Feature feature = availableFeatures.findFeature(name).orElse(null);
            if (feature != null) {
                features.add(feature);
            } else {
                throw new IllegalArgumentException("The requested feature does not exist: " + name);
            }
        }

        availableFeatures.getFeatures()
                .filter(f -> f instanceof DefaultFeature)
                .filter(f -> ((DefaultFeature) f).shouldApply(availableFeatures.getCommand(), features))
                .forEach(features::add);

        FeatureContext featureContext = new FeatureContext(lang, test, build, availableFeatures, features);

        featureContext.processSelectedFeatures();

        List<Feature> featureList = featureContext.getFeatures();

        validateOneOf(featureList);

        CommandContext commandContext = new CommandContext(featureContext, project);
        commandContext.getConfiguration().put("micronaut.application.name", project.getAppName());
        commandContext.addTemplate("micronautCli",
                new RockerTemplate("micronaut-cli.yml",
                        cli.template(commandContext.getLanguage(),
                                commandContext.getTestFramework(),
                                commandContext.getProject(),
                                commandContext.getFeatures())));

        for (Feature feature: featureContext.getFeatures()) {
            feature.apply(commandContext);
        }

        Map<String, String> replacements = project.getProperties();

        for (Template template: commandContext.getTemplates().values()) {
            String path = replaceVariables(template.getPath(), replacements);
            outputHandler.write(path, template);
        }
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

    private void validateOneOf(List<Feature> featureList) {
        Set<Class<?>> oneOfFeatures = featureList.stream()
                .filter(feature -> feature instanceof OneOfFeature)
                .map(OneOfFeature.class::cast)
                .map(OneOfFeature::getFeatureClass)
                .collect(Collectors.toSet());

        for (Class<?> featureClass: oneOfFeatures) {
            List<String> matches = featureList.stream()
                    .filter(feature -> featureClass.isAssignableFrom(feature.getClass()))
                    .map(Feature::getName)
                    .collect(Collectors.toList());
            if (matches.size() > 1) {
                throw new IllegalArgumentException(String.format("There can only be one of the following features selected: %s", matches));
            }
        }
    }

    public static class CreateAppFeatures extends AvailableFeatures {

        public CreateAppFeatures() {
            super(() -> "create-app");
        }
    }
}
