package io.micronaut.starter.openrewrite;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.sdk.BuildTool;
import io.micronaut.starter.sdk.dependency.Dependency;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

public interface RecipeFetcher {
    Optional<String> findMicronautDocumentationByRecipeName(String recipeName);

    Optional<String> findThirdPartyDocumentationByRecipeName(String recipeName);

    @NonNull
    List<Dependency> findAllByRecipeNameAndBuildTool(@NonNull String recipe, @NonNull BuildTool buildTool);

    @NonNull
    Optional<Properties> findPropertiesByRecipeName(@NonNull String recipe);


}
