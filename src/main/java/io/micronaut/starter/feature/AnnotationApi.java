package io.micronaut.starter.feature;

import io.micronaut.starter.command.MicronautCommand;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.util.VersionInfo;

import javax.inject.Singleton;
import java.util.List;

@Singleton
public class AnnotationApi implements Feature, DefaultFeature {

    @Override
    public String getName() {
        return "annotation-api";
    }

    @Override
    public boolean shouldApply(MicronautCommand micronautCommand, Language language, List<Feature> selectedFeatures) {
        return VersionInfo.getJavaVersion() >= 9;
    }
}
