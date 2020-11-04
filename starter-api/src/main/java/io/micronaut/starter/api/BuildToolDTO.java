package io.micronaut.starter.api;

import io.micronaut.core.annotation.Creator;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.starter.options.BuildTool;
import io.swagger.v3.oas.annotations.media.Schema;


/**
 * DTO objects for {@link BuildTool}.
 *
 * @since 2.2.0
 */
@Introspected
@Schema(name = "BuildToolInfo")
public class BuildToolDTO implements Selectable<String>{
    String label;
    String description;
    String value;

    public BuildToolDTO(BuildTool buildTool) {
        this.label = buildTool.name();
        this.value = buildTool.name();
        this.description = buildTool.name();
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
