package io.micronaut.starter.command;


import io.micronaut.context.annotation.Prototype;
import io.micronaut.starter.feature.*;
import io.micronaut.starter.feature.validation.FeatureValidator;
import picocli.CommandLine;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CommandLine.Command(name = CreateCliCommand.NAME, description = "Creates a CLI application")
@Prototype
public class CreateCliCommand extends CreateCommand {

    public static final String NAME = "create-cli-app";

    @CommandLine.Option(names = {"-f", "--features"}, paramLabel = "FEATURE", split = ",", description = "The features to use. Possible values: ${COMPLETION-CANDIDATES}", completionCandidates = CreateCliFeatures.class)
    List<String> features = new ArrayList<>();

    public CreateCliCommand(CreateCliFeatures createCliFeatures, FeatureValidator featureValidator) {
        super(createCliFeatures, featureValidator, MicronautCommand.CREATE_CLI);
    }

    @Override
    protected List<String> getSelectedFeatures() {
        return features;
    }

    @Singleton
    public static class CreateCliFeatures extends AvailableFeatures {

        public CreateCliFeatures(List<Feature> features) {
            super(features.stream()
                    .filter(f -> f.supports(MicronautCommand.CREATE_CLI))
                    .collect(Collectors.toList()));
        }

    }
}