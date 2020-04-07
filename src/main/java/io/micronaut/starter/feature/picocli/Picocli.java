package io.micronaut.starter.feature.picocli;

import io.micronaut.starter.Project;
import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.command.MicronautCommand;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.picocli.test.junit.PicocliJunit;
import io.micronaut.starter.feature.picocli.test.kotlintest.PicocliKotlinTest;
import io.micronaut.starter.feature.picocli.test.spock.PicocliSpock;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;

import javax.inject.Singleton;
import java.util.List;

@Singleton
public class Picocli implements DefaultFeature {

    private final PicocliJunit junit;
    private final PicocliSpock spock;
    private final PicocliKotlinTest kotlinTest;

    public Picocli(PicocliJunit junit, PicocliSpock spock, PicocliKotlinTest kotlinTest) {
        this.junit = junit;
        this.spock = spock;
        this.kotlinTest = kotlinTest;
    }

    @Override
    public boolean shouldApply(MicronautCommand micronautCommand, Language language, List<Feature> selectedFeatures) {
        return micronautCommand == MicronautCommand.CREATE_CLI;
    }

    @Override
    public String getName() {
        return "picocli";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (featureContext.getTestFramework() == TestFramework.junit) {
            featureContext.addFeature(junit);
        } else if (featureContext.getTestFramework() == TestFramework.spock) {
            featureContext.addFeature(spock);
        } else if (featureContext.getTestFramework() == TestFramework.kotlintest) {
            featureContext.addFeature(kotlinTest);
        }
    }

    @Override
    public void apply(CommandContext commandContext) {
        if (commandContext.getBuildTool() == BuildTool.maven) {
            commandContext.getProjectProperties().put("micronaut.picocli.version", "1.2.1");
        }
    }
}
