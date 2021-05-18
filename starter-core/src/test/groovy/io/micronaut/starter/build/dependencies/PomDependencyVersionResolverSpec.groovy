package io.micronaut.starter.build.dependencies

import io.micronaut.context.ApplicationContext
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

class PomDependencyVersionResolverSpec extends Specification {

    @AutoCleanup
    @Shared
    ApplicationContext applicationContext  = ApplicationContext.run()

    @Shared
    @Subject
    PomDependencyVersionResolver pomDependencyVersionResolver = applicationContext.getBean(PomDependencyVersionResolver)

    void "PomDependencyVersionResolver exposes coordinates map"() {
        expect:
        pomDependencyVersionResolver.coordinates
    }
}
