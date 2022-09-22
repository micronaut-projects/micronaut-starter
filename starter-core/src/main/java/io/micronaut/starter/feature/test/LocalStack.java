package io.micronaut.starter.feature.test;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.database.TestContainers;
import jakarta.inject.Singleton;

import static io.micronaut.starter.feature.database.TestContainers.TESTCONTAINERS_GROUP_ID;

/**
 * Adds support for <a href="https://localstack.cloud/">LocalStack</a>.
 *
 * @author Álvaro Sánchez-Mariscal
 * @since 3.7.1
 */
@Singleton
public class LocalStack implements Feature {

    private final TestContainers testContainers;

    public LocalStack(TestContainers testContainers) {
        this.testContainers = testContainers;
    }

    @Override
    @NonNull
    public String getName() {
        return "localstack";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getTitle() {
        return "LocalStack";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "A fully functional local cloud stack to develop and test your cloud and serverless apps offline, integrated via Testcontainers";
    }

    @Override
    public String getCategory() {
        return Category.DEV_TOOLS;
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://www.testcontainers.org/modules/localstack/";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(TestContainers.class)) {
            featureContext.addFeature(testContainers);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId(TESTCONTAINERS_GROUP_ID)
                .artifactId("localstack")
                .test());
        generatorContext.addDependency(Dependency.builder()
                .groupId("com.amazonaws")
                .artifactId("aws-java-sdk-core")
                .test());
    }
}
