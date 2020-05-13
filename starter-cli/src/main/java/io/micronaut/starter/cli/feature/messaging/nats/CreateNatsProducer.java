package io.micronaut.starter.cli.feature.messaging.nats;

import io.micronaut.context.annotation.Parameter;
import io.micronaut.context.annotation.Prototype;
import io.micronaut.core.annotation.ReflectiveAccess;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.cli.CodeGenConfig;
import io.micronaut.starter.cli.command.CodeGenCommand;
import io.micronaut.starter.cli.feature.messaging.nats.template.producer.groovyProducer;
import io.micronaut.starter.cli.feature.messaging.nats.template.producer.javaProducer;
import io.micronaut.starter.cli.feature.messaging.nats.template.producer.kotlinProducer;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.template.RenderResult;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.TemplateRenderer;
import picocli.CommandLine;

/**
 * @author jgrimm
 */
@CommandLine.Command(name = "create-nats-producer", description = "Creates a producer class for Nats.io")
@Prototype
public class CreateNatsProducer extends CodeGenCommand {

    @ReflectiveAccess
    @CommandLine.Parameters(paramLabel = "PRODUCER", description = "The name of the producer to create")
    String producerName;

    public CreateNatsProducer(@Parameter CodeGenConfig config) {
        super(config);
    }

    @Override
    public boolean applies() {
        return config.getFeatures().contains("nats");
    }

    @Override
    public Integer call() throws Exception {
        Project project = getProject(producerName);

        TemplateRenderer templateRenderer = getTemplateRenderer(project);

        RenderResult renderResult = null;
        if (config.getSourceLanguage() == Language.JAVA) {
            renderResult = templateRenderer.render(new RockerTemplate("src/main/java/{packagePath}/{className}.java", javaProducer.template(project)), overwrite);
        } else if (config.getSourceLanguage() == Language.GROOVY) {
            renderResult = templateRenderer.render(new RockerTemplate("src/main/groovy/{packagePath}/{className}.groovy", groovyProducer.template(project)), overwrite);
        } else if (config.getSourceLanguage() == Language.KOTLIN) {
            renderResult = templateRenderer.render(new RockerTemplate("src/main/kotlin/{packagePath}/{className}.kt", kotlinProducer.template(project)), overwrite);
        }

        if (renderResult != null) {
            if (renderResult.isSuccess()) {
                out("@|blue ||@ Rendered Nats producer to " + renderResult.getPath());
            } else if (renderResult.isSkipped()) {
                warning("Rendering skipped for " + renderResult.getPath() + " because it already exists. Run again with -f to overwrite.");
            } else if (renderResult.getError() != null) {
                throw renderResult.getError();
            }
        }

        return 0;
    }
}
