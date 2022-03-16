package io.micronaut.starter.feature.dependencies

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.dependencies.Dependency
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.Feature

import jakarta.inject.Singleton

import static io.micronaut.starter.options.BuildTool.GRADLE
import static io.micronaut.starter.options.BuildTool.GRADLE_KOTLIN
import static io.micronaut.starter.options.BuildTool.MAVEN

class PomDependencySpec extends ApplicationContextSpec {

    @Override
    Map<String, Object> getConfiguration() {
        ['spec.name': 'PomDependencySpec']
    }

    void 'test adding pom to Gradle build'() {
        when:
        String template = new BuildBuilder(beanContext, GRADLE)
                .features([PomFeature.NAME])
                .render()

        then:
        template.contains 'implementation platform("gr.oup.id:ar.ifact.id:ver.sion")'
    }

    void 'test adding pom to Gradle Kotlin DSL build'() {
        when:
        String template = new BuildBuilder(beanContext, GRADLE_KOTLIN)
                .features([PomFeature.NAME])
                .render()

        then:
        template.contains 'implementation(platform("gr.oup.id:ar.ifact.id:ver.sion"))'
    }


    void 'test adding pom to Maven build'() {
        when:
        String template = new BuildBuilder(beanContext, MAVEN)
                .features([PomFeature.NAME])
                .render()

        then:
        template.contains '''\
    <dependency>
      <groupId>gr.oup.id</groupId>
      <artifactId>ar.ifact.id</artifactId>
      <version>ver.sion</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
'''
    }

    @Singleton
    static class PomFeature implements Feature {

        public static final String NAME = 'ojdbc-bom'

        final String name = NAME

        final String category = Category.DATABASE
        final String title = 'test'
        final String description = 'test'

        void apply(GeneratorContext generatorContext) {
            generatorContext.addDependency(Dependency.builder()
                    .groupId('gr.oup.id')
                    .artifactId('ar.ifact.id')
                    .version('ver.sion')
                    .scope(Scope.COMPILE)
                    .pom(true)
                    .build())
        }

        boolean supports(ApplicationType applicationType) {
            true
        }
    }
}
