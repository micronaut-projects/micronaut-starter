package io.micronaut.starter.feature;

import io.micronaut.starter.util.VersionInfo;

public class AnnotationApi implements Feature {

    @Override
    public String getName() {
        return "annotation-api";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (VersionInfo.getJavaVersion() <= 8) {
            featureContext.exclude(feature -> feature == this);
        }
    }
}
