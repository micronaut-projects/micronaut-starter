/*
 * Copyright 2017-2025 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.graalvm;

import com.fizzed.rocker.runtime.DefaultRockerModel;
import io.micronaut.core.util.StringUtils;
import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.hosted.RuntimeReflection;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

/**
 * GraalVM native-image feature that discovers Rocker-generated template classes on the
 * application classpath and registers them for reflection.
 * <p>
 * Discovery is automatic and does not depend on a hardcoded class list: classpath locations
 * are collected from JVM classpath entries, classloader URLs, and manifest resources, then
 * candidate classes are filtered by Rocker invariants (model classes plus their
 * {@code $Template}/{@code $PlainText} nested classes).
 */
public final class RockerTemplateReflectionFeature implements Feature {
    private static final String CLASS_SUFFIX = ".class";
    private static final boolean LOG = false;

    @Override
    public void beforeAnalysis(BeforeAnalysisAccess access) {
        ClassLoader classLoader = access.getApplicationClassLoader();
        log("feature started");

        Set<Path> locations;
        try {
            locations = discoverClassPathLocations(classLoader);
        } catch (IOException e) {
            throw new IllegalStateException("Error discovering classpath locations for rocker templates", e);
        }
        log("classpath locations discovered=" + locations.size());

        Set<String> candidateClassNames;
        try {
            candidateClassNames = discoverCandidateClassNames(locations);
        } catch (IOException e) {
            throw new IllegalStateException("Error scanning classpath for rocker templates", e);
        }
        log("candidate classes discovered=" + candidateClassNames.size());

        int matched = 0;
        int registered = 0;
        int loadFailures = 0;
        for (String className : candidateClassNames) {
            try {
                Class<?> clazz = Class.forName(className, false, classLoader);
                if (!isRockerTemplateClass(clazz)) {
                    continue;
                }
                matched++;
                registerAllDeclared(clazz);
                registered++;
                log("registered class=" + className);
            } catch (ClassNotFoundException e) {
                loadFailures++;
                log("failed to load candidate class=" + className + " reason=" + e.getMessage());
            }
        }
        log("rocker matched classes=" + matched + " registered=" + registered + " loadFailures=" + loadFailures);
    }

    private static void registerAllDeclared(Class<?> clazz) {
        RuntimeReflection.register(clazz);
        RuntimeReflection.register(clazz.getDeclaredConstructors());
        RuntimeReflection.register(clazz.getDeclaredMethods());
        RuntimeReflection.register(clazz.getDeclaredFields());
    }

    private static Set<Path> discoverClassPathLocations(ClassLoader classLoader) throws IOException {
        Set<Path> locations = new HashSet<>();
        collectFromJavaClassPath(locations);
        collectFromClassLoaderUrls(classLoader, locations);
        collectFromManifestResources(classLoader, locations);
        return locations;
    }

    private static void collectFromJavaClassPath(Set<Path> locations) {
        String classPath = System.getProperty("java.class.path", "");
        for (String entry : classPath.split(File.pathSeparator)) {
            if (StringUtils.isNotEmpty(entry)) {
                locations.add(Paths.get(entry));
            }
        }
    }

    private static void collectFromClassLoaderUrls(ClassLoader classLoader, Set<Path> locations) {
        if (classLoader instanceof URLClassLoader urlClassLoader) {
            for (URL url : urlClassLoader.getURLs()) {
                addUrlPath(url, locations);
            }
            return;
        }
        try {
            Method getUrls = classLoader.getClass().getMethod("getURLs");
            Object result = getUrls.invoke(classLoader);
            if (result instanceof URL[] urls) {
                for (URL url : urls) {
                    addUrlPath(url, locations);
                }
            }
        } catch (Exception ignored) {
            log("classloader does not expose getURLs: " + classLoader.getClass().getName());
        }
    }

