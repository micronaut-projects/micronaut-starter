package io.micronaut.starter.feature.server

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class ServerSpec extends BeanContextSpec {

    @Unroll
    void 'test gradle server feature #serverFeature'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures([serverFeature])).render().toString()

        then:
        template.contains(dependency)

        where:
        serverFeature     | dependency
        "netty-server"    | 'runtime "netty"'
        "jetty-server"    | 'runtime "jetty"'
        "tomcat-server"   | 'runtime "tomcat"'
        "undertow-server" | 'runtime "undertow"'
    }

    @Unroll
    void 'test maven server feature #serverFeature'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures([serverFeature]), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>$groupId</groupId>
      <artifactId>$artifactId</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        when:
        template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures([serverFeature], Language.KOTLIN), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>$groupId</groupId>
      <artifactId>$artifactId</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        when:
        template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures([serverFeature], Language.GROOVY), []).render().toString()

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
