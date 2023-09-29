package io.micronaut.starter.feature

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.fixture.CommandOutputFixture

class SettingsExtensionsSpec  extends ApplicationContextSpec implements CommandOutputFixture {

    @Override
    Map<String, Object> getConfiguration() {
        ['spec.name': 'SettingsExtensionsSpec']
    }

    void "it is possible to provide extensions to settings.gradle"() {
        when:
        def output = generate(['google-app-engine-gradle'])
        def settings = output["settings.gradle.kts"]

        then:
        settings.contains('''\
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id.startsWith("com.google.cloud.tools.appengine")) {
                useModule("com.google.cloud.tools:appengine-gradle-plugin:${requested.version}")
            }
        }
    }
}''')
    }
}
