package io.micronaut.starter.feature.other

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.util.VersionInfo

class ReadMeSpec extends BeanContextSpec implements CommandOutputFixture {

    void 'test readme.md contains links to micronaut user guides'() {
        when:
        def output = generate([])
        def readme = output["README.md"]
        def version = VersionInfo.isMicronautSnapshot() ? "snapshot" : VersionInfo.getMicronautVersion()

        then:
        readme
        def title = "## Micronaut ${VersionInfo.getMicronautVersion()} Documentation"
        def guide = "[User Guide](https://docs.micronaut.io/${version}/guide/index.html)"
        def api = "[API Reference](https://docs.micronaut.io/${version}/api/index.html)"
        def config = "[Configuration Reference](https://docs.micronaut.io/${version}/guide/configurationreference.html)"
        def guides = "[Micronaut Guides](https://guides.micronaut.io/index.html)"
        readme.startsWith(title)
        readme.contains(guide)
        readme.contains(api)
        readme.contains(config)
        readme.contains(guides)
    }
}
