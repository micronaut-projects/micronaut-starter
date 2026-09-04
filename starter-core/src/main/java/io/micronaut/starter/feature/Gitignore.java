package io.micronaut.starter.feature;

import com.fizzed.rocker.RockerModel;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.rocker.feature.build.gitignore;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.Template;
import jakarta.inject.Singleton;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Set;

@Singleton
public class Gitignore implements DefaultFeature {
    @Override
    public @NonNull String getName() {
        return "gitignore";
    }

    @Override
    public @Nullable String getDescription() {
        return "generates a .gitignore file";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addGitIgnore(generatorContext);
    }

    private void addGitIgnore(GeneratorContext generatorContext) {
        generatorContext.addTemplate("gitignore", new RockerTemplate(Template.ROOT, ".gitignore", gitIgnore(generatorContext)));
    }

    @SuppressWarnings("java:S1172") // Unused parameter for extension
    protected RockerModel gitIgnore(GeneratorContext generatorContext) {
        return gitignore.template(generatorContext.getFeatures(), generatorContext.getLanguage().equals(Language.PYTHON));
    }

    @Override
    public boolean isVisible() {
        return false;
    }



    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
        return true;
    }
}
