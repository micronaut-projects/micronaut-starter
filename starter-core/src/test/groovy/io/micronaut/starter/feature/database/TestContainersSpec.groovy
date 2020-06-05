package io.micronaut.starter.feature.database

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom

import java.util.stream.Collectors

class TestContainersSpec extends BeanContextSpec {

    void "test oracle dependency is present for gradle"() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['testcontainers', 'oracle'])).render().toString()

        then:
        template.contains('testRuntimeOnly("org.testcontainers:oracle-xe:1.14.3")')
    }

    void "test mysql dependency is present for gradle"() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['testcontainers', 'mysql'])).render().toString()

        then:
        template.contains('testRuntimeOnly("org.testcontainers:mysql:1.14.3")')
    }

    void "test postgres dependency is present for gradle"() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['testcontainers', 'postgres'])).render().toString()

        then:
        template.contains('testRuntimeOnly("org.testcontainers:postgresql:1.14.3")')
    }

    void "test mariadb dependency is present for gradle"() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['testcontainers', 'mariadb'])).render().toString()

        then:
        template.contains('testRuntimeOnly("org.testcontainers:mariadb:1.14.3")')
    }

    void "test sqlserver dependency is present for gradle"() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['testcontainers', 'sqlserver'])).render().toString()

        then:
        template.contains('testRuntimeOnly("org.testcontainers:mssqlserver:1.14.3")')
    }

    void "test oracle dependency is present for maven"() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['testcontainers', 'oracle']), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>org.testcontainers</groupId>
      <artifactId>oracle-xe</artifactId>
      <version>1.14.3</version>
      <scope>test</scope>
    </dependency>
""")

    }

    void "test mysql dependency is present for maven"() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['testcontainers', 'mysql']), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>org.testcontainers</groupId>
      <artifactId>mysql</artifactId>
      <version>1.14.3</version>
      <scope>test</scope>
    </dependency>
""")
    }

    void "test postgres dependency is present for maven"() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['testcontainers', 'postgres']), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>org.testcontainers</groupId>
      <artifactId>postgresql</artifactId>
      <version>1.14.3</version>
      <scope>test</scope>
    </dependency>
""")
    }

    void "test mariadb dependency is present for maven"() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['testcontainers', 'mariadb']), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>org.testcontainers</groupId>
      <artifactId>mariadb</artifactId>
      <version>1.14.3</version>
      <scope>test</scope>
    </dependency>
""")
    }

    void "test sqlserver dependency is present for maven"() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['testcontainers', 'sqlserver']), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>org.testcontainers</groupId>
      <artifactId>mssqlserver</artifactId>
      <version>1.14.3</version>
      <scope>test</scope>
    </dependency>
""")
    }

    void "test there is a dependency for every non embedded driver feature"() {
        when:
        String mavenTemplate = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['testcontainers', driverFeature.getName()]), []).render().toString()
        String gradleTemplate = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['testcontainers', driverFeature.getName()])).render().toString()

        then:
        gradleTemplate.contains("org.testcontainers")
        mavenTemplate.contains("org.testcontainers")

        where:
        driverFeature <<  beanContext.streamOfType(DatabaseDriverFeature)
                .filter({ f ->  !f.embedded() })
                .collect(Collectors.toList())
    }
}
