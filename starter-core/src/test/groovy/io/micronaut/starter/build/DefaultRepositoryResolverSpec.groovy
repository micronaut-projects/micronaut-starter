package io.micronaut.starter.build

import io.micronaut.starter.util.VersionInfo
import spock.lang.Specification
import io.micronaut.starter.application.generator.GeneratorContext;

class DefaultRepositoryResolverSpec extends Specification {

    void "do not generate snapshot repository if not Micronaut Framework"() {
        given:
        DefaultRepositoryResolver resolver = new DefaultRepositoryResolver()
        GeneratorContext generatorContext = Stub(GeneratorContext) {
            getFramework() >> 'Foo'
        }
        expect:
        !resolver.addSnapshotRepository(generatorContext)
    }

    void "do not generate snapshot repository if not Micronaut Framework"() {
        given:
        DefaultRepositoryResolver resolver = new DefaultRepositoryResolver()
        GeneratorContext generatorContext = Stub(GeneratorContext) {
            getFramework() >> 'Micronaut'
        }
        expect:
        VersionInfo.isMicronautSnapshot() == resolver.addSnapshotRepository(generatorContext)
    }

}