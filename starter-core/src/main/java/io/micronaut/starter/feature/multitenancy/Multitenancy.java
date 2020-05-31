package io.micronaut.starter.feature.multitenancy;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import javax.inject.Singleton;

@Singleton
public class Multitenancy implements Feature {

    @NonNull
    @Override
    public String getName() {
        return "multi-tenancy";
    }

    @Override
    public String getTitle() {
        return "Multi-tenancy";
    }

    @NonNull
    @Override
    public String getDescription() {
        return "Adds multi-tenancy capabilities to your app. Tenant resolution, tenant propagation";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.DATABASE;
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://docs.micronaut.io/latest/guide/index.html#multitenancy";
    }
}
