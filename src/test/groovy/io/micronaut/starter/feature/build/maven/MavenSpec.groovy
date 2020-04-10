package io.micronaut.starter.feature.build.maven

import io.micronaut.context.BeanContext
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.ContextFixture
import io.micronaut.starter.fixture.ProjectFixture
import io.micronaut.starter.options.Language
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class MavenSpec extends Specification implements ProjectFixture, ContextFixture {

    @Shared
    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    void 'test graal-native-image feature'() {
        when:
        String template = pom.template(buildProject(), getFeatures(["graal-native-image"]), []).render().toString()

        then:
        template.contains('<groupId>org.graalvm.nativeimage</groupId>')
        template.contains('<artifactId>svm</artifactId>')
        template.contains('<scope>provided</scope>')
        template.contains('<artifactId>micronaut-graal</artifactId>')

        when:
        template = pom.template(buildProject(), getFeatures(["graal-native-image"], Language.kotlin), []).render().toString()

        then:
        template.contains('<groupId>org.graalvm.nativeimage</groupId>')
        template.contains('<artifactId>svm</artifactId>')
        template.contains('<scope>provided</scope>')
        template.contains('<artifactId>micronaut-graal</artifactId>')

        when:
        template = pom.template(buildProject(), getFeatures(["graal-native-image"], Language.groovy), []).render().toString()

        then:
        template.contains('<groupId>org.graalvm.nativeimage</groupId>')
        template.contains('<artifactId>svm</artifactId>')
        template.contains('<scope>provided</scope>')
        template.contains('<artifactId>micronaut-graal</artifactId>')
    }

    @Unroll
    void 'test jdbc feature #jdbcFeature.name'() {
        when:
        String template = pom.template(buildProject(), getFeatures([jdbcFeature]), []).render().toString()

        then:
        template.contains("<artifactId>micronaut-${jdbcFeature}</artifactId>")

        when:
        template = pom.template(buildProject(), getFeatures([jdbcFeature], Language.kotlin), []).render().toString()

        then:
        template.contains("<artifactId>micronaut-${jdbcFeature}</artifactId>")

        when:
        template = pom.template(buildProject(), getFeatures([jdbcFeature], Language.groovy), []).render().toString()

        then:
        template.contains("<artifactId>micronaut-${jdbcFeature}</artifactId>")

        where:
        jdbcFeature << ["jdbc-dbcp", "jdbc-hikari", "jdbc-tomcat"]
    }

    @Unroll
    void 'test server feature #serverFeature.name'() {
        when:
        String template = pom.template(buildProject(), getFeatures([serverFeature]), []).render().toString()

        then:
        template.contains(dependency)

        when:
        template = pom.template(buildProject(), getFeatures([serverFeature], Language.kotlin), []).render().toString()

        then:
        template.contains(dependency)

        when:
        template = pom.template(buildProject(), getFeatures([serverFeature], Language.groovy), []).render().toString()

        then:
        template.contains(dependency)

        where:
        serverFeature          | dependency
        "netty-server"         | '<artifactId>micronaut-http-server-netty</artifactId>'
        "jetty-server"         | '<artifactId>micronaut-http-server-jetty</artifactId>'
        "tomcat-server"        | '<artifactId>micronaut-http-server-tomcat</artifactId>'
        "undertow-server"      | '<artifactId>micronaut-http-server-undertow</artifactId>'
    }
}
