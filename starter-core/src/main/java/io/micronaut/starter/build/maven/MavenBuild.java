/*
 * Copyright 2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.build.maven;

import com.fizzed.rocker.RockerModel;
import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.starter.build.dependencies.Coordinate;
import io.micronaut.starter.build.Property;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import io.micronaut.starter.template.RockerWritable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MavenBuild {
    private static final Logger LOG = LoggerFactory.getLogger(MavenBuild.class);

    private final MavenCombineAttribute annotationProcessorCombineAttribute;

    private final MavenCombineAttribute testAnnotationProcessorCombineAttribute;

    private final List<Coordinate> testAnnotationProcessors;

    private final List<Coordinate> annotationProcessors;

    private final List<MavenDependency> dependencies;

    private final List<MavenBuildPlugin> plugins;

    private final List<Property> properties;

    public MavenBuild() {
        this(Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                MavenCombineAttribute.APPEND,
                MavenCombineAttribute.APPEND);
    }

    public MavenBuild(@NonNull List<Coordinate> annotationProcessors,
                      @NonNull List<Coordinate> testAnnotationProcessors,
                      @NonNull List<MavenDependency> dependencies,
                      @NonNull List<Property> properties,
                      @NonNull List<MavenBuildPlugin> plugins,
                      @NonNull MavenCombineAttribute annotationProcessorCombineAttribute,
                      @NonNull MavenCombineAttribute testAnnotationProcessorCombineAttribute) {
        this.annotationProcessors = annotationProcessors;
        this.testAnnotationProcessors = testAnnotationProcessors;
        this.dependencies = dependencies;
        this.properties = properties;
        this.plugins = plugins;
        this.annotationProcessorCombineAttribute = annotationProcessorCombineAttribute;
        this.testAnnotationProcessorCombineAttribute = testAnnotationProcessorCombineAttribute;
    }

    @NonNull
    public String renderPlugins(int indentationSpaces) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        plugins.stream()
                .map(mavenBuildPlugin -> {
                    if (mavenBuildPlugin.getExtension() != null) {
                        return mavenBuildPlugin.getExtension();
                    }
                    RockerModel model = mavenPlugin.template(mavenBuildPlugin.getGroupId(), mavenBuildPlugin.getArtifactId(), mavenBuildPlugin.getVersion());
                    return new RockerWritable(model);
                })
                .forEach(writable -> {
                    try {
                        writable.write(outputStream);
                    } catch (IOException e) {
                        if (LOG.isErrorEnabled()) {
                            LOG.error("IO Exception rendering Gradle Plugin extension");
                        }
                    }
                });
        String str = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
        if (indentationSpaces == 0) {
            return str;
        }
        String[] lines = str.split("\n");
        List<String> indentedLines = new ArrayList<>();
        StringBuilder newLine = new StringBuilder();
        for (int i = 0; i < indentationSpaces; i++) {
            newLine.append(" ");
        }
        for (String originalLine : lines) {
            indentedLines.add(newLine + originalLine);
        }
        return String.join("\n", indentedLines) + "\n";
    }

    @NonNull
    public List<Coordinate> getAnnotationProcessors() {
        return annotationProcessors;
    }

    @NonNull
    public List<Coordinate> getTestAnnotationProcessors() {
        return testAnnotationProcessors;
    }

    @NonNull
    public List<MavenDependency> getDependencies() {
        return dependencies;
    }

    @NonNull
    public List<Property> getProperties() {
        return properties;
    }

    public MavenCombineAttribute getAnnotationProcessorCombineAttribute() {
        return annotationProcessorCombineAttribute;
    }

    @NonNull
    public MavenCombineAttribute getTestAnnotationProcessorCombineAttribute() {
        return testAnnotationProcessorCombineAttribute;
    }
}
