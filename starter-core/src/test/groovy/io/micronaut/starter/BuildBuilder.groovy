package io.micronaut.starter

import io.micronaut.context.ApplicationContext
import io.micronaut.context.BeanContext
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.openrewrite.RecipeFetcher
import io.micronaut.projectgen.core.options.JdkVersion
import io.micronaut.projectgen.core.options.Language
import io.micronaut.projectgen.core.options.Options
import io.micronaut.projectgen.core.options.TestFramework
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.projectgen.core.generator.Project
import io.micronaut.projectgen.core.generator.GeneratorContext
import io.micronaut.projectgen.core.buildtools.DefaultRepositoryResolver
import io.micronaut.projectgen.core.buildtools.dependencies.CoordinateResolver
import io.micronaut.projectgen.core.buildtools.gradle.GradleBuild
import io.micronaut.projectgen.core.buildtools.gradle.GradleBuildCreator
import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.build.maven.JvmArgumentsFeature
import io.micronaut.projectgen.core.buildtools.maven.MavenBuild
import io.micronaut.projectgen.core.buildtools.maven.MavenBuildCreator
import io.micronaut.projectgen.core.feature.Features
import io.micronaut.projectgen.core.buildtools.gradle.Gradle
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.ContextFixture
import io.micronaut.starter.fixture.ProjectFixture
import io.micronaut.starter.options.*

class BuildBuilder implements ProjectFixture, ContextFixture {

    private BuildTool buildTool
    private List<String> features
    private Language language
    private TestFramework testFramework
    private String framework
    private ApplicationType applicationType
    private JdkVersion jdkVersion
    private Project project
    private ApplicationContext ctx
    private GradleBuildCreator gradleDependencyResolver
    private MavenBuildCreator mavenDependencyResolver

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

    BuildBuilder framework(String framework) {
        this.framework = framework
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

    Project getProject() {
        this.project ?: buildProject()
    }

    /**
     * If {@param render} is set to false it returns a {@link GradleBuild} or {@link MavenBuild} object
     */
    Object build(boolean render = true) {
        List<String> featureNames = this.features ?: []
        Language language = this.language ?: Language.DEFAULT_OPTION
        TestFramework testFramework = this.testFramework ?: language.defaults.test
        ApplicationType type = this.applicationType ?: ApplicationType.DEFAULT
        Project project = getProject()
        JdkVersion jdkVersion = this.jdkVersion ?: MicronautJdkVersionConfiguration.DEFAULT_OPTION
        Options options = MicronautOptions.builder()
                .name("demo")
                .packageName("com.example")
                .language(language)
                .testFramework(testFramework)
                .buildTool(buildTool)
                .javaVersion(jdkVersion)
                .build()
        Features features = getFeatures(featureNames, options, type)

        if (buildTool.isGradle()) {
            GradleBuild build = gradleBuild(options, features, project, type)
            if (render) {
                return buildGradle.template(type, project, features, build).render().toString()
            }
            return build
        } else if (buildTool == BuildTool.MAVEN) {
            MavenBuild build = mavenBuild(options, features, project, type)
            if (render) {
                return pom.template(type, project, features, build, JvmArgumentsFeature.getJvmArguments(features.getFeatures())).render().toString()
            }
            return build
        }
        null
    }

    String render() {
        return build()
    }

    private GradleBuildCreator getGradleDependencyResolver() {
        if (gradleDependencyResolver == null) {
            gradleDependencyResolver = ctx.getBean(GradleBuildCreator)
        }
        gradleDependencyResolver
    }

    private MavenBuildCreator getMavenDependencyResolver() {
        if (mavenDependencyResolver == null) {
            mavenDependencyResolver = ctx.getBean(MavenBuildCreator)
        }
        mavenDependencyResolver
    }

    MavenBuild mavenBuild(Options options, Features features, Project project, ApplicationType type) {
        GeneratorContext ctx = createGeneratorContextAndApplyFeatures(options, features, project, type)
        getMavenDependencyResolver().create(ctx, new DefaultRepositoryResolver().resolveRepositories(ctx))
    }

    GradleBuild gradleBuild(Options options, Features features, Project project, ApplicationType type) {
        GeneratorContext ctx = createGeneratorContextAndApplyFeatures(options, features, project, type)
        getGradleDependencyResolver().create(ctx, new DefaultRepositoryResolver().resolveRepositories(ctx), Gradle.DEFAULT_USER_VERSION_CATALOGUE)
    }

    GeneratorContext createGeneratorContextAndApplyFeatures(Options options, Features features, Project project, ApplicationType type) {
        GeneratorContext ctx = new GeneratorContext(project, options, features.features, ctx.getBean(CoordinateResolver), ctx.getBean(RecipeFetcher))
        ctx.applyFeatures()
        ctx
    }

    @Override
    BeanContext getBeanContext() {
        ctx
    }
}
