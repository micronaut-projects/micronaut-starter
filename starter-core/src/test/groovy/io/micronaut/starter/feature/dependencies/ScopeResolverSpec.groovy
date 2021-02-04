package io.micronaut.starter.feature.dependencies

import io.micronaut.starter.feature.build.gradle.Gradle
import io.micronaut.starter.options.BuildTool
import spock.lang.Specification
import spock.lang.Unroll

class ScopeResolverSpec extends Specification {

    @Unroll("the resolved scoped for #gradleName and build #buildName is #expected")
    void "resolve a scope from a maven scope"(GradleConfiguration gradleConfiguration, BuildTool buildTool, String expected, String gradleName, String buildName) {
        expect:
        expected == ScopeResolver.resolve(gradleConfiguration, buildTool)

        where:
        gradleConfiguration                     | buildTool         | expected
        GradleConfiguration.API                 | BuildTool.GRADLE  | 'api'
        GradleConfiguration.IMPLEMENTATION      | BuildTool.GRADLE  | 'implementation'
        GradleConfiguration.COMPILE_ONLY        | BuildTool.GRADLE  | 'compileOnly'
        GradleConfiguration.COMPILE_ONLY_API    | BuildTool.GRADLE  | 'compileOnlyApi'
        GradleConfiguration.RUNTIME_ONLY        | BuildTool.GRADLE  | 'runtimeOnly'
        GradleConfiguration.TEST_IMPLEMENTATION | BuildTool.GRADLE  | 'testImplementation'
        GradleConfiguration.TEST_COMPILE_ONLY   | BuildTool.GRADLE  | 'testCompileOnly'
        GradleConfiguration.TEST_RUNTIME_ONLY   | BuildTool.GRADLE  | 'testRuntimeOnly'
        GradleConfiguration.API                 | BuildTool.MAVEN   | 'compile'
        GradleConfiguration.IMPLEMENTATION      | BuildTool.MAVEN   | 'compile'
        GradleConfiguration.COMPILE_ONLY        | BuildTool.MAVEN   | 'provided'
        GradleConfiguration.COMPILE_ONLY_API    | BuildTool.MAVEN   | 'provided'
        GradleConfiguration.RUNTIME_ONLY        | BuildTool.MAVEN   | 'runtime'
        GradleConfiguration.TEST_IMPLEMENTATION | BuildTool.MAVEN   | 'test'
        GradleConfiguration.TEST_COMPILE_ONLY   | BuildTool.MAVEN   | 'test'
        GradleConfiguration.TEST_RUNTIME_ONLY   | BuildTool.MAVEN   | 'test'

        gradleName = gradleConfiguration.getConfigurationName()
        buildName = buildTool.name
    }

    @Unroll("the resolved scoped for #mvnName and build #buildName is #expected")
    void "resolve a scope from a maven scope"(MavenScope mvnScope, BuildTool buildTool, String expected, String mvnName, String buildName) {
        expect:
        expected == ScopeResolver.resolve(mvnScope, buildTool)

        where:
        mvnScope            | buildTool         | expected
        MavenScope.COMPILE  | BuildTool.MAVEN   | 'compile'
        MavenScope.PROVIDED | BuildTool.MAVEN   | 'provided'
        MavenScope.RUNTIME  | BuildTool.MAVEN   | 'runtime'
        MavenScope.TEST     | BuildTool.MAVEN   | 'test'
        MavenScope.SYSTEM   | BuildTool.MAVEN   | 'system'
        MavenScope.IMPORT   | BuildTool.MAVEN   | 'import'
        MavenScope.COMPILE  | BuildTool.GRADLE  | 'implementation'
        MavenScope.PROVIDED | BuildTool.GRADLE  | 'compileOnly'
        MavenScope.RUNTIME  | BuildTool.GRADLE  | 'runtimeOnly'
        MavenScope.TEST     | BuildTool.GRADLE  | 'testImplementation'
        MavenScope.SYSTEM   | BuildTool.GRADLE  | 'system'
        MavenScope.IMPORT   | BuildTool.GRADLE  | 'import'

        mvnName = mvnScope.toString()
        buildName = buildTool.name
    }
}
