package io.micronaut.starter.api;

import io.micronaut.context.MessageSource;
import io.micronaut.core.annotation.Creator;
import io.micronaut.core.annotation.Internal;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.util.NameUtils;
import io.swagger.v3.oas.annotations.media.Schema;


/**
 * DTO objects for {@link BuildTool}.
 *
 * @since 2.2.0
 */
@Introspected
@Schema(name = "BuildToolInfo")
public class BuildToolDTO implements Selectable<String>{
    static final String MESSAGE_PREFIX = StarterConfiguration.PREFIX + ".buildTools.";
    String label;
    String description;
    String value;

    public BuildToolDTO(BuildTool buildTool) {
        this.label = buildTool.name();
        this.value = buildTool.name();
        this.description = buildTool.name();
    }

    @Internal
    public BuildToolDTO(BuildTool buildTool, MessageSource messageSource, MessageSource.MessageContext messageContext) {
        this.value = buildTool.name();
        String label = NameUtils.getNaturalNameOfEnum(buildTool.name());
        this.label = messageSource.getMessage(MESSAGE_PREFIX + this.value + ".label", messageContext, label);
        this.description = messageSource.getMessage(MESSAGE_PREFIX + this.value + ".description", messageContext, label);
    }

    @Creator
    public BuildToolDTO(String label, String description, String value) {
        this.label = label;
        this.description = description;
        this.value = value;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getValue() {
        return value;
    }
}
