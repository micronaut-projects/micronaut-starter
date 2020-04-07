package io.micronaut.starter.command;

import io.micronaut.context.annotation.Prototype;
import io.micronaut.starter.feature.AvailableFeatures;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.validation.FeatureValidator;
import picocli.CommandLine;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CommandLine.Command(name = CreateAppCommand.NAME, description = "Creates an application")
@Prototype
public class CreateAppCommand extends CreateCommand {

    public static final String NAME = "create-app";

    @CommandLine.Option(names = {"-f", "--features"}, paramLabel = "FEATURE", split = ",", description = "The features to use. Possible values: ${COMPLETION-CANDIDATES}", completionCandidates = CreateAppFeatures.class)
    List<String> features = new ArrayList<>();

    public CreateAppCommand(CreateAppFeatures createAppFeatures,
                            FeatureValidator featureValidator) {
        super(createAppFeatures, featureValidator, MicronautCommand.CREATE_APP);
    }

    @Override
    protected List<String> getSelectedFeatures() {
        return features;
    }

    @Singleton
    public static class CreateAppFeatures extends AvailableFeatures {

        public CreateAppFeatures(List<Feature> features) {
            super(features.stream()
                    .filter(f -> f.supports(MicronautCommand.CREATE_APP))
                    .collect(Collectors.toList()));
        }
    }
}
