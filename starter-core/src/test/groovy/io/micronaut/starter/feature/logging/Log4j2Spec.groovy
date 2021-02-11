package io.micronaut.starter.feature.logging

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.dependencies.Dependency
import io.micronaut.starter.build.dependencies.MavenCoordinate
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.TestFramework

class Log4j2Spec extends ApplicationContextSpec {

    void "org.apache.logging.log4j dependencies are present for log4j2 feature and build gradle"() {
        when:
        List<Dependency> dependencies = getFeatureDependencies(Log4j2, BuildTool.GRADLE, TestFramework.JUNIT)
        List<MavenCoordinate> annotationProcessors = getAnnotationProcessors(Log4j2, BuildTool.GRADLE, TestFramework.JUNIT)
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(["log4j2"]), false, dependencies, annotationProcessors).render().toString()

        then:
        template.contains('implementation("org.apache.logging.log4j:log4j-core:2.12.1")')
        template.contains('runtimeOnly("org.apache.logging.log4j:log4j-api:2.12.1")')
        template.contains('runtimeOnly("org.apache.logging.log4j:log4j-slf4j-impl:2.12.1")')
    }

    void "org.apache.logging.log4j dependencies are present for log4j2 feature and build maven"() {
        given:
        List<Dependency> dependencies = getFeatureDependencies(Log4j2, BuildTool.MAVEN, TestFramework.JUNIT)
        List<MavenCoordinate> annotationProcessors = getAnnotationProcessors(Log4j2, BuildTool.MAVEN, TestFramework.JUNIT)
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(["log4j2"]), [], dependencies, annotationProcessors).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>org.apache.logging.log4j</groupId>
      <artifactId>log4j-core</artifactId>
      <version>2.12.1</version>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>org.apache.logging.log4j</groupId>
      <artifactId>log4j-api</artifactId>
      <version>2.12.1</version>
      <scope>runtime</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>org.apache.logging.log4j</groupId>
      <artifactId>log4j-slf4j-impl</artifactId>
      <version>2.12.1</version>
      <scope>runtime</scope>
    </dependency>
""")
    }
}
