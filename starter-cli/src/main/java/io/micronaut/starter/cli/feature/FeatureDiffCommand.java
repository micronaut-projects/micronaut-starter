package io.micronaut.starter.cli.feature;

import com.github.difflib.DiffUtils;
import com.github.difflib.UnifiedDiffUtils;
import com.github.difflib.patch.Patch;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.core.annotation.ReflectiveAccess;
import io.micronaut.core.util.functional.ThrowingSupplier;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.ProjectGenerator;
import io.micronaut.starter.cli.CodeGenConfig;
import io.micronaut.starter.cli.command.CodeGenCommand;
import io.micronaut.starter.io.ConsoleOutput;
import io.micronaut.starter.io.MapOutputHandler;
import io.micronaut.starter.io.OutputHandler;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.util.NameUtils;
import picocli.CommandLine;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.*;

@CommandLine.Command(name = "feature-diff", description = "Produces the diff of an original project with an original project with additional features.")
public class FeatureDiffCommand extends CodeGenCommand {

    private final ProjectGenerator projectGenerator;

    @ReflectiveAccess
    @CommandLine.Option(names = {"--features"}, paramLabel = "FEATURE", split = ",", description = "The additional features")
    List<String> features = new ArrayList<>();

    @Inject
    public FeatureDiffCommand(@Parameter CodeGenConfig config, ProjectGenerator projectGenerator) {
        super(config);
        this.projectGenerator = projectGenerator;
    }

    public FeatureDiffCommand(CodeGenConfig config,
                              ThrowingSupplier<OutputHandler, IOException> outputHandlerSupplier,
                              ConsoleOutput consoleOutput,
                              ProjectGenerator projectGenerator) {
        super(config, outputHandlerSupplier, consoleOutput);
        this.projectGenerator = projectGenerator;
    }

    @Override
    public boolean applies() {
        return true;
    }

    @Override
    public Integer call() throws Exception {
        String appName = new File(".").getCanonicalFile().getName();
        Project project = NameUtils.parse(config.getDefaultPackage() + "." + appName);
        Options options = new Options(config.getSourceLanguage(), config.getTestFramework(), config.getBuildTool());
        MapOutputHandler outputHandler = new MapOutputHandler();
        projectGenerator.generate(config.getApplicationType(), project, options, Collections.emptyList(), outputHandler, ConsoleOutput.NOOP);
        Map<String, String> oldProject = outputHandler.getProject();

        outputHandler = new MapOutputHandler();
        projectGenerator.generate(config.getApplicationType(), project, options, features, outputHandler, ConsoleOutput.NOOP);
        Map<String, String> newProject = outputHandler.getProject();

        for (Map.Entry<String, String> entry: newProject.entrySet()) {
            String oldFile = oldProject.remove(entry.getKey());

            if (entry.getValue() == null) {
                continue;
            }

            List<String> oldFileLines = oldFile == null ? Collections.emptyList() : toLines(oldFile);

            String newFile = entry.getValue();
            List<String> newFileLines = toLines(newFile);

            Patch<String> diff = DiffUtils.diff(oldFileLines, newFileLines);
            List<String> unifiedDiff = UnifiedDiffUtils.generateUnifiedDiff(entry.getKey(), entry.getKey(), oldFileLines, diff, 3);

            if (!unifiedDiff.isEmpty()) {
                for (String delta : unifiedDiff) {
                    out(delta);
                }
                out("\n");
            }
        }

        for (Map.Entry<String, String> entry: oldProject.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            List<String> oldFileLines = toLines(entry.getValue());
            Patch<String> diff = DiffUtils.diff(oldFileLines, Collections.emptyList());
            List<String> unifiedDiff = UnifiedDiffUtils.generateUnifiedDiff(entry.getKey(), entry.getKey(), oldFileLines, diff, 3);

            if (!unifiedDiff.isEmpty()) {
                for (String delta : unifiedDiff) {
                    out(delta);
                }
                out("\n");
            }
        }

        return 0;
    }

    private List<String> toLines(String file) {
        return Arrays.asList(file.split("\n"));
    }
}
