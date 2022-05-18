package io.micronaut.starter.feature.server

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class ServerSpec extends ApplicationContextSpec {

    @Unroll
    void 'test gradle server feature #serverFeature'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features( [serverFeature])
                .render()

        then:
        template.contains("runtime(")
        template.contains(dependency)

        where:
        serverFeature     | dependency
        "netty-server"    | 'runtime("netty")'
        "jetty-server"    | 'runtime("jetty")'
        "tomcat-server"   | 'runtime("tomcat")'
        "undertow-server" | 'runtime("undertow")'
    }

    @Unroll
    void 'test maven server feature #serverFeature'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features([serverFeature])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>$groupId</groupId>
      <artifactId>$artifactId</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        when:
        template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(Language.KOTLIN)
                .features([serverFeature])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>$groupId</groupId>
      <artifactId>$artifactId</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        when:
        template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(Language.GROOVY)
                .features([serverFeature])
                .render()

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
