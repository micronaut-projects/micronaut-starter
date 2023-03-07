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
package io.micronaut.starter.coordinates;

import org.gradle.api.DefaultTask;
import org.gradle.api.artifacts.MinimalExternalModuleDependency;
import org.gradle.api.artifacts.ModuleIdentifier;
import org.gradle.api.artifacts.VersionCatalog;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.internal.file.FileOperations;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.CacheableTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

import javax.inject.Inject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@CacheableTask
public abstract class CoordinatesSourceGenerator extends DefaultTask {
    @Internal
    public abstract Property<VersionCatalog> getVersionCatalog();

    @Input
    public Provider<Set<String>> getCoordinates() {
        return getVersionCatalog().map(versionCatalog -> versionCatalog.getLibraryAliases()
                .stream()
                .map(alias -> {
                    Optional<Provider<MinimalExternalModuleDependency>> library = versionCatalog.findLibrary(alias);
                    if (library.isPresent()) {
                        MinimalExternalModuleDependency lib = library.get().get();
                        String artifactId = lib.getModule().getName();
                        String groupId = lib.getModule().getGroup();
                        String version = lib.getVersionConstraint().getRequiredVersion();
                        String pom = alias.startsWith("bom-") ? "true" : "false";
                        return artifactId + ":" + groupId + ":" + version + ":" + pom;
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet()));
    }

    @Input
    public abstract Property<String> getPackageName();

    @OutputDirectory
    public abstract DirectoryProperty getOutputDirectory();

    @Inject
    protected abstract FileOperations getFileOperations();

    @TaskAction
    public void generateSources() throws IOException {
        File outputDirectory = getOutputDirectory().get().getAsFile();
        String packageName = getPackageName().get();
        Path packageDirectory = outputDirectory.toPath().resolve(packageName.replace('.', '/'));
        getFileOperations().delete(outputDirectory);
        Files.createDirectories(packageDirectory);
        try (PrintWriter writer = new PrintWriter(new FileWriter(new File(packageDirectory.toFile(), "StarterCoordinates.java")))) {
            writer.println("package " + packageName + ";");
            writer.println();
            writer.println("import java.util.HashMap;");
            writer.println("import java.util.Map;");
            writer.println("import java.util.Collections;");
            writer.println();
            writer.println("public class StarterCoordinates {");
            writer.println("    public static final Map<String, Coordinate> ALL_COORDINATES;");
            Map<String, String> coordinatesMap = new LinkedHashMap<>();
            VersionCatalog versionCatalog = getVersionCatalog().get();
            writeDependencies(writer, coordinatesMap, versionCatalog, true);
            writeDependencies(writer, coordinatesMap, versionCatalog, false);
            writer.println("    static {");
            writer.println("        Map<String, Coordinate> coordinates = new HashMap<>();");
            for (Map.Entry<String, String> entry : coordinatesMap.entrySet()) {
                writer.println("        coordinates.put(\"" + entry.getKey() + "\", " + entry.getValue() + ");");
            }
            writer.println("        ALL_COORDINATES = Collections.unmodifiableMap(coordinates);");
            writer.println("    }");
            writer.println("}");
        }
    }

    private static void writeDependencies(PrintWriter writer,
                                          Map<String, String> coordinatesMap,
                                          VersionCatalog versionCatalog,
                                          boolean bom) {
        versionCatalog.getLibraryAliases()
                .stream()
                .filter(alias -> bom == alias.startsWith("bom."))
                .map(versionCatalog::findLibrary)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Provider::get)
                .sorted(Comparator.comparing(MinimalExternalModuleDependency::getModule, Comparator.comparing(ModuleIdentifier::getName)))
                .forEachOrdered(lib -> {
                    String artifactId = lib.getModule().getName();
                    String groupId = lib.getModule().getGroup();
                    String version = lib.getVersionConstraint().getRequiredVersion();
                    String pom = bom ? "true" : "false";
                    String name = artifactId.toUpperCase().replace('-', '_').replace('.', '_');
                    writer.println("    public static final Coordinate " + name + " = Dependency.builder()\n" +
                                   "                .groupId(\"" + groupId + "\")\n" +
                                   "                .artifactId(\"" + artifactId + "\")\n" +
                                   "                .version(\"" + version + "\")\n" +
                                   "                .pom(" + pom + ")\n" +
                                   "                .buildCoordinate();");
                    coordinatesMap.put(artifactId, name);
                });
    }
}
