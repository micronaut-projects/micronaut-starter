package io.micronaut.starter.feature.build.maven

import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.feature.graalvm.GraalNativeImage
import io.micronaut.starter.fixture.FeatureFixture
import io.micronaut.starter.fixture.ProjectFixture
import spock.lang.Specification

class MavenSpec extends Specification implements ProjectFixture, FeatureFixture {

    void 'test graal-native-image feature'() {
        when:
        String template = pom.template(buildProject(), buildJavaWithFeatures(new GraalNativeImage()), [:]).render().toString()

        then:
        template.contains('<groupId>org.graalvm.nativeimage</groupId>')
        template.contains('<artifactId>svm</artifactId>')
        template.contains('<scope>provided</scope>')
        template.contains('<artifactId>micronaut-graal</artifactId>')

        when:
        template = pom.template(buildProject(), buildKotlinWithFeatures(new GraalNativeImage()), [:]).render().toString()

        then:
        template.contains('<groupId>org.graalvm.nativeimage</groupId>')
        template.contains('<artifactId>svm</artifactId>')
        template.contains('<scope>provided</scope>')
        template.contains('<artifactId>micronaut-graal</artifactId>')

        when:
        template = pom.template(buildProject(), buildGroovyWithFeatures(new GraalNativeImage()), [:]).render().toString()

        then:
        template.contains('<groupId>org.graalvm.nativeimage</groupId>')
        template.contains('<artifactId>svm</artifactId>')
        template.contains('<scope>provided</scope>')
        template.contains('<artifactId>micronaut-graal</artifactId>')
    }
}
