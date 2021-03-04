package io.micronaut.starter

import io.micronaut.context.ApplicationContext
import io.micronaut.context.BeanContext
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.Project
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.Property
import io.micronaut.starter.feature.Features
import io.micronaut.starter.fixture.ContextFixture
import io.micronaut.starter.fixture.ProjectFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom

class BuildBuilder implements ProjectFixture, ContextFixture {

    BuildTool buildTool
    List<String> features
    Language language
    TestFramework testFramework
    ApplicationType applicationType
    JdkVersion jdkVersion
    Project project
    ApplicationContext ctx

    BuildBuilder(ApplicationContext ctx, BuildTool buildTool) {
        this.ctx = ctx
        this.buildTool = buildTool
    }

    BuildBuilder features(List<String> features) {
        this.features = features
        this
    }

    BuildBuilder language(Language language) {
        this.language = language
        this
    }

    BuildBuilder testFramework(TestFramework testFramework) {
        this.testFramework = testFramework
        this
    }

    BuildBuilder applicationType(ApplicationType applicationType) {
        this.applicationType = applicationType
        this
    }

    BuildBuilder jdkVersion(JdkVersion jdkVersion) {
        this.jdkVersion = jdkVersion
        this
    }

    BuildBuilder project(Project project) {
        this.project = project
        this
    }

    String render() {
        List<String> featureNames = this.features ?: []
        Language language = this.language ?: Language.DEFAULT_OPTION
        TestFramework testFramework = this.testFramework ?: language.defaults.test
        ApplicationType type = this.applicationType ?: ApplicationType.DEFAULT
        Project project = this.project ?: buildProject()
        JdkVersion jdkVersion = this.jdkVersion ?: JdkVersion.JDK_8

        Options options = new Options(language, testFramework, buildTool, jdkVersion)
        Features features = getFeatures(featureNames, options, type)
        if (buildTool.isGradle()) {
            return buildGradle.template(type, project, features, buildTool == BuildTool.GRADLE_KOTLIN).render().toString()
        } else if (buildTool == BuildTool.MAVEN) {
            GeneratorContext ctx = createGeneratorContextAndApplyFeatures(options, features, project, type)
            List<Property> properties = ctx.getBuildProperties().getProperties()
            return pom.template(type, project, features, properties).render().toString()
        }
        null

    }

    GeneratorContext createGeneratorContextAndApplyFeatures(Options options, Features features, Project project, ApplicationType type) {
        GeneratorContext ctx = new GeneratorContext(project, type, options, null, features.features)
        features.features.each {feat -> feat.apply(ctx)}
        ctx
    }

    @Override
    BeanContext getBeanContext() {
        ctx
    }
}
