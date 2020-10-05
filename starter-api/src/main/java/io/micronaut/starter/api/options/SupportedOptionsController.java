package io.micronaut.starter.api.options;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;


/**
 * Gets Information about supported build options.
 *
 * @author eahrold
 * @since 1.0.0
 */
@Controller("/supported-options")
public class SupportedOptionsController {


    /**
     * Gets supported build options
     * @return Supported options and their defaults.
     */
    @Get
    HttpResponse<SupportedOptionsDTO>supportedOptions() {
        return HttpResponse.ok(SupportedOptionsDTO.make());
    }

}
