package io.micronaut.starter.feature.logging

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.Project
import io.micronaut.starter.build.dependencies.GradleBuild
import io.micronaut.starter.build.dependencies.MavenBuild
import io.micronaut.starter.feature.Features
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options

class Log4j2Spec extends ApplicationContextSpec {

    void "org.apache.logging.log4j dependencies are present for log4j2 feature and build gradle"() {
        given:
        Language language = Language.JAVA
        Project project = buildProject()
        BuildTool buildTool = BuildTool.GRADLE
        ApplicationType type = ApplicationType.DEFAULT

        when:
        Features features = getFeatures(['log4j2'], language)
        Options options = new Options(language, buildTool)
        GradleBuild build = gradleBuild(options, features, project, type)
        String template = buildGradle.template(type, project, features, build).render().toString()

        then:
        template.contains('implementation("org.apache.logging.log4j:log4j-core:2.12.1")')
        template.contains('runtimeOnly("org.apache.logging.log4j:log4j-api:2.12.1")')
        template.contains('runtimeOnly("org.apache.logging.log4j:log4j-slf4j-impl:2.12.1")')
    }

    void "org.apache.logging.log4j dependencies are present for log4j2 feature and build maven"() {
        given:
        Language language = Language.JAVA
        Project project = buildProject()
        BuildTool buildTool = BuildTool.MAVEN
        ApplicationType type = ApplicationType.DEFAULT

        when:
        Features features = getFeatures(['log4j2'], language)
        Options options = new Options(language, buildTool)
        MavenBuild build = mavenBuild(options, features, project, type)
        String template = pom.template(type, project, features, build).render().toString()

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
