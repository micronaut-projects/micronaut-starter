package io.micronaut.starter.feature.logging

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.options.BuildTool
import spock.lang.Unroll

import java.util.regex.Pattern

class Log4j2Spec extends ApplicationContextSpec {

    @Unroll
    void "test dependency added for lg4j2 feature" (BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features([Log4j2.NAME])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasBom("org.apache.logging.log4j", "log4j-bom", Scope.COMPILE)
        verifier.hasDependency("org.apache.logging.log4j", "log4j-api", Scope.COMPILE)
        verifier.hasDependency("org.apache.logging.log4j", "log4j-core", Scope.RUNTIME)
        verifier.hasDependency("org.apache.logging.log4j", "log4j-slf4j-impl", Scope.RUNTIME)

        where:
        buildTool << BuildTool.values()

    }

    void "org.apache.logging.log4j dependencies are present for log4j2 feature and build gradle"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['log4j2'])
                .render()

        then:
        template.contains('implementation platform("org.apache.logging.log4j:log4j-bom:')
        template.contains('implementation("org.apache.logging.log4j:log4j-api")')
        template.contains('runtimeOnly("org.apache.logging.log4j:log4j-core")')
        template.contains('runtimeOnly("org.apache.logging.log4j:log4j-slf4j-impl")')
    }

    void "org.apache.logging.log4j dependencies are present for log4j2 feature and build maven"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['log4j2'])
                .render()

        then:
        Pattern.compile("""
    <dependency>
      <groupId>org\\.apache\\.logging\\.log4j</groupId>
      <artifactId>log4j-bom</artifactId>
      <version>.*?</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
""").matcher(template).find()
        Pattern.compile("""
    <dependency>
      <groupId>org\\.apache\\.logging\\.log4j</groupId>
      <artifactId>log4j-api</artifactId>
      <scope>compile</scope>
    </dependency>
""").matcher(template).find()
        Pattern.compile("""
    <dependency>
      <groupId>org\\.apache\\.logging\\.log4j</groupId>
      <artifactId>log4j-core</artifactId>
      <scope>runtime</scope>
    </dependency>
""").matcher(template).find()
        Pattern.compile("""
    <dependency>
      <groupId>org\\.apache\\.logging\\.log4j</groupId>
      <artifactId>log4j-slf4j-impl</artifactId>
      <scope>runtime</scope>
    </dependency>
""").matcher(template).find()
    }
}
