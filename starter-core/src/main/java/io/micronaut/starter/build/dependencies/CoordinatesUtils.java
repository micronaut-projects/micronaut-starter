/*
 * Copyright 2017-2022 original authors
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
package io.micronaut.starter.build.dependencies;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public final class CoordinatesUtils {
    private static final Logger LOG = LoggerFactory.getLogger(CoordinatesUtils.class);

    private static final String NODE_NAME_TEXT = "#text";

    private CoordinatesUtils() {
    }

    public static Map<String, Coordinate> readCoordinates(Stream<URL> urls) {
        Map<String, Coordinate> coordinates = new HashMap<>();
        urls.forEach(url -> {
            try (InputStream inputStream = url.openStream()) {
                Document doc = documentFor(inputStream);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("dependency");
                for (int i = 0; i < nList.getLength(); i++) {
                    Node node = nList.item(i);
                    NodeList childNodes = node.getChildNodes();
                    String groupId = null;
                    String artifactId = null;
                    String version = null;
                    boolean pom = false;
                    for (int x = 0; x < childNodes.getLength(); x++) {
                        Node child = childNodes.item(x);
                        if ("version".equals(child.getNodeName())) {
                            if (valueOfNode(child).isPresent()) {
                                version = valueOfNode(child).get();
                            }
                        }
                        if ("groupId".equals(child.getNodeName())) {
                            if (valueOfNode(child).isPresent()) {
                                groupId = valueOfNode(child).get();
                            }
                        }
                        if ("artifactId".equals(child.getNodeName())) {
                            if (valueOfNode(child).isPresent()) {
                                artifactId = valueOfNode(child).get();
                            }
                        }
                        if ("type".equals(child.getNodeName())) {
                            if (valueOfNode(child).isPresent()) {
                                pom = "pom".equalsIgnoreCase(valueOfNode(child).get());
                            }
                        }
                    }

                    if (StringUtils.isNotEmpty(groupId) && StringUtils.isNotEmpty(artifactId)) {
                        DependencyCoordinate dependencyCoordinate = Dependency.builder()
                                .groupId(groupId)
                                .artifactId(artifactId)
                                .version(version)
                                .pom(pom)
                                .buildCoordinate();
                        coordinates.put(dependencyCoordinate.getArtifactId(), dependencyCoordinate);
                    }
                }
            } catch (IOException | ParserConfigurationException | SAXException e) {
                if (LOG.isErrorEnabled()) {
                    LOG.error("could not parse coordinates", e);
                }
            }
        });
        return coordinates;
    }

    private static Document documentFor(@NonNull InputStream inputStream)
            throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        return dBuilder.parse(inputStream);
    }

    @NonNull
    private static Optional<String> valueOfNode(@NonNull Node node) {
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeName().equals(NODE_NAME_TEXT)) {
                return Optional.of(child.getNodeValue());
            }
        }
        return Optional.empty();
    }
}
