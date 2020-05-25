package io.micronaut.starter.feature.database.jdbc

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.fixture.CommandOutputFixture

class TomcatSpec extends BeanContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature jdbc-tomcat contains links to micronaut docs'() {
        when:
        def output = generate(['jdbc-tomcat'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-sql/latest/guide/index.html#jdbc")
    }

}
