package io.micronaut.starter.feature.objectstorage

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.camunda.ExternalWorker
import io.micronaut.starter.feature.function.Cloud
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language

class ObjectStorageLocalSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void "object-storage-local has the correct properties"() {
        given:
        ObjectStorageFeature feature = beanContext.streamOfType(ObjectStorageLocal).findFirst().orElse(null)

        expect:
        feature.name == "object-storage-local"
        feature.title == "Object Storage - Local"
        feature.description == "Micronaut Object Storage provides a uniform API to create, read and delete objects in the major cloud providers. This feature adds a local implementation to save to a folder in your computer which you may want to use during testing and development.";
        feature.category == Category.DEV_TOOLS
        for (ApplicationType applicationType : ApplicationType.values()) {
            assert feature.supports(applicationType)
        }
    }

    void "test dependency added for object-storage-local feature"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(["object-storage-local"])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut.objectstorage", "micronaut-object-storage-local", Scope.COMPILE)

        where:
        buildTool << BuildTool.values()
    }
}
