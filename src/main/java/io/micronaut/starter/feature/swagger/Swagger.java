package io.micronaut.starter.feature.swagger;

import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.options.Language;

public class Swagger implements Feature {

    @Override
    public String getName() {
        return "swagger";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        Language language = featureContext.getLanguage();
        if (language.isJava()) {
            featureContext.addFeature(new SwaggerJava());
        } else if (language.isKotlin()) {
            featureContext.addFeature(new SwaggerKotlin());
        } else if (language.isGroovy()) {
            featureContext.addFeature(new SwaggerGroovy());
        }
    }

}
