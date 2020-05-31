package io.micronaut.starter.feature.multitenancy;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.FeaturePredicate;
import io.micronaut.starter.feature.LanguageSpecificFeature;
import io.micronaut.starter.feature.kotlin.Ktor;
import io.micronaut.starter.options.Language;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class MultitenancyGorm implements Feature, LanguageSpecificFeature {

    private final Multitenancy multitenancy;

    public MultitenancyGorm(Multitenancy multitenancy) {
        this.multitenancy = multitenancy;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (featureContext.getLanguage() == Language.GROOVY) {
            if (!featureContext.isPresent(Multitenancy.class)) {
                featureContext.addFeature(multitenancy);
            }
        } else {
            featureContext.exclude(new FeaturePredicate() {
                @Override
                public boolean test(Feature feature) {
                    return feature instanceof MultitenancyGorm;
                }

                @Override
                public Optional<String> getWarning() {
                    return Optional.of("multi-tenancy-gorm feature only supports Groovy");
                }
            });
        }
    }

    @NonNull
    @Override
    public String getName() {
        return "multi-tenancy-gorm";
    }

    @Override
    public String getTitle() {
        return "Multi-tenancy GORM";
    }

    @Override
    public String getDescription() {
        return "Integrates Micronaut's multi-tenancy capabilities with GORM multi-tenancy features";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://docs.micronaut.io/latest/guide/index.html#multitenancyGorm";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "http://gorm.grails.org/latest/hibernate/manual/index.html#multiTenancy";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public Language getRequiredLanguage() {
        return Language.GROOVY;
    }

    @Override
    public String getCategory() {
        return Category.DATABASE;
    }
}
