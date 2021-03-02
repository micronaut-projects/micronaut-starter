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
package io.micronaut.starter.build.dependencies;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.core.io.ResourceResolver;
import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.build.maven.MavenCoordinate;
import io.micronaut.starter.build.maven.MavenCoordinateImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.inject.Singleton;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class PomDependencyVersionResolver implements DependencyVersionResolver {

    private static final String NODE_NAME_TEXT = "#text";
    private Map<String, MavenCoordinate> coordinates;

    public PomDependencyVersionResolver(ResourceResolver resourceResolver) {
        Map<String, MavenCoordinate> coordinates = new HashMap<>();
        for (URL url : resourceResolver.getResources("classpath:pom.xml").collect(Collectors.toList())) {
            try {
                InputStream inputStream = url.openStream();
                Document doc = documentFor(inputStream);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("dependency");
                for (int i = 0; i < nList.getLength(); i++) {
                    Node node = nList.item(i);
                    NodeList childNodes = node.getChildNodes();
                    MavenCoordinateImpl mavenCoordinate = new MavenCoordinateImpl();
                    for (int x = 0; x < childNodes.getLength(); x++) {
                        Node child = childNodes.item(x);
                        if (child.getNodeName().equals("version")) {
                            valueOfNode(child).ifPresent(mavenCoordinate::setVersion);
                        }
                        if (child.getNodeName().equals("groupId")) {
                            valueOfNode(child).ifPresent(mavenCoordinate::setGroupId);
                        }
                        if (child.getNodeName().equals("artifactId")) {
                            valueOfNode(child).ifPresent(mavenCoordinate::setArtifactId);
                        }
                    }
                    if (StringUtils.isNotEmpty(mavenCoordinate.getGroupId()) && StringUtils.isNotEmpty(mavenCoordinate.getArtifactId())) {
                        coordinates.put(mavenCoordinate.getArtifactId(), mavenCoordinate);
                    }
                }
            } catch (IOException | SAXException | ParserConfigurationException e) {
                e.printStackTrace();
            }
        }
        this.coordinates = coordinates;
    }

    @Override
    @NonNull
    public Optional<MavenCoordinate> findByArtifactId(@NonNull String artifactId) {
        return Optional.ofNullable(coordinates.get(artifactId));
    }

    private static Document documentFor(@NonNull InputStream inputStream)
            throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        return dBuilder.parse(inputStream);
    }

    @NonNull
    private Optional<String> valueOfNode(@NonNull Node node) {
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
