package io.micronaut.starter.feature.build.maven

import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.feature.graalvm.GraalNativeImage
import io.micronaut.starter.feature.jdbc.Dbcp
import io.micronaut.starter.feature.jdbc.Hikari
import io.micronaut.starter.feature.jdbc.Tomcat
import io.micronaut.starter.fixture.FeatureFixture
import io.micronaut.starter.fixture.ProjectFixture
import io.micronaut.starter.options.Language
import spock.lang.Specification
import spock.lang.Unroll

class MavenSpec extends Specification implements ProjectFixture, FeatureFixture {

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
}
