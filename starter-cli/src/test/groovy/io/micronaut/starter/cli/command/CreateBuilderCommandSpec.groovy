package io.micronaut.starter.cli.command

import io.micronaut.starter.feature.function.CloudProvider
import spock.lang.Specification

class CreateBuilderCommandSpec extends Specification {

    void "all cloud providers are in the list shown to the user"() {
        given:
        Set<CloudProvider> selection = CreateBuilderCommand.CloudProviderSelection.values()*.provider.findAll()
        Set<CloudProvider> expected = CloudProvider.values()

        expect:
        selection == expected
    }
}
