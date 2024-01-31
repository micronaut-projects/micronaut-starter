package io.micronaut.starter.feature.messaging.jms

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class OracleAdvancedQueuingSpec extends ApplicationContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature jms-oracle-aq contains links to micronaut jms docs'() {
        when:
        Map<String, String> output = generate(['jms-oracle-aq'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-jms/snapshot/guide/index.html")
    }

    void 'test jms-oracle-aq fails when other than oracle database driver was selected'() {
        when:
        new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['jms-oracle-aq', 'mariadb'])
                .render()

        then:
        thrown(IllegalArgumentException)
    }

    void 'test jms-oracle-aq will not fail when oracle database driver was selected'() {
        when:
        new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['jms-oracle-aq', 'oracle'])
                .render()

        then:
        noExceptionThrown()
    }

    void 'test jms-oracle-aq with Gradle'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['jms-oracle-aq'])
                .render()

        then:
        template.contains('implementation("io.micronaut.jms:micronaut-jms-core")')
        template.contains('implementation("javax.transaction:jta:1.1")')
        template.contains('implementation("com.oracle.database.messaging:aqapi:19.3.0.0")')
        template.contains('runtimeOnly("com.oracle.database.jdbc:ojdbc8")')
    }

    void 'test jms-oracle-aq with Maven'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['jms-oracle-aq'])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.jms</groupId>
      <artifactId>micronaut-jms-core</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>javax.transaction</groupId>
      <artifactId>jta</artifactId>
      <version>1.1</version>
      <scope>compile</scope>
    </dependency>
""")

        template.contains("""
    <dependency>
      <groupId>com.oracle.database.messaging</groupId>
      <artifactId>aqapi</artifactId>
      <version>19.3.0.0</version>
      <scope>compile</scope>
    </dependency>
""")

        template.contains("""
    <dependency>
      <groupId>com.oracle.database.jdbc</groupId>
      <artifactId>ojdbc8</artifactId>
      <scope>runtime</scope>
    </dependency>
""")
    }
}
