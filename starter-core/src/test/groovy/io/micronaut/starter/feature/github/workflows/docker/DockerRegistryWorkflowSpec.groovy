package io.micronaut.starter.feature.github.workflows.docker

import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.BeanContextSpec
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.JdkVersion
import io.micronaut.projectgen.core.options.Language
import io.micronaut.starter.options.MicronautJdkVersionConfiguration
import io.micronaut.projectgen.core.options.Options
import io.micronaut.projectgen.core.options.TestFramework

class DockerRegistryWorkflowSpec extends BeanContextSpec implements CommandOutputFixture{

    void 'test github workflow readme'(){
        when:
        Map<String, String> output = generate([DockerRegistryWorkflow.NAME])
        String readme = output['README.md']

        then:
        readme
        readme.contains("""
Add the following GitHub secrets:

| Name | Description |
| ---- | ----------- |
| DOCKER_USERNAME | Username for Docker registry authentication. |
| DOCKER_PASSWORD | Docker registry password. |
| DOCKER_REPOSITORY_PATH | Path to the docker image repository inside the registry, e.g. for the image `foo/bar/micronaut:0.1` it is `foo/bar`. |
| DOCKER_REGISTRY_URL | Docker registry url. |
""")
    }

    void 'test github workflow is created for #buildTool'(BuildTool buildTool, String workflowName) {
        given:
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.DEFAULT)
                .language(Language.JAVA)
                .testFramework(TestFramework.JUNIT)
                .buildTool(buildTool)
                .javaVersion(MicronautJdkVersionConfiguration.DEFAULT_OPTION)
                .features([DockerRegistryWorkflow.NAME])
                .build()
        when:
        def output = generate(options)
        def workflow = output[".github/workflows/${workflowName}"]

        then:
        workflow
        workflow.contains("name: Java CI")

        where:
        buildTool | workflowName
        BuildTool.GRADLE | "gradle.yml"
        BuildTool.MAVEN | "maven.yml"
    }

    void 'test push to docker workflow for maven'() {
        given:
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.DEFAULT)
                .language(Language.JAVA)
                .testFramework(TestFramework.JUNIT)
                .buildTool(BuildTool.MAVEN)
                .javaVersion(MicronautJdkVersionConfiguration.DEFAULT_OPTION)
                .features([DockerRegistryWorkflow.NAME])
                .build()
        when:
        def output = generate(options)
        def maven = output['.github/workflows/maven.yml']

        then:
        maven
        maven.contains("DOCKER_IMAGE=`echo \"\${DOCKER_REGISTRY_URL}/\${DOCKER_REPOSITORY_PATH}/foo")
    }

    void 'test docker image is configured in #buildFileName'(BuildTool buildTool) {
        given:
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.DEFAULT)
                .language(Language.JAVA)
                .buildTool(buildTool)
                .features([DockerRegistryWorkflow.NAME])
                .build()
        when:
        def output = generate(options)
        def gradle = output[buildTool.buildFileName]

        then:
        if (buildTool == BuildTool.GRADLE) {
            assert gradle.contains('''
    dockerBuild {
        images = ["${System.env.DOCKER_IMAGE ?: project.name}:$project.version"]
    }''')
        } else {
            assert gradle.contains('''
    dockerBuild {
        images = listOf("${System.getenv("DOCKER_IMAGE") ?: project.name}:${project.version}")
    }''')
        }

        where:
        buildTool << BuildTool.valuesGradle()
        buildFileName = buildTool.buildFileName
    }

    void 'test github gradle workflow java version for #version'(JdkVersion version){
        given:
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.DEFAULT)
                .language(Language.JAVA)
                .testFramework(TestFramework.JUNIT)
                .buildTool(BuildTool.GRADLE)
                .javaVersion(version)
                .features([DockerRegistryWorkflow.NAME])
                .build()
        when:
        def output = generate(options)
        def workflow = output['.github/workflows/gradle.yml']

        then:
        workflow
        workflow.contains("java-version: ${version.majorVersion()}")

        where:
        version << MicronautJdkVersionConfiguration.SUPPORTED_JDKS
    }
}
