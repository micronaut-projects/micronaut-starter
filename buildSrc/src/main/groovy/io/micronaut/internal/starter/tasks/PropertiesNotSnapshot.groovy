package io.micronaut.internal.starter.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction

abstract class PropertiesNotSnapshot extends DefaultTask {
    @Input
    abstract Property<String> getPropertyName()

    @InputFile
    abstract RegularFileProperty getGradleProperties()

    PropertiesNotSnapshot() {
        getGradleProperties().convention(getProject().getLayout().getProjectDirectory().file("gradle.properties"))
    }

    @TaskAction
    def validatedPropertiesFile() {
        File propertiesFile = getGradleProperties().get().asFile
        Properties properties = new Properties()
        propertiesFile.withInputStream {
            properties.load(it)
        }
        String key = propertyName.get()
        String version = properties.get(key)
        SoftwareVersion softwareVersion = SoftwareVersion.build(version)
        if (softwareVersion.isSnapshot()) {
            throw new GradleException("Property $key is a snapshot version: $version")
        }
    }
}
