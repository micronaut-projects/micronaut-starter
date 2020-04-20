package io.micronaut.starter.rocker.plugin;

import groovy.lang.Closure;
import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.tasks.SourceSet;
import org.gradle.util.ConfigureUtil;

/**
 * The rocker property added to the {@link SourceSet}.
 */
public class RockerSourceSetProperty {

    private final TemplateDirectorySet templateDirs;

    /**
     * @param project - main gradle project
     */
    public RockerSourceSetProperty(Project project) {
        super();
        templateDirs = new TemplateDirectorySet(project);
    }

    public TemplateDirectorySet getRocker() {
        return templateDirs;
    }

    public RockerSourceSetProperty rocker(Closure<?> configureClosure) {
        ConfigureUtil.configure(configureClosure, getRocker());
        return this;
    }

    public RockerSourceSetProperty rocker(
        Action<? super TemplateDirectorySet> configureAction) {
        configureAction.execute(getRocker());
        return this;
    }
}
