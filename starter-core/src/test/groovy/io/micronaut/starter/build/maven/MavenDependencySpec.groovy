package io.micronaut.starter.build.maven

import io.micronaut.starter.BeanContextSpec
import io.micronaut.projectgen.core.buildtools.dependencies.DependencyContextImpl
import io.micronaut.projectgen.core.buildtools.dependencies.CoordinateResolver
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency
import io.micronaut.projectgen.core.buildtools.dependencies.DependencyContext
import io.micronaut.projectgen.core.buildtools.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.Language

class MavenDependencySpec  extends BeanContextSpec implements CommandOutputFixture {

    public static final String ARTIFACT_ID_LOGBACK_CLASSIC = "logback-classic"
    public static final String GROUP_ID_LOGBACK = "ch.qos.logback"

    void "maven dependency in test and runtime should be only in compile"() {
        given:
        CoordinateResolver coordinateResolver = beanContext.getBean(CoordinateResolver)
        DependencyContext dependencyContext = new DependencyContextImpl(coordinateResolver)

        when:
        List<Dependency> dependencies = dependencyContext.removeDuplicates(List.of(
                Dependency.builder().scope(Scope.COMPILE).groupId(GROUP_ID_LOGBACK).artifactId(ARTIFACT_ID_LOGBACK_CLASSIC).build(),
                Dependency.builder().scope(Scope.RUNTIME).groupId(GROUP_ID_LOGBACK).artifactId(ARTIFACT_ID_LOGBACK_CLASSIC).build(),
        ), Language.JAVA, BuildTool.MAVEN)

        then:
        dependencies == List.of(
                Dependency.builder().scope(Scope.COMPILE).groupId(GROUP_ID_LOGBACK).artifactId(ARTIFACT_ID_LOGBACK_CLASSIC).build())

        when:
        dependencies = dependencyContext.removeDuplicates(List.of(
                Dependency.builder().scope(Scope.TEST).groupId(GROUP_ID_LOGBACK).artifactId(ARTIFACT_ID_LOGBACK_CLASSIC).build(),
                Dependency.builder().scope(Scope.RUNTIME).groupId(GROUP_ID_LOGBACK).artifactId(ARTIFACT_ID_LOGBACK_CLASSIC).build(),
        ), Language.JAVA, BuildTool.GRADLE)

        then:
        dependencies == List.of(
                Dependency.builder().scope(Scope.RUNTIME).groupId(GROUP_ID_LOGBACK).artifactId(ARTIFACT_ID_LOGBACK_CLASSIC).build(),
                Dependency.builder().scope(Scope.TEST).groupId(GROUP_ID_LOGBACK).artifactId(ARTIFACT_ID_LOGBACK_CLASSIC).build(),
        )

        when:
        dependencies = dependencyContext.removeDuplicates(List.of(
                Dependency.builder().scope(Scope.TEST).groupId(GROUP_ID_LOGBACK).artifactId(ARTIFACT_ID_LOGBACK_CLASSIC).build(),
                Dependency.builder().scope(Scope.RUNTIME).groupId(GROUP_ID_LOGBACK).artifactId(ARTIFACT_ID_LOGBACK_CLASSIC).build(),
        ), Language.JAVA, BuildTool.MAVEN)

        then:
        dependencies == List.of(
                Dependency.builder().scope(Scope.RUNTIME).groupId(GROUP_ID_LOGBACK).artifactId(ARTIFACT_ID_LOGBACK_CLASSIC).build())

        when:
        dependencies = dependencyContext.removeDuplicates(List.of(
                Dependency.builder().scope(Scope.COMPILE).groupId(GROUP_ID_LOGBACK).artifactId(ARTIFACT_ID_LOGBACK_CLASSIC).build(),
                Dependency.builder().scope(Scope.RUNTIME).groupId(GROUP_ID_LOGBACK).artifactId(ARTIFACT_ID_LOGBACK_CLASSIC).build(),
        ), Language.JAVA, BuildTool.MAVEN)

        then:
        dependencies == List.of(
                Dependency.builder().scope(Scope.COMPILE).groupId(GROUP_ID_LOGBACK).artifactId(ARTIFACT_ID_LOGBACK_CLASSIC).build())
    }
}
