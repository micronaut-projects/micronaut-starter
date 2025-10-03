package io.micronaut.starter.feature.build.maven

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.*

class MavenJdkVersionSpec extends BeanContextSpec  implements CommandOutputFixture {

    void 'java 25 populates jdk.version and release.version'() {
        when:
        Options options = new Options(Language.JAVA,
                TestFramework.JUNIT,
                BuildTool.MAVEN,
                JdkVersion.JDK_25)
        Map<String, String> output = generate(ApplicationType.DEFAULT, options, [])

        then:
        output.containsKey('pom.xml')

        when:
        String pom = output.get('pom.xml')

        then:
        pom.contains('<jdk.version>25</jdk.version>')
        pom.contains('<release.version>25</release.version>')
    }
}
