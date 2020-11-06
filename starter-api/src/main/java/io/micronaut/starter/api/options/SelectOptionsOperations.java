package io.micronaut.starter.api.options;

import io.micronaut.starter.api.RequestInfo;
import io.micronaut.starter.api.SelectOptionsDTO;
import io.swagger.v3.oas.annotations.Parameter;

public interface SelectOptionsOperations {
    SelectOptionsDTO selectOptions(@Parameter(hidden = true) RequestInfo serverURL);
}
