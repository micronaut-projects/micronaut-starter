package io.micronaut.starter

import edu.umd.cs.findbugs.annotations.NonNull
import io.micronaut.context.ApplicationContext
import io.micronaut.inject.qualifiers.Qualifiers
import io.micronaut.starter.build.dependencies.BuildToolDependencyResolver
import io.micronaut.starter.build.dependencies.Dependency
import io.micronaut.starter.build.dependencies.DependencyContext
import io.micronaut.starter.build.dependencies.ScopedArtifact
import io.micronaut.starter.feature.Feature
import io.micronaut.starter.fixture.ContextFixture
import io.micronaut.starter.fixture.ProjectFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.TestFramework
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

abstract class ApplicationContextSpec extends Specification implements ProjectFixture, ContextFixture {

    Map<String, Object> getConfiguration() {
        [:]
    }

    @Shared
    @AutoCleanup
    ApplicationContext beanContext = ApplicationContext.run(configuration)

    @Shared
    BuildToolDependencyResolver gradleDependencyResolver = beanContext.getBean(BuildToolDependencyResolver, Qualifiers.byName("gradle"))
    @Shared
    BuildToolDependencyResolver mavenDependencyResolver = beanContext.getBean(BuildToolDependencyResolver, Qualifiers.byName("maven"))


    List<Dependency> getFeatureDependencies(Class<? extends Feature> feature,
                                            BuildTool buildTool,
                                            TestFramework testFramework) {
        MockDependencyContext dependencyContext = new MockDependencyContext(testFramework)
        beanContext.getBean(feature).applyDependencies(dependencyContext)

        if (buildTool.isGradle()) {
            return gradleDependencyResolver.resolve(dependencyContext.scopedArtifacts)
        }
        return mavenDependencyResolver.resolve(dependencyContext.scopedArtifacts)
    }


    static class MockDependencyContext implements DependencyContext {

        TestFramework testFramework
        Set<ScopedArtifact> scopedArtifacts = new HashSet<>()

        MockDependencyContext(TestFramework testFramework) {
            this.testFramework = testFramework
        }

        @Override
        TestFramework getTestFramework() {
            return testFramework
        }

        @Override
        void addDependency(@NonNull ScopedArtifact scopedArtifact) {
            scopedArtifacts << scopedArtifact
        }
    }
}
