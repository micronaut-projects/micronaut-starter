package io.micronaut.starter.api.options;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;


/**
 * Gets Information about select options for the starter.
 *
 * @since 2.2.0
 */
@Controller("/select-options")
public class SelectOptionsController implements SelectOptionsOperations {

    /**
     * Gets select options for the starter
     * @return Select Options and their defaults.
     */
    @Override
    @Get(uri = "/", produces = MediaType.APPLICATION_JSON)
    public SelectOptionsDTO selectOptions() {
        return SelectOptionsDTO.make();
    }
}
