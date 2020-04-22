package io.micronaut.starter.cli.feature.server.websocket;

import io.micronaut.context.annotation.Parameter;
import io.micronaut.context.annotation.Prototype;
import io.micronaut.core.util.functional.ThrowingSupplier;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.cli.CodeGenConfig;
import io.micronaut.starter.cli.command.CodeGenCommand;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.server.ServerFeature;
import io.micronaut.starter.io.ConsoleOutput;
import io.micronaut.starter.io.OutputHandler;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.template.RenderResult;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.TemplateRenderer;
import picocli.CommandLine;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

@CommandLine.Command(name = "create-websocket-server", description = "Creates a Websocket server")
@Prototype
public class CreateWebsocketServerCommand extends CodeGenCommand {

    private final List<Feature> features;
    @CommandLine.Parameters(paramLabel = "SERVER-NAME", description = "The name of the server to create")
    String serverName;

    @Inject
    public CreateWebsocketServerCommand(@Parameter CodeGenConfig config, List<Feature> features) {
        super(config);
        this.features = features;
    }

    public CreateWebsocketServerCommand(CodeGenConfig config,
                                        ThrowingSupplier<OutputHandler, IOException> outputHandlerSupplier,
                                        ConsoleOutput consoleOutput,
                                        List<Feature> features) {
        super(config, outputHandlerSupplier, consoleOutput);
        this.features = features;
    }

    @Override
    public boolean applies() {
        List<String> selectedFeatures = config.getFeatures();
        return features.stream()
                .filter(ServerFeature.class::isInstance)
                .map(Feature::getName)
                .anyMatch(selectedFeatures::contains);
    }

    @Override
    public Integer call() throws Exception {
        Project project = getProject(serverName);

        TemplateRenderer templateRenderer = getTemplateRenderer(project);

        RenderResult renderResult = null;
        if (config.getSourceLanguage() == Language.JAVA) {
            renderResult = templateRenderer.render(new RockerTemplate("src/main/java/{packagePath}/{className}Server.java", javaWebsocketServer.template(project)), overwrite);
        } else if (config.getSourceLanguage() == Language.GROOVY) {
            renderResult = templateRenderer.render(new RockerTemplate("src/main/groovy/{packagePath}/{className}Server.groovy", groovyWebsocketServer.template(project)), overwrite);
        } else if (config.getSourceLanguage() == Language.KOTLIN) {
            renderResult = templateRenderer.render(new RockerTemplate("src/main/kotlin/{packagePath}/{className}Server.kt", kotlinWebsocketServer.template(project)), overwrite);
        }

        if (renderResult != null) {
            if (renderResult.isSuccess()) {
                out("@|blue ||@ Rendered websocket server to " + renderResult.getPath());
            } else if (renderResult.isSkipped()) {
                warning("Rendering skipped for " + renderResult.getPath() + " because it already exists. Run again with -f to overwrite.");
            } else if (renderResult.getError() != null) {
                throw renderResult.getError();
            }
        }

        return 0;
    }
}