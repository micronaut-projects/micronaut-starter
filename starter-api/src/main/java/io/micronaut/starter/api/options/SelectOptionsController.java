package io.micronaut.starter.api.options;

import io.micronaut.context.MessageSource;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.starter.api.RequestInfo;
import io.micronaut.starter.api.SelectOptionsDTO;

import javax.inject.Inject;


/**
 * Gets Information about select options for the starter.
 *
 * @since 2.2.0
 */
@Controller("/select-options")
public class SelectOptionsController implements SelectOptionsOperations {

    @Inject
    MessageSource messageSource;

    /**
     * Gets select options for the starter
     * @return Select Options and their defaults.
     */
    @Override
    @Get(uri = "/", produces = MediaType.APPLICATION_JSON)
    public SelectOptionsDTO selectOptions(RequestInfo requestInfo) {
        MessageSource.MessageContext context = MessageSource.MessageContext.of(requestInfo.getLocale());
        return SelectOptionsDTO.make(messageSource, context);
    }
}
