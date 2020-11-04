package io.micronaut.starter.api.options;

import io.micronaut.core.annotation.Creator;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.starter.api.Selectable;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Supported Option
 * @param <T> The underlying option type
 */
@Introspected
@Schema(name = "SupportedOptionInfo")
public class SupportedOptionDTO<T> {

    /**
     * The list of options
     */
    List<T> options;

    /**
     * The Default option
     */
    T defaultOption;

    @Creator
    public SupportedOptionDTO(List<T> options, T defaultOption) {
        this.options = options;
        this.defaultOption = defaultOption;
    }

    @Schema(description = "supported options")
    public List<T> getOptions() {
        return options;
    }

    @Schema(description = "default value")
    public T getDefaultOption() {
        return defaultOption;
    }
}
