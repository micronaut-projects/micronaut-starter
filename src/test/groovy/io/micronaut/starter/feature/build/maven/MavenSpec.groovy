package io.micronaut.starter.feature.build.maven

import io.micronaut.context.BeanContext
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.feature.graalvm.GraalNativeImage
import io.micronaut.starter.feature.jdbc.Dbcp
import io.micronaut.starter.feature.jdbc.Hikari
import io.micronaut.starter.feature.jdbc.Tomcat
import io.micronaut.starter.feature.micrometer.MicrometerFeature
import io.micronaut.starter.feature.server.Jetty
import io.micronaut.starter.feature.server.Netty
import io.micronaut.starter.feature.server.Undertow
import io.micronaut.starter.fixture.FeatureFixture
import io.micronaut.starter.fixture.ProjectFixture
import io.micronaut.starter.options.Language
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class MavenSpec extends Specification implements ProjectFixture, FeatureFixture {

    @Shared
    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    void 'test graal-native-image feature'() {
        when:
        String template = pom.template(buildProject(), buildWithFeatures(Language.java, new GraalNativeImage()), [:]).render().toString()

        then:
        template.contains('<groupId>org.graalvm.nativeimage</groupId>')
        template.contains('<artifactId>svm</artifactId>')
        template.contains('<scope>provided</scope>')
        template.contains('<artifactId>micronaut-graal</artifactId>')

        when:
        template = pom.template(buildProject(), buildWithFeatures(Language.kotlin, new GraalNativeImage()), [:]).render().toString()

        then:
        template.contains('<groupId>org.graalvm.nativeimage</groupId>')
        template.contains('<artifactId>svm</artifactId>')
        template.contains('<scope>provided</scope>')
        template.contains('<artifactId>micronaut-graal</artifactId>')

        when:
        template = pom.template(buildProject(), buildWithFeatures(Language.groovy, new GraalNativeImage()), [:]).render().toString()

        then:
        template.contains('<groupId>org.graalvm.nativeimage</groupId>')
        template.contains('<artifactId>svm</artifactId>')
        template.contains('<scope>provided</scope>')
        template.contains('<artifactId>micronaut-graal</artifactId>')
    }

    @Unroll
    void 'test jdbc feature #jdbcFeature.name'() {
        when:
        String template = pom.template(buildProject(), buildWithFeatures(Language.java, jdbcFeature), [:]).render().toString()

        then:
        template.contains("<artifactId>micronaut-${jdbcFeature.name}</artifactId>")

        when:
        template = pom.template(buildProject(), buildWithFeatures(Language.kotlin, jdbcFeature), [:]).render().toString()

        then:
        template.contains("<artifactId>micronaut-${jdbcFeature.name}</artifactId>")

        when:
        template = pom.template(buildProject(), buildWithFeatures(Language.groovy, jdbcFeature), [:]).render().toString()

        then:
        template.contains("<artifactId>micronaut-${jdbcFeature.name}</artifactId>")

        where:
        jdbcFeature << [new Dbcp(), new Hikari(), new Tomcat()]
    }

    @Unroll
    void 'test micrometer feature #micrometerFeature.name'() {
        given:
        String dependency = "micronaut-micrometer-registry-${micrometerFeature.name - 'micrometer-'}"

        when:
        String template = pom.template(buildProject(), buildWithFeatures(Language.java, micrometerFeature), [:]).render().toString()

        then:
        template.contains("<artifactId>${dependency}</artifactId>")

        when:
        template = pom.template(buildProject(), buildWithFeatures(Language.kotlin, micrometerFeature), [:]).render().toString()

        then:
        template.contains("<artifactId>${dependency}</artifactId>")

        when:
        template = pom.template(buildProject(), buildWithFeatures(Language.groovy, micrometerFeature), [:]).render().toString()

        then:
        template.contains("<artifactId>${dependency}</artifactId>")

        where:
        micrometerFeature << beanContext.getBeansOfType(MicrometerFeature).iterator()
    }

    @Unroll
    void 'test server feature #serverFeature.name'() {
        when:
        String template = pom.template(buildProject(), buildWithFeatures(Language.java, serverFeature), [:]).render().toString()

        then:
        template.contains(dependency)

        when:
        template = pom.template(buildProject(), buildWithFeatures(Language.kotlin, serverFeature), [:]).render().toString()

        then:
        template.contains(dependency)

        when:
        template = pom.template(buildProject(), buildWithFeatures(Language.groovy, serverFeature), [:]).render().toString()

        then:
        template.contains(dependency)

        where:
        serverFeature                                    | dependency
        new Netty()                                      | '<artifactId>micronaut-http-server-netty</artifactId>'
        new Jetty()                                      | '<artifactId>micronaut-http-server-jetty</artifactId>'
        new io.micronaut.starter.feature.server.Tomcat() | '<artifactId>micronaut-http-server-tomcat</artifactId>'
        new Undertow()                                   | '<artifactId>micronaut-http-server-undertow</artifactId>'
    }
}
