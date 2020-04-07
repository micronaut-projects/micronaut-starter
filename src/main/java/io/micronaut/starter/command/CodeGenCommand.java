package io.micronaut.starter.command;

import io.micronaut.core.async.SupplierUtil;
import io.micronaut.core.beans.BeanIntrospection;
import io.micronaut.core.beans.BeanIntrospector;
import io.micronaut.core.util.functional.ThrowingSupplier;
import io.micronaut.starter.CodeGenConfig;
import io.micronaut.starter.OutputHandler;
import io.micronaut.starter.Project;
import io.micronaut.starter.io.FileSystemOutputHandler;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.template.Template;
import io.micronaut.starter.template.TemplateRenderer;
import io.micronaut.starter.util.NameUtils;
import org.yaml.snakeyaml.Yaml;
import picocli.CommandLine;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

public abstract class CodeGenCommand extends BaseCommand implements Callable<Integer> {

    @CommandLine.Option(names = {"-f", "--force"}, description = "Whether to overwrite existing files")
    boolean overwrite;

    protected final CodeGenConfig config;
    private final ThrowingSupplier<OutputHandler, IOException> outputHandlerSupplier;

    public CodeGenCommand(CodeGenConfig config) {
        this.config = config;
        this.outputHandlerSupplier = () -> new FileSystemOutputHandler(new File(".").getCanonicalFile(), this);
    }

    public abstract boolean applies();

    protected Project getProject(String name, @Nullable CodeGenConfig codeGenConfig) {
        if (name.indexOf('-') > -1) {
            name = NameUtils.getNameFromScript(name);
        }
        if (codeGenConfig != null && codeGenConfig.getDefaultPackage() != null && name.indexOf('.') == -1) {
            return NameUtils.parse(codeGenConfig.getDefaultPackage() + "." + name);
        } else {
            return NameUtils.parse(name);
        }
    }

    protected TemplateRenderer getTemplateRenderer(Project project) throws IOException {
        return TemplateRenderer.create(project, outputHandlerSupplier.get());
    }
}
