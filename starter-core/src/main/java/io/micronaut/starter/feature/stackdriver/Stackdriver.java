package io.micronaut.starter.feature.stackdriver;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;

import javax.inject.Singleton;

@Singleton
public class Stackdriver implements Feature {
    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @NonNull
    @Override
    public String getName() {
        return "stackdriver";
    }

    @Override
    public String getTitle() {
        return "stackdriver";
    }

    @Override
    public String getDescription() {
        return "Integrates Micronaut with Stackdriver Trace.";
    }

    @Override
    public String getCategory() {
        return Category.LOGGING;
    }
}
