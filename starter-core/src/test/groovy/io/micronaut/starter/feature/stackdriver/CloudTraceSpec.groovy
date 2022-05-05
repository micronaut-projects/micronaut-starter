package io.micronaut.starter.feature.stackdriver

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class CloudTraceSpec extends ApplicationContextSpec {

    @Subject
    @Shared
    CloudTrace cloudtrace = beanContext.getBean(CloudTrace)

    void "cloudtrace belongs to Tracing category"() {
        expect:
        Category.TRACING == cloudtrace.category
    }

    void "cloudtrace is visible"() {
        expect:
        cloudtrace.visible
    }

    void "cloudtrace title and description are different"() {
        expect:
        cloudtrace.getTitle()
        cloudtrace.getDescription()
        cloudtrace.getTitle() != cloudtrace.getDescription()
    }

    @Unroll("feature cloudtrace works for application type: #applicationType")
    void "feature cloudtrace works for every type of application type"(ApplicationType applicationType) {
        expect:
        cloudtrace.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
    }

    @Unroll
    void 'dependency is included with maven and feature cloudtrace for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['gcp-cloud-trace'])
                .language(language)
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.gcp</groupId>
      <artifactId>micronaut-gcp-tracing</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'dependency is included with gradle and feature gcp-cloud-trace for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['gcp-cloud-trace'])
                .language(language)
                .render()

        then:
        template.contains('implementation("io.micronaut.gcp:micronaut-gcp-tracing")')

        where:
        language << Language.values()
    }

    @Unroll
    void 'test gradle graalvm & gcp-cloud-trace features adds gcp native-image support for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['gcp-cloud-trace', 'graalvm'])
                .render()

        then:
        template.count('implementation("io.micronaut.gcp:micronaut-gcp-tracing")') == 1

        and:
        template.count('nativeImageCompileOnly("com.google.cloud:native-image-support")') == 1

        where:
        language << [Language.JAVA, Language.KOTLIN]
    }

    @Unroll
    void 'test maven graalvm & gcp-cloud-trace features adds gcp native-image support for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['gcp-cloud-trace', 'graalvm'])
                .render()

        then:
        template.count('''
    <dependency>
      <groupId>io.micronaut.gcp</groupId>
      <artifactId>micronaut-gcp-tracing</artifactId>
      <scope>compile</scope>
    </dependency>
''') == 1

        and:
        template.count('''
        <profile>
            <id>graalVM</id>
            <activation>
                <property>
                    <name>packaging</name>
                    <value>native-image</value>
                </property>
            </activation>
            <dependencies>
                <dependency>
                    <groupId>com.google.cloud</groupId>
                    <artifactId>native-image-support</artifactId>
               </dependency>
            </dependencies>
        </profile>
''') == 1

        where:
        language << [Language.JAVA, Language.KOTLIN]
    }
}
