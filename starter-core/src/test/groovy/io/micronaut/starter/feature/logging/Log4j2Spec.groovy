package io.micronaut.starter.feature.logging

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.options.BuildTool

class Log4j2Spec extends ApplicationContextSpec {

    void "org.apache.logging.log4j dependencies are present for log4j2 feature and build gradle"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['log4j2'])
                .render()

        then:
        template.contains('implementation("org.apache.logging.log4j:log4j-core:2.12.1")')
        template.contains('runtimeOnly("org.apache.logging.log4j:log4j-api:2.12.1")')
        template.contains('runtimeOnly("org.apache.logging.log4j:log4j-slf4j-impl:2.12.1")')
    }

    void "org.apache.logging.log4j dependencies are present for log4j2 feature and build maven"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['log4j2'])
                .render()

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
