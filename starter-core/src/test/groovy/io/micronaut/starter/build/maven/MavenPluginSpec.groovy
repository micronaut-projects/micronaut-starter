package io.micronaut.starter.build.maven

import com.fizzed.rocker.RockerModel
import io.micronaut.starter.template.RockerWritable
import spock.lang.Specification
import io.micronaut.starter.feature.function.azure.template.azureFunctionMavenPlugin

class MavenPluginSpec extends Specification {

    void "MavenPlugin implements equals and hashCode"() {
        given:
        String mavenPluginArtifactId = "azure-functions-maven-plugin"
        RockerModel model = azureFunctionMavenPlugin.template()

        expect:
        instantiateMavenPlugin(mavenPluginArtifactId, model) == instantiateMavenPlugin(mavenPluginArtifactId, model)
    }

    private static MavenPlugin instantiateMavenPlugin(String mavenPluginArtifactId, RockerModel rockerModel) {
        MavenPlugin.builder()
                .artifactId(mavenPluginArtifactId)
                .extension(new RockerWritable(rockerModel))
                .build()
    }
}
