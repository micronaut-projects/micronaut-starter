package io.micronaut.starter.rocker.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.internal.plugins.DslObject;
import org.gradle.api.plugins.JavaLibraryPlugin;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskProvider;

/**
 * A plugin that for gradle that provides the `generateRockerTemplateSource`
 * tasks.
 */
public class RockerPlugin implements Plugin<Project> {
    /**
     * Create `rockerCompile` task in group build and describe the task
     *
     * @param project - gradle project
     */
    @Override
    public void apply(Project project) {
        // Make sure that we have the objects from the java task available
        project.getPluginManager().apply(JavaLibraryPlugin.class);

        // Create own project extension (configuration)
        RockerConfiguration rockerConfig = project.getExtensions().create("rocker", RockerConfiguration.class);
        rockerConfig.getOutputBaseDirectory().convention(
                project.getLayout().getBuildDirectory().dir("generated-src/rocker")
        );
        rockerConfig.getClassBaseDirectory().convention(
                project.getLayout().getBuildDirectory().dir("classes")
        );

        // Create own source set extension
        SourceSetContainer sourceSets = project.getConvention().getPlugin(
                JavaPluginConvention.class).getSourceSets();
        sourceSets.all(sourceSet -> processSourceSet(project, sourceSet, rockerConfig));

    }

    private static void processSourceSet(Project project, SourceSet sourceSet,
                                         RockerConfiguration rockerConfig) {
        // for each source set we will:
        // 1) Add a new 'rocker' property to the source set
        RockerSourceSetProperty rockerProperty
                = new RockerSourceSetProperty(project);
        new DslObject(sourceSet).getConvention().getPlugins().put(
                "rocker", rockerProperty);

        // 2) Create a rocker task for this sourceSet following the gradle
        //    naming conventions
        String taskName = sourceSet.getTaskName(
                "generate", "RockerTemplateSource");
        TaskProvider<RockerTask> rockerTaskProvider = project.getTasks().register(taskName, RockerTask.class, rockerTask -> {
            rockerTask.setGroup("build");
            rockerTask.setDescription("Generate Sources from "
                    + sourceSet.getName() + " Rocker Templates");
            rockerTask.getRockerProjectConfig().set(rockerConfig);
            rockerTask.getOutputDir().convention(
                    rockerConfig.getOutputBaseDirectory().dir(sourceSet.getName())
            );
            rockerTask.getClassDir().convention(
                    rockerConfig.getClassBaseDirectory().dir(sourceSet.getName())
            );
            rockerTask.getTemplateDirs().from(rockerProperty.getRocker().getSrcDirs());
        });

        sourceSet.getJava().srcDir(rockerTaskProvider);
    }

}
