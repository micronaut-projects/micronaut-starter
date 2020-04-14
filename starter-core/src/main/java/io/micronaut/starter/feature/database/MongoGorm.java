package io.micronaut.starter.feature.database;

import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.options.Language;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class MongoGorm implements Feature {

    private final MongoReactive mongoReactive;

    public MongoGorm(MongoReactive mongoReactive) {
        this.mongoReactive = mongoReactive;
    }

    @Override
    public String getName() {
        return "mongo-gorm";
    }

    @Override
    public String getDescription() {
        return "Configures GORM for MongoDB for Groovy applications";
    }

    @Override
    public Optional<Language> getRequiredLanguage() {
        return Optional.of(Language.groovy);
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(MongoReactive.class)) {
            featureContext.addFeature(mongoReactive);
        }
    }
}
