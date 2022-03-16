package io.micronaut.starter.rocker.plugin;

import com.fizzed.rocker.model.JavaVersion;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.Optional;

/**
 * Bean for the configuration options of Rocker Compiler
 */
public abstract class RockerConfiguration {
    private String[] postProcessing;
    private Boolean markAsGenerated;

    public RockerConfiguration() {
        getSkip().convention(false);
        getFailOnError().convention(true);
        getSkipTouch().convention(true);
        getJavaVersion().convention(JavaVersion.current());
        getOptimize().convention(true);
        getDiscardLogicWhitespace().convention(true);
    }

    @Internal
    public abstract Property<Boolean> getSkip();

    @Internal
    public abstract Property<Boolean> getFailOnError();

    @Internal
    public abstract Property<Boolean> getSkipTouch();

    @Optional
    @Input
    public abstract Property<String> getTouchFile();

    @Input
    public abstract Property<String> getJavaVersion();

    @Optional
    @Input
    public abstract Property<String> getExtendsClass();

    @Optional
    @Input
    public abstract Property<String> getExtendsModelClass();

    @Optional
    @Input
    public abstract Property<Boolean> getOptimize();

    @Optional
    @Input
    public abstract Property<Boolean> getDiscardLogicWhitespace();

    @Optional
    @Input
    public abstract Property<String> getTargetCharset();

    @Optional
    @Input
    public abstract Property<String> getSuffixRegex();

    @Internal
    public abstract DirectoryProperty getOutputBaseDirectory();

    @Internal
    public abstract DirectoryProperty getClassBaseDirectory();

    @Optional
    @Input
    public abstract ListProperty<String> getPostProcessing();

    @Optional
    @Input
    public abstract Property<Boolean> getMarkAsGenerated();
}
