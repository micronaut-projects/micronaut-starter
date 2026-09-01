/*
 * Copyright 2017-2024 original authors
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

import org.jspecify.annotations.NonNull;
import io.micronaut.starter.options.BuildTool;

public final class MicronautDependencyUtils {
    public static final String ARTIFACT_ID_MICRONAUT_DATA_TX_HIBERNATE = "micronaut-data-tx-hibernate";
    public static final String ARTIFACT_ID_MICRONAUT_DATA_PROCESSOR_ARTIFACT = "micronaut-data-processor";
    public static final String ARTIFACT_ID_MICRONAUT_INJECT = "micronaut-inject";
    public static final Dependency MICRONAUT_INJECT = coreDependency()
            .artifactId(ARTIFACT_ID_MICRONAUT_INJECT)
            .compile()
            .build();

    public static final String GROUP_ID_MICRONAUT_LANGCHAIN4J = "io.micronaut.langchain4j";
    public static final String GROUP_ID_MICRONAUT_GUICE = "io.micronaut.guice";
    public static final String GROUP_ID_MICRONAUT = "io.micronaut";
    public static final String GROUP_ID_MICRONAUT_TESTRESOURCES = "io.micronaut.testresources";
    public static final String  GROUP_ID_MICRONAUT_JAXRS = "io.micronaut.jaxrs";
    public static final String ARTIFACT_ID_MICRONAUT_CORE_PROCESSOR = "micronaut-core-processor";
    public static final String ARTIFACT_ID_MICRONAUT_INJECT_JAVA = "micronaut-inject-java";
    public static final String GROUP_ID_MICRONAUT_AWS = "io.micronaut.aws";
    public static final String GROUP_ID_MICRONAUT_AZURE = "io.micronaut.azure";
    public static final String GROUP_ID_MICRONAUT_CASSANDRA = "io.micronaut.cassandra";
    public static final String GROUP_ID_MICRONAUT_CHATBOTS = "io.micronaut.chatbots";
    public static final String GROUP_ID_MICRONAUT_COHERENCE = "io.micronaut.coherence";
    public static final String GROUP_ID_MICRONAUT_CRAC = "io.micronaut.crac";
    public static final String GROUP_ID_MICRONAUT_ECLIPSESTORE = "io.micronaut.eclipsestore";
    public static final String GROUP_ID_MICRONAUT_GCP = "io.micronaut.gcp";
    public static final String GROUP_ID_MICRONAUT_JSON_SCHEMA = "io.micronaut.jsonschema";
    public static final String GROUP_ID_MICRONAUT_GRAAL_LANGUAGES = "io.micronaut.graal-languages";    
    public static final String GROUP_ID_MICRONAUT_KAFKA = "io.micronaut.kafka";
    public static final String GROUP_ID_MICRONAUT_OCI = "io.micronaut.oraclecloud";
    public static final String GROUP_ID_MICRONAUT_OPENSEARCH = "io.micronaut.opensearch";
    public static final String GROUP_ID_MICRONAUT_SERDE = "io.micronaut.serde";
    public static final String GROUP_ID_MICRONAUT_REACTOR = "io.micronaut.reactor";
    public static final String GROUP_ID_MICRONAUT_SECURITY = "io.micronaut.security";
    public static final String GROUP_ID_MICRONAUT_SESSION = "io.micronaut.session";
    public static final String GROUP_ID_MICRONAUT_SERVLET = "io.micronaut.servlet";
    public static final String GROUP_ID_MICRONAUT_SOURCEGEN = "io.micronaut.sourcegen";
    public static final String GROUP_ID_MICRONAUT_TRACING = "io.micronaut.tracing";
    public static final String GROUP_ID_MICRONAUT_TEST = "io.micronaut.test";
    public static final String GROUP_ID_MICRONAUT_R2DBC = "io.micronaut.r2dbc";
    public static final String GROUP_ID_MICRONAUT_DATA = "io.micronaut.data";
    public static final String GROUP_ID_MICRONAUT_SQL = "io.micronaut.sql";
    public static final String GROUP_ID_MICRONAUT_STARTER = "io.micronaut.starter";
    public static final String GROUP_ID_MICRONAUT_KOTLIN = "io.micronaut.kotlin";

    public static final String GROUP_ID_MICRONAUT_MICROMETER = "io.micronaut.micrometer";
    public static final String ARTIFACT_ID_PREFIX_MICRONAUT_MICROMETER = "micronaut-micrometer-";

    public static final String GROUP_ID_MICRONAUT_GROOVY = "io.micronaut.groovy";
    public static final String GROUP_ID_IO_MICRONAUT_NEO4J = "io.micronaut.neo4j";
    public static final String GROUP_ID_IO_MICRONAUT_OPENAPI = "io.micronaut.openapi";
    public static final String GROUP_ID_IO_MICRONAUT_SERVLET = "io.micronaut.servlet";
    public static final String GROUP_ID_IO_MICRONAUT_VALIDATION = "io.micronaut.validation";

    public static final String GROUP_ID_MICRONAUT_PLATFORM = "io.micronaut.platform";

    public static final String GROUP_ID_MICRONAUT_GRPC = "io.micronaut.grpc";
    public static final String GROUP_ID_IO_MICRONAUT_JMS = "io.micronaut.jms";
    public static final String GROUP_ID_IO_MICRONAUT_ORACLE_CLOUD = "io.micronaut.oraclecloud";
    public static final String GROUP_ID_IO_MICRONAUT_PICOCLI = "io.micronaut.picocli";
    public static final String GROUP_ID_IO_MICRONAUT_DISCOVERY = "io.micronaut.discovery";
    public static final String GROUP_ID_MICRONAUT_ELASTICSEARCH = "io.micronaut.elasticsearch";
    public static final String GROUP_ID_IO_MICRONAUT_CONTROLPANEL = "io.micronaut.controlpanel";
    public static final String GROUP_ID_MICRONAUT_FLYWAY = "io.micronaut.flyway";
    public static final String GROUP_ID_MICRONAUT_SPRING = "io.micronaut.spring";
    public static final String GROUP_ID_MICRONAUT_VIEWS = "io.micronaut.views";
    public static final String GROUP_ID_MICRONAUT_MCP = "io.micronaut.mcp";

    private MicronautDependencyUtils() {

    }

    public static Dependency.@NonNull Builder langchain4j() {
        return micronautDependency(GROUP_ID_MICRONAUT_LANGCHAIN4J);
    }

    public static Dependency.@NonNull Builder guiceDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_GUICE);
    }

    public static Dependency.@NonNull Builder coreDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT);
    }

    public static Dependency.@NonNull Builder jaxrsDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_JAXRS);
    }

    public static Dependency.@NonNull Builder awsDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_AWS);
    }

    public static Dependency.@NonNull Builder starterDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_STARTER);
    }

    public static Dependency.@NonNull Builder azureDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_AZURE);
    }

    public static Dependency.@NonNull Builder mcpDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_MCP);
    }

    public static Dependency.@NonNull Builder reactorDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_REACTOR);
    }

    public static Dependency.@NonNull Builder serdeDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_SERDE);
    }

    public static Dependency.@NonNull Builder securityDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_SECURITY);
    }

    public static Dependency.@NonNull Builder sessionDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_SESSION);
    }

    public static Dependency.@NonNull Builder servletDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_SERVLET);
    }

    public static Dependency.@NonNull Builder sourcegenDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_SOURCEGEN);
    }

    public static Dependency.@NonNull Builder testDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_TEST);
    }

    public static Dependency.@NonNull Builder r2dbcDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_R2DBC);
    }

    public static Dependency.@NonNull Builder tracingDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_TRACING);
    }

    public static Dependency.@NonNull Builder dataDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_DATA);
    }

    public static Dependency.@NonNull Builder sqlDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_SQL);
    }

    public static Dependency.@NonNull Builder kotlinDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_KOTLIN);
    }

    public static Dependency.@NonNull Builder micrometerDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_MICROMETER);
    }

    public static Dependency.@NonNull Builder eclipsestoreDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_ECLIPSESTORE);
    }

    public static Dependency.@NonNull Builder micrometerRegistryDependency(@NonNull String implementationName) {
        return micrometerDependency().artifactId("micronaut-micrometer-registry-" + implementationName);
    }

    public static Dependency.@NonNull Builder groovyDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_GROOVY);
    }

    private static Dependency.@NonNull Builder micronautDependency(@NonNull String groupId) {
        return Dependency.builder()
                .groupId(groupId);
    }

    public static Dependency.@NonNull Builder cracDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_CRAC);
    }

    public static Dependency.@NonNull Builder cassandraDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_CASSANDRA);
    }

    public static Dependency.@NonNull Builder chatBotsDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_CHATBOTS);
    }

    public static Dependency.@NonNull Builder coherenceDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_COHERENCE);
    }

    public static Dependency.@NonNull Builder gcpDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_GCP);
    }

    public static Dependency.@NonNull Builder graalLanguagesDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_GRAAL_LANGUAGES);
    }

    public static Dependency.@NonNull Builder jsonSchemaDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_JSON_SCHEMA);
    }

    public static Dependency.@NonNull Builder kafkaDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_KAFKA);
    }

    public static Dependency.@NonNull Builder ociDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_OCI);
    }

    public static Dependency.@NonNull Builder opensearchDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_OPENSEARCH);
    }

    public static Dependency.@NonNull Builder platformDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_PLATFORM);
    }

    public static Dependency.@NonNull Builder grpcDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_GRPC);
    }

    public static Dependency.@NonNull Builder jmsDependency() {
        return micronautDependency(GROUP_ID_IO_MICRONAUT_JMS);
    }

    public static Dependency.@NonNull Builder neo4j() {
        return micronautDependency(GROUP_ID_IO_MICRONAUT_NEO4J);
    }

    public static Dependency.@NonNull Builder openapi() {
        return micronautDependency(GROUP_ID_IO_MICRONAUT_OPENAPI);
    }

    public static Dependency.@NonNull Builder validationDependency() {
        return micronautDependency(GROUP_ID_IO_MICRONAUT_VALIDATION);
    }

    public static Dependency.@NonNull Builder injectJava() {
        return coreDependency().artifactId(ARTIFACT_ID_MICRONAUT_INJECT_JAVA);
    }

    public static Dependency.@NonNull Builder coreProcessor() {
        return coreDependency().artifactId(ARTIFACT_ID_MICRONAUT_CORE_PROCESSOR);
    }

    public static Dependency.@NonNull Builder picocliDependency() {
        return micronautDependency(GROUP_ID_IO_MICRONAUT_PICOCLI);
    }

    public static Dependency.@NonNull Builder discovery() {
        return micronautDependency(GROUP_ID_IO_MICRONAUT_DISCOVERY);
    }

    public static Dependency.@NonNull Builder flywayDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_FLYWAY);
    }

    public static Dependency.@NonNull Builder annotationProcessor(@NonNull BuildTool buildTool,
                                                         @NonNull String groupId,
                                                         @NonNull String artifactId,
                                                         @NonNull String propertyName) {
        return annotationProcessor(buildTool, groupId, artifactId, propertyName, false);
    }

    public static Dependency.@NonNull Builder annotationProcessor(@NonNull BuildTool buildTool,
                                                         @NonNull String groupId,
                                                         @NonNull String artifactId,
                                                         @NonNull String propertyName,
                                                         boolean requiresPriority) {
        return switch (buildTool) {
            case GRADLE, GRADLE_KOTLIN -> Dependency.builder()
                    .groupId(groupId)
                    .artifactId(artifactId)
                    .annotationProcessor();
            case PYRONAUT -> Dependency.builder()
                    .groupId(groupId)
                    .artifactId(artifactId)
                    .annotationProcessor(requiresPriority);
            case MAVEN -> moduleMavenAnnotationProcessor(groupId, artifactId, propertyName, false, requiresPriority);
        };
    }

    public static Dependency.@NonNull Builder testAnnotationProcessor(@NonNull BuildTool buildTool,
                                                             @NonNull String groupId,
                                                             @NonNull String artifactId,
                                                             @NonNull String propertyName) {
        return testAnnotationProcessor(buildTool, groupId, artifactId, propertyName, false);
    }

    public static Dependency.@NonNull Builder testAnnotationProcessor(@NonNull BuildTool buildTool,
                                                         @NonNull String groupId,
                                                         @NonNull String artifactId,
                                                         @NonNull String propertyName,
                                                         boolean requiresPriority) {
        return switch (buildTool) {
            case GRADLE, GRADLE_KOTLIN -> Dependency.builder()
                    .groupId(groupId)
                    .artifactId(artifactId)
                    .testAnnotationProcessor();
            case PYRONAUT -> Dependency.builder()
                    .groupId(groupId)
                    .artifactId(artifactId)
                    .testAnnotationProcessor(requiresPriority);
            case MAVEN -> moduleMavenAnnotationProcessor(groupId, artifactId, propertyName, true, requiresPriority);
        };
    }

    public static Dependency.@NonNull Builder moduleMavenAnnotationProcessor(@NonNull String groupId,
                                                                    @NonNull String artifactId,
                                                                    @NonNull String propertyName,
                                                                    boolean isTestScope,
                                                                    boolean requiresPriority) {
        Dependency.Builder dependency = Dependency.builder()
                .groupId(groupId)
                .artifactId(artifactId)
                .exclude(MICRONAUT_INJECT)
                .versionProperty(propertyName);

        return isTestScope ? dependency.testAnnotationProcessor(requiresPriority) : dependency.annotationProcessor(requiresPriority);
    }

    public static Dependency.@NonNull Builder controlPanelDependency() {
        return micronautDependency(GROUP_ID_IO_MICRONAUT_CONTROLPANEL).developmentOnly();
    }

    public static Dependency.@NonNull Builder elasticSearchDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_ELASTICSEARCH);
    }

    public static Dependency.@NonNull Builder oracleCloudDependency() {
        return micronautDependency(GROUP_ID_IO_MICRONAUT_ORACLE_CLOUD);
    }

    public static Dependency.Builder springDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_SPRING);
    }

    public static Dependency.@NonNull Builder viewsDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_VIEWS);
    }
}
