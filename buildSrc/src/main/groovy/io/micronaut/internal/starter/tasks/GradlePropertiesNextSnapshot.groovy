package io.micronaut.internal.starter.tasks


import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*


abstract class GradlePropertiesNextSnapshot extends DefaultTask {
    @Input
    abstract Property<String> getPropertyName()

    @InputFile
    abstract RegularFileProperty getGradleProperties()

    GradlePropertiesNextSnapshot() {
        getGradleProperties().convention(getProject().getLayout().getProjectDirectory().file("gradle.properties"))
    }

    @TaskAction
    def greet() {
        File propertiesFile = getGradleProperties().get().asFile
        Properties properties = new Properties()
        propertiesFile.withInputStream {
            properties.load(it)
        }
        String key = propertyName.get()
        String version = properties.get(key)
        SoftwareVersion softwareVersion = SoftwareVersion.build(version)
        if (softwareVersion.nextSnapshot()) {
            properties.setProperty(key, softwareVersion.toString())
            properties.store(new FileOutputStream(propertiesFile), "")
        }

    }
}
