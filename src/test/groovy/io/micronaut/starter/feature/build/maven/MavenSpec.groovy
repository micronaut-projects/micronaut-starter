package io.micronaut.starter.feature.build.maven

import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.feature.graalvm.GraalNativeImage
import io.micronaut.starter.fixture.FeatureFixture
import io.micronaut.starter.fixture.ProjectFixture
import io.micronaut.starter.options.Language
import spock.lang.Specification

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
}
