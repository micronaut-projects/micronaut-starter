package io.micronaut.starter.command;

import com.fizzed.rocker.Rocker;
import io.micronaut.starter.BaseCommand;
import io.micronaut.starter.OutputHandler;
import io.micronaut.starter.Project;
import io.micronaut.starter.feature.*;
import io.micronaut.starter.io.FileSystemOutputHandler;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.template.Template;
import io.micronaut.starter.util.NameUtils;
import picocli.CommandLine;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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

        List<Feature> features = new ArrayList<>(8);
        for (String name: this.features) {
            Feature feature = CreateAppFeatures.get(name);
            if (feature != null) {
                features.add(feature);
            } else {
                err("The requested feature does not exist: " + name);
                return 1;
            }
        }

        features.add(new AnnotationApi());

        FeatureContext featureContext = new FeatureContext(lang, test, build);

        featureContext.processSelectedFeatures(features);

        features = featureContext.getFeatures();

        Set<Class<?>> oneOfFeatures = features.stream()
                .filter(feature -> feature instanceof OneOfFeature)
                .map(OneOfFeature.class::cast)
                .map(OneOfFeature::getFeatureClass)
                .collect(Collectors.toSet());

        for (Class<?> featureClass: oneOfFeatures) {
            List<String> matches = features.stream()
                    .filter(feature -> featureClass.isAssignableFrom(feature.getClass()))
                    .map(Feature::getName)
                    .collect(Collectors.toList());
            if (matches.size() > 1) {
                err(String.format("There can only be one of the following features selected: %s", matches));
                return 1;
            }
        }

        CommandContext commandContext = new CommandContext(featureContext, project);

        for (Feature feature: featureContext.getFeatures()) {
            feature.apply(commandContext);
        }

        OutputHandler outputHandler = new FileSystemOutputHandler(project, inplace, this);

        Map<String, String> replacements = project.getProperties();

        for (Template template: commandContext.getTemplates().values()) {
            String path = replaceVariables(template.getPath(), replacements);
            outputHandler.write(path, template);
        }

        out("created " + name);
        return 0;
    }

    String replaceVariables(String path, Map<String, String> replacements) {
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

    public static class CreateAppFeatures extends AllFeatures {

        private static Predicate<Feature> SUPPORTS = (feature) -> feature.supports(CreateAppCommand.NAME);

        @Override
        protected Stream<Feature> apply(Stream<Feature> stream) {
            return doApply(stream);
        }

        private static Stream<Feature> doApply(Stream<Feature> stream) {
            return stream.filter(SUPPORTS);
        }

        public static Feature get(String name) {
            Feature feature = AllFeatures.get(name);
            if (feature != null && SUPPORTS.test(feature)) {
                return feature;
            } else {
                return null;
            }
        }

        public static Stream<Feature> stream() {
            return doApply(AllFeatures.list().stream());
        }
    }
}
