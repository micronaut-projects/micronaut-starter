package io.micronaut.starter.feature.test;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;

import javax.inject.Singleton;

@Singleton
public class Hamcrest  implements Feature {

    @Override
    public String getName() {
        return "hamcrest";
    }

    @Override
    public String getTitle() {
        return "Java Hamcrest";
    }

    @Override
    public String getDescription() {
        return "Hamcrest matchers for JUnit";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.DEV_TOOLS;
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "http://hamcrest.org/JavaHamcrest/";
    }
}