    private static void collectFromManifestResources(ClassLoader classLoader, Set<Path> locations) throws IOException {
        Enumeration<URL> resources = classLoader.getResources("META-INF/MANIFEST.MF");
        while (resources.hasMoreElements()) {
            URL manifestUrl = resources.nextElement();
            try {
                if ("jar".equals(manifestUrl.getProtocol())) {
                    JarURLConnection connection = (JarURLConnection) manifestUrl.openConnection();
                    addUrlPath(connection.getJarFileURL(), locations);
                } else if ("file".equals(manifestUrl.getProtocol())) {
                    Path manifestPath = toPath(manifestUrl);
                    if (manifestPath != null && manifestPath.getParent() != null && manifestPath.getParent().getParent() != null) {
                        locations.add(manifestPath.getParent().getParent());
                    }
                }
            } catch (ClassCastException ignored) {
                log("manifest URL not castable to JarURLConnection=" + manifestUrl);
            }
        }
    }

    private static Set<String> discoverCandidateClassNames(Set<Path> locations) throws IOException {
        Set<String> classNames = new HashSet<>();
        for (Path location : locations) {
            if (location == null || !Files.exists(location)) {
                continue;
            }
            if (Files.isDirectory(location)) {
                classNames.addAll(findClassFilesInDirectory(location));
            } else if (location.toString().endsWith(".jar") && Files.isRegularFile(location)) {
                classNames.addAll(findClassFilesInJar(location));
            }
        }
        return classNames;
    }

    private static Set<String> findClassFilesInDirectory(Path rootDirectory) {
        try (Stream<Path> files = Files.walk(rootDirectory)) {
            Set<String> classNames = new HashSet<>();
            files.filter(path -> path.toString().endsWith(CLASS_SUFFIX)).forEach(path -> {
                String relative = rootDirectory.relativize(path).toString().replace('\\', '/');
                String className = relative.substring(0, relative.length() - CLASS_SUFFIX.length()).replace('/', '.');
                if (!className.endsWith(".module-info") && !className.endsWith(".package-info") && className.contains(".rocker.")) {
                    classNames.add(className);
                }
            });
            return classNames;
        } catch (IOException e) {
            throw new IllegalStateException("Unable to scan directory for classes: " + rootDirectory, e);
        }
    }

    private static Set<String> findClassFilesInJar(Path jarPath) throws IOException {
        Set<String> classNames = new HashSet<>();
        try (JarFile jarFile = new JarFile(jarPath.toFile())) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                if (entry.isDirectory() || !name.endsWith(CLASS_SUFFIX) || !name.contains("/rocker/")) {
                    continue;
                }
                String className = name.substring(0, name.length() - CLASS_SUFFIX.length()).replace('/', '.');
                if (!className.endsWith(".module-info") && !className.endsWith(".package-info")) {
                    classNames.add(className);
                }
            }
        }
        return classNames;
    }

    private static boolean isRockerTemplateClass(Class<?> clazz) {
        if (DefaultRockerModel.class.isAssignableFrom(clazz)) {
            return true;
        }
        String name = clazz.getName();
        if (!name.endsWith("$Template") && !name.endsWith("$PlainText")) {
            return false;
        }
        Class<?> enclosingClass = clazz.getEnclosingClass();
        return enclosingClass != null && DefaultRockerModel.class.isAssignableFrom(enclosingClass);
    }

    private static void addUrlPath(URL url, Set<Path> locations) {
        Path path = toPath(url);
        if (path != null) {
            locations.add(path);
        }
    }

    private static Path toPath(URL url) {
        try {
            return Paths.get(url.toURI());
        } catch (URISyntaxException e) {
            try {
                return Paths.get(url.getPath());
            } catch (Exception ignored) {
                log("unable to convert URL to path=" + url + " reason=" + e.getMessage());
                return null;
            }
        }
    }

    private static void log(String message) {
        if (LOG) {
            System.out.println("[RockerTemplateReflectionFeature] " + message);
        }
    }
}
