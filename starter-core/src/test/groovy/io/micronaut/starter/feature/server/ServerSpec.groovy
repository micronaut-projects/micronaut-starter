package io.micronaut.starter.feature.server

import io.micronaut.context.BeanContext
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.ContextFixture
import io.micronaut.starter.fixture.ProjectFixture
import io.micronaut.starter.options.Language
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class ServerSpec extends Specification implements ProjectFixture, ContextFixture {

    @Shared
    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    @Unroll
    void 'test gradle server feature #serverFeature'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures([serverFeature])).render().toString()

        then:
        template.contains(dependency)

        where:
        serverFeature     | dependency
        "netty-server"    | 'implementation "io.micronaut:micronaut-http-server-netty"'
        "jetty-server"    | 'implementation "io.micronaut.servlet:micronaut-http-server-jetty"'
        "tomcat-server"   | 'implementation "io.micronaut.servlet:micronaut-http-server-tomcat"'
        "undertow-server" | 'implementation "io.micronaut.servlet:micronaut-http-server-undertow"'
    }

    @Unroll
    void 'test maven server feature #serverFeature'() {
        when:
        String template = pom.template(buildProject(), getFeatures([serverFeature]), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>$groupId</groupId>
      <artifactId>$artifactId</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        when:
        template = pom.template(buildProject(), getFeatures([serverFeature], Language.kotlin), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>$groupId</groupId>
      <artifactId>$artifactId</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        when:
        template = pom.template(buildProject(), getFeatures([serverFeature], Language.groovy), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>$groupId</groupId>
      <artifactId>$artifactId</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        serverFeature     | groupId                | artifactId
        "netty-server"    | 'io.micronaut'         | 'micronaut-http-server-netty'
        "jetty-server"    | 'io.micronaut.servlet' | 'micronaut-http-server-jetty'
        "tomcat-server"   | 'io.micronaut.servlet' | 'micronaut-http-server-tomcat'
        "undertow-server" | 'io.micronaut.servlet' | 'micronaut-http-server-undertow'
    }

    void 'test there can only be one server feature'() {
        when:
        getFeatures(["netty-server", "jetty-server", "tomcat-server", "undertow-server"])

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message.contains("There can only be one of the following features selected")
    }

}
