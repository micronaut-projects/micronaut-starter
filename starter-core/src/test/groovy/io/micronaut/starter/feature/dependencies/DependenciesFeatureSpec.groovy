package io.micronaut.starter.feature.dependencies

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.Project
import io.micronaut.starter.build.dependencies.GradleBuild
import io.micronaut.starter.build.dependencies.MavenBuild
import io.micronaut.starter.feature.Features
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Unroll

class DependenciesFeatureSpec extends ApplicationContextSpec implements CommandOutputFixture {
    @Override
    Map<String, Object> getConfiguration() {
        ['spec.name': 'DependenciesFeatureSpec']
    }

    @Unroll
    void 'test gradle geb feature for language=#language and spock'() {
        given:
        TestFramework testFramework = TestFramework.SPOCK
        Project project = buildProject()
        BuildTool buildTool = BuildTool.GRADLE
        ApplicationType type = ApplicationType.DEFAULT

        when:
        Features features = getFeatures(['geb'], language, testFramework)
        Options options = new Options(language, testFramework, buildTool)
        GradleBuild build = gradleBuild(options, features, project, type)
        String template = buildGradle.template(type, project, features, build).render().toString()

        then:
        template.contains('testImplementation("org.gebish:geb-spock:4.0")')
        template.contains('testRuntimeOnly("org.seleniumhq.selenium:selenium-firefox-driver:3.141.59")')
        template.contains('testRuntimeOnly("org.seleniumhq.selenium:selenium-support:3.141.59")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test gradle geb feature for language=#language and junit'() {
        given:
        TestFramework testFramework = TestFramework.JUNIT
        Project project = buildProject()
        BuildTool buildTool = BuildTool.GRADLE
        ApplicationType type = ApplicationType.DEFAULT

        when:
        Features features = getFeatures(['geb'], language, testFramework)
        Options options = new Options(language, testFramework, buildTool)
        GradleBuild build = gradleBuild(options, features, project, type)
        String template = buildGradle.template(type, project, features, build).render().toString()

        then:
        template.contains('testImplementation("org.gebish:geb-junit5:4.0")')
        template.contains('testRuntimeOnly("org.seleniumhq.selenium:selenium-firefox-driver:3.141.59")')
        template.contains('testRuntimeOnly("org.seleniumhq.selenium:selenium-support:3.141.59")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test gradle mybatis feature for language=#language'() {
        given:
        TestFramework testFramework = TestFramework.JUNIT
        Project project = buildProject()
        BuildTool buildTool = BuildTool.GRADLE
        ApplicationType type = ApplicationType.DEFAULT

        when:
        Features features = getFeatures(['mybatis'], language, testFramework)
        Options options = new Options(language, testFramework, buildTool)
        GradleBuild build = gradleBuild(options, features, project, type)
        String template = buildGradle.template(type, project, features, build).render().toString()

        then:
        template.contains('implementation("org.mybatis:mybatis:3.4.6")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven mybatis feature for language=#language'() {
        given:
        TestFramework testFramework = TestFramework.JUNIT
        Project project = buildProject()
        BuildTool buildTool = BuildTool.MAVEN
        ApplicationType type = ApplicationType.DEFAULT

        when:
        Features features = getFeatures(['mybatis'], language, testFramework)
        Options options = new Options(language, testFramework, buildTool)
        MavenBuild build = mavenBuild(options, features, project, type)
        String template = pom.template(type, project, features, build).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>org.mybatis</groupId>
      <artifactId>mybatis</artifactId>
      <version>3.4.6</version>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven geb feature for language=#language'() {
        given:
        TestFramework testFramework = language == Language.GROOVY ? TestFramework.SPOCK : TestFramework.JUNIT
        Project project = buildProject()
        BuildTool buildTool = BuildTool.MAVEN
        ApplicationType type = ApplicationType.DEFAULT

        when:
        Features features = getFeatures(['geb'], language, testFramework)
        Options options = new Options(language, testFramework, buildTool)
        MavenBuild build = mavenBuild(options, features, project, type)
        String template = pom.template(type, project, features, build).render().toString()

        then:
        if (language == Language.GROOVY) {
            assert template.contains("""
    <dependency>
      <groupId>org.gebish</groupId>
      <artifactId>geb-spock</artifactId>
      <version>4.0</version>
      <scope>test</scope>
    </dependency>
""")
        } else {
            assert template.contains("""
    <dependency>
      <groupId>org.gebish</groupId>
      <artifactId>geb-junit5</artifactId>
      <version>4.0</version>
      <scope>test</scope>
    </dependency>
""")
        }

        and:
        template.contains("""
    <dependency>
      <groupId>org.seleniumhq.selenium</groupId>
      <artifactId>selenium-firefox-driver</artifactId>
      <version>3.141.59</version>
      <scope>test</scope>
    </dependency>
""")
        and:
        template.contains("""
    <dependency>
      <groupId>org.seleniumhq.selenium</groupId>
      <artifactId>selenium-support</artifactId>
      <version>3.141.59</version>
      <scope>test</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }
}
