package io.micronaut.starter.cli.feature.server.websocket;

import io.micronaut.context.annotation.Parameter;
import io.micronaut.context.annotation.Prototype;
import io.micronaut.core.util.functional.ThrowingSupplier;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.cli.CodeGenConfig;
import io.micronaut.starter.cli.command.CodeGenCommand;
import io.micronaut.starter.cli.feature.other.groovyClient;
import io.micronaut.starter.cli.feature.other.javaClient;
import io.micronaut.starter.cli.feature.other.kotlinClient;
import io.micronaut.starter.io.ConsoleOutput;
import io.micronaut.starter.io.OutputHandler;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.template.RenderResult;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.TemplateRenderer;
import picocli.CommandLine;

import javax.inject.Inject;
import java.io.IOException;

@CommandLine.Command(name = "create-websocket-client", description = "Creates a Websocket client")
@Prototype
public class CreateWebsocketClientCommand extends CodeGenCommand {

    @CommandLine.Parameters(paramLabel = "CLIENT-NAME", description = "The name of the client to create")
    String clientName;

    @Inject
    public CreateWebsocketClientCommand(@Parameter CodeGenConfig config) {
        super(config);
    }

    public CreateWebsocketClientCommand(CodeGenConfig config, ThrowingSupplier<OutputHandler, IOException> outputHandlerSupplier, ConsoleOutput consoleOutput) {
        super(config, outputHandlerSupplier, consoleOutput);
    }

    @Override
    public boolean applies() {
        return config.getFeatures().contains("http-client");
    }


    @Override
    public Integer call() throws Exception {
        Project project = getProject(clientName);

        TemplateRenderer templateRenderer = getTemplateRenderer(project);

        RenderResult renderResult = null;
        if (config.getSourceLanguage() == Language.JAVA) {
            renderResult = templateRenderer.render(new RockerTemplate("src/main/java/{packagePath}/{className}Client.java", javaWebsocketClient.template(project)), overwrite);
        } else if (config.getSourceLanguage() == Language.GROOVY) {
            renderResult = templateRenderer.render(new RockerTemplate("src/main/groovy/{packagePath}/{className}Client.groovy", groovyWebsocketClient.template(project)), overwrite);
        } else if (config.getSourceLanguage() == Language.KOTLIN) {
            renderResult = templateRenderer.render(new RockerTemplate("src/main/kotlin/{packagePath}/{className}Client.kt", kotlinWebsocketClient.template(project)), overwrite);
        }

        if (renderResult != null) {
            if (renderResult.isSuccess()) {
                out("@|blue ||@ Rendered websocket client to " + renderResult.getPath());
            } else if (renderResult.isSkipped()) {
                warning("Rendering skipped for " + renderResult.getPath() + " because it already exists. Run again with -f to overwrite.");
            } else if (renderResult.getError() != null) {
                throw renderResult.getError();
            }
        }

        return 0;
    }
}
