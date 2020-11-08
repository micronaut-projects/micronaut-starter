package io.micronaut.starter.api;

import io.micronaut.core.annotation.Creator;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.starter.api.Selectable;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Supported Option
 * @param <T> The underlying option type
 */
@Introspected
@Schema(name = "SelectOption")
public class SelectOptionDTO<T extends Selectable<?>> {

    /**
     * The list of options
     */
    List<T> options;

    /**
     * The Default option
     */
    T defaultOption;

    @Creator
    public SelectOptionDTO(List<T> options, T defaultOption) {
        this.options = options;
        this.defaultOption = defaultOption;
    }

    @ArraySchema(schema =
        @Schema(description = "the supported options", ref="#/components/schemas/<Any>")
    )
    public List<T> getOptions() {
        return options;
    }

    @Schema(description = "the default value", ref="#/components/schemas/<Any>")
    public T getDefaultOption() {
        return defaultOption;
    }
}
