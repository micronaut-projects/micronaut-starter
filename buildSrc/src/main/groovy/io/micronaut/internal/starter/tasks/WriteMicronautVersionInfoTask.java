/*
 * Copyright 2003-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.internal.starter.tasks;

import groovy.xml.XmlSlurper;
import groovy.xml.slurpersupport.GPathResult;
import groovy.xml.slurpersupport.NodeChild;
import org.gradle.api.DefaultTask;
import org.gradle.api.artifacts.result.ArtifactResolutionResult;
import org.gradle.api.artifacts.result.ComponentArtifactsResult;
import org.gradle.api.artifacts.result.ResolvedArtifactResult;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.CacheableTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;
import org.gradle.maven.MavenModule;
import org.gradle.maven.MavenPomArtifact;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

@CacheableTask
public abstract class WriteMicronautVersionInfoTask extends DefaultTask {
    @Input
    public abstract Property<String> getVersion();

    @Input abstract ListProperty<String> getExtraBomProperties();

    @OutputDirectory
    public abstract DirectoryProperty getOutputDirectory();

    public WriteMicronautVersionInfoTask() {
        getOutputs().doNotCacheIf("snapshot version", spec -> getVersion().get().endsWith("SNAPSHOT"));
    }

    @TaskAction
    public void writeVersionInfo() throws IOException {
        Map<String, String> props = generateProperties();
        try (OutputStream out = new FileOutputStream(getOutputDirectory().file("micronaut-versions.properties").get().getAsFile())) {
            for (Map.Entry<String, String> entry : props.entrySet()) {
                String line = entry.getKey() + "=" + entry.getValue() + "\n";
                out.write(line.getBytes(StandardCharsets.ISO_8859_1));
            }
        }
    }

    private Map<String, String> generateProperties() {
        Map<String, String> props = new TreeMap<>();

        props.put("micronaut.version", getVersion().get());

        props.putAll(bomProperties("io.micronaut.platform", "micronaut-platform", getVersion().get()));

        for (String extraBomProperty : getExtraBomProperties().get()) {
            String[] groupAndArtifact = extraBomProperty.split(":", 2);
            String groupId = groupAndArtifact[0];
            String artifactId = groupAndArtifact[1];
            String key = artifactId.replace("-", ".") + ".version";
            String version = props.get(key);

            Map<String, String> bomProperties = bomProperties(groupId, artifactId + "-bom", version);
            for (Map.Entry<String, String> entry : bomProperties.entrySet()) {
                if (entry.getKey().startsWith("micronaut.")) {
                    getLogger().lifecycle("Skipping {} from {}", entry.getKey(), extraBomProperty);
                } else {
                    if (props.containsKey(entry.getKey())) {
                        getLogger().warn("Property {} from {} already exists ({}). Replacing with {}", entry.getKey(), extraBomProperty, props.get(entry.getKey()), entry.getValue());
                    }
                    props.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return props;
    }

    private Map<String, String> bomProperties(String groupId, String artifactId, String version) {
        ArtifactResolutionResult result = getProject().getDependencies().createArtifactResolutionQuery()
                .forModule(groupId, artifactId, version)
                .withArtifacts(MavenModule.class, MavenPomArtifact.class)
                .execute();
        Map<String, String> props = new TreeMap<>();
        for (ComponentArtifactsResult component : result.getResolvedComponents()) {
            component.getArtifacts(MavenPomArtifact.class).forEach(artifact -> {
                if (artifact instanceof ResolvedArtifactResult) {
                    ResolvedArtifactResult resolved = (ResolvedArtifactResult) artifact;
                    GPathResult pom = null;
                    try {
                        pom = new XmlSlurper().parse(resolved.getFile());
                    } catch (IOException | SAXException | ParserConfigurationException e) {
                        // ignore
                    }
                    ((GPathResult) pom.getProperty("properties")).children().forEach(child -> {
                        NodeChild node = (NodeChild) child;
                        props.put(node.name(), node.text());
                    });
                }
            });
        }
        return props;
    }
}
